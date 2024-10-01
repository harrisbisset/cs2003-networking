package com;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SimpleUtil {
    public static InetAddress getInetAddress(String addr) {
        try {
            return InetAddress.getByName(addr);
        } catch (UnknownHostException ex) {
            System.out.println("address is invalid");
            System.exit(2);
        }
        return null;
    }

    public static int getPort(String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            System.out.println("port is not a valid integer");
            System.exit(2);
        }
        return 10;
    }
}
