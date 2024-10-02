package com.client;

import com.util.ResultError;

public class SimpleClientConfig {
    private final String address;
    private final int port;

    public SimpleClientConfig(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public static SimpleClientConfigBuilder New(String address, int port) {
        return new SimpleClientConfigBuilder(address, port);
    }

    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return this.address;
    }

    public static class SimpleClientConfigBuilder {
        private final int port;
        private final String address;

        public SimpleClientConfigBuilder(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public ResultError<SimpleClientConfig> Build() {
            return ResultError.full(new SimpleClientConfig(this.address, this.port));
        }
    }
}
