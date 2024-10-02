package com.server;

import com.server.responses.IProtocol;
import com.util.ResultError;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SimpleServerConfig {
    private final InetAddress address;
    private final int port;
    private final int threadLimit;
    private final IProtocol messageProtocol;

    public SimpleServerConfig(InetAddress address, int port, int threadLimit, IProtocol col) {
        this.address = address;
        this.port = port;
        this.threadLimit = threadLimit;
        this.messageProtocol = col;
    }

    public static SimpleServerConfigBuilder New(int strPort, IProtocol col) {
        return new SimpleServerConfigBuilder(strPort, col);
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public int getThreadLimit() {
        return this.threadLimit;
    }

    public IProtocol getMessageProtocol() {
        return this.messageProtocol;
    }

    public static class SimpleServerConfigBuilder {
        private final int port;
        private final IProtocol messageProtocol;
        private InetAddress addr = null;
        private int threadLimit = 10;

        public SimpleServerConfigBuilder(int port, IProtocol col) {
            this.port = port;
            this.messageProtocol = col;
        }

        public SimpleServerConfigBuilder Address(InetAddress addr) {
            this.addr = addr;
            return this;
        }

        public SimpleServerConfigBuilder ThreadLimit(Integer count) {
            this.threadLimit = count;
            return this;
        }

        public ResultError<SimpleServerConfig> Build() {
            if (this.port < 1 && this.port > 65535) {
                return ResultError.empty("port must within 1 and 65535");
            }

            if (this.addr == null) {
                try {
                    this.addr = InetAddress.getByName("127.0.0.0");
                } catch (UnknownHostException ex) {
                    return ResultError.empty("default address failed");
                }
            }

            if (this.messageProtocol == null) {
                ResultError.empty("must be a valid IProtocol");
            }

            return ResultError.full(new SimpleServerConfig(this.addr, this.port, this.threadLimit, this.messageProtocol));
        }
    }
}
