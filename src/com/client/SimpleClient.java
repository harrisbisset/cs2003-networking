package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.util.ResultError;
import com.util.SimpleUtil;

public class SimpleClient {
    private final String addr;
    private final int port;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private BufferedReader stdInput;


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimpleClient <IPaddress> <port>");
            System.exit(1);
        }

        // will exit if fails conversion from string
        int port = SimpleUtil.getPort(args[1]);
        String addr = args[0];

        ResultError<SimpleClientConfig> config = SimpleClientConfig.
            New(addr, port).
            Build();

        if (!config.isPresent()) {
            System.err.printf("invalid config\n%s", config.getError());
            System.exit(3);
        }

        SimpleClient client = new SimpleClient(config.get());
        boolean started = client.start();
        if (!started) {
            System.exit(3);
        }
        System.out.println("starting client");
        client.ListenAndServe();
    }

    public SimpleClient(SimpleClientConfig config) {
        this.addr = config.getAddress();
        this.port = config.getPort();
    }

    public void ListenAndServe() {
        try {
            
            String userInput;
            while ((userInput = this.stdInput.readLine()) != null) {
                this.writeMessage(userInput);
            }
            this.stop();    

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public synchronized boolean start() {
        try {
            this.socket = new Socket(this.addr, this.port);
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));   
            this.stdInput = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }

    public synchronized boolean writeMessage(String str) throws IOException {
        if (str == null) {
            return false;
        }
        this.output.println(str);
        this.output.flush();
        return true;
    }

    public synchronized void stop() throws IOException {
        this.input.close();
        this.output.close();
        this.socket.close();
    }
}