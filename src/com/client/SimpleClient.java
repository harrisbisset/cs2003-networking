package com.client;

import com.util.ResultError;
import com.util.SimpleUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient implements AutoCloseable {
    private final String addr;
    private final int port;
    private final Socket socket;
    private final PrintWriter output;
    private final BufferedReader input;
    private final BufferedReader stdInput;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimpleClient <IPaddress> <port>");
            System.exit(1);
        }

        // will exit if fails conversion from string
        int port    = SimpleUtil.getPort(args[1]);
        String addr = args[0];

        ResultError<SimpleClientConfig> config = SimpleClientConfig.
            New(addr, port).
            Build();

        if (!config.isPresent()) {
            System.err.printf("invalid config\n%s", config.getError());
            System.exit(3);
        }

        try (
            SimpleClient client = new SimpleClient(config.get());
        ) {
            System.out.println("starting client");
            client.ListenAndServe();
            System.out.println("closing client");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public SimpleClient(SimpleClientConfig config) throws IOException {
        this.addr = config.getAddress();
        this.port = config.getPort();

        this.socket   = new Socket(this.addr, this.port);
        this.output   = new PrintWriter(this.socket.getOutputStream(), true);
        this.input    = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));   
        this.stdInput = new BufferedReader(new InputStreamReader(System.in));
    }

    public void ListenAndServe() {
        String userInput;
        String serverMessage;

        try {
            while (this.socket.isConnected()) {
                userInput = this.stdInput.readLine();
                if (userInput == null) {
                    this.socket.close();
                    break;
                }
                this.writeMessage(userInput);

                serverMessage = this.input.readLine();
                if (serverMessage == null) {
                    this.socket.close();
                    break;
                }
                System.out.println(serverMessage);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public synchronized boolean writeMessage(String str) throws IOException {
        if (str == null) {
            return false;
        }
        this.output.println(str);
        this.output.flush();
        return true;
    }

    @Override
    public synchronized void close() throws IOException {
        this.input.close();
        this.output.close();
        this.socket.close();
        this.stdInput.close();
    }
}