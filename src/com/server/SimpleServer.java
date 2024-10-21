package com.server;

import com.server.responses.AdvancedProtocol;
import com.server.responses.IProtocol;
import com.util.ResultError;
import com.util.SimpleUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleServer implements AutoCloseable {
    private final ArrayList<HandleSimpleClient> connPool = new ArrayList<>();
    private final InetAddress address;
    private final Integer port;
    private final Integer threadLimit;
    private final IProtocol messageProtocol;
    private ServerSocket serverSocket = null;
    private int clientCount = 1;
    
    public static void main(String[] args) {
        
        if (args.length != 2) {
            System.out.println("Usage: java SimpleSever <IPaddress> <port>");
            System.exit(1);
        }

        // will exit if fails conversion from string
        InetAddress address = SimpleUtil.getInetAddress(args[0]);
        int port            = SimpleUtil.getPort(args[1]);

        ResultError<SimpleServerConfig> config = SimpleServerConfig.
            New(port, new AdvancedProtocol()).
            Address(address).
            Build();

        if (!config.isPresent()) {
            System.err.printf("invalid config\n%s", config.getError());
            System.exit(3);
        }

        try (
            SimpleServer server = new SimpleServer(config.get())
        ) {
            System.out.printf("listening on port: %d\n", server.port);
            server.ListenAndServe();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public SimpleServer(SimpleServerConfig config) {
        this.address = config.getAddress();
        this.port = config.getPort();
        this.threadLimit = config.getThreadLimit();
        this.messageProtocol = config.getMessageProtocol();
    }

    public void ListenAndServe() throws IOException {
        this.serverSocket = new ServerSocket(this.port, 50, this.address);

        while (true) {
            Socket socket = this.serverSocket.accept(); // new conn request

            // if too many connections
            if (this.clientCount >= this.threadLimit) {
                System.out.println("cannot exceed thread limit");
                continue;
            }
            
            System.out.printf("connection: %d\nnew client connected from %s, on port %d\n",
                this.clientCount,
                socket.getLocalAddress().toString(), 
                socket.getPort()
            );

            // new thread for the connection
            HandleSimpleClient conn = new HandleSimpleClient(
                this, 
                socket, 
                this.messageProtocol.generic(), 
                this.clientCount
            );

            new Thread(conn).start();
            this.connPool.add(conn);
            this.clientCount++;
        }
    }

    public synchronized boolean closeConn(int connNumber) {
        for (HandleSimpleClient hsc : this.connPool) {
            if (hsc.Value() == connNumber) {
                this.connPool.remove(hsc);
                this.clientCount--;
                System.out.println("closed client conn");
                return true;
            }
        }
        return false;
    }
    
    @Override
    public synchronized void close() throws Exception {
        this.serverSocket.close();
    }

    private class HandleSimpleClient implements Runnable {
        private final SimpleServer server;
        private final Socket socket;
        private final IProtocol messageProtocol;
        private final int value;

        public HandleSimpleClient(SimpleServer server, Socket socket, IProtocol messageProtocol, int value) {
            this.server = server;
            this.socket = socket;
            this.messageProtocol = messageProtocol;
            this.value = value;
        }

        public synchronized int Value() {
            return this.value;
        }

        @Override
        public synchronized void run() {
            try {
                try (this.socket) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    PrintWriter output = new PrintWriter(this.socket.getOutputStream(), true);
                    
                    String line;
                    while ((line = input.readLine()) != null && !messageProtocol.endSession()) {
                        String message = messageProtocol.message(line);
                        output.println(message);
                        output.flush();
                    }
                }
                System.out.println("closing connection");
                
            } catch (IOException ex) {
                System.err.println(ex);
            }
            this.server.closeConn(this.value);
        }
    }
}
