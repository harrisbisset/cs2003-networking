package com.server.responses;

public interface IProtocol {
    public IProtocol generic();
    public String message(String msg);
    public boolean endSession();
}
