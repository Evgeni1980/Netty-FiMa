package ru.gb.netty.fima.common;

public class DisconnectRequest implements BasicRequest {

    @Override
    public String getType() {
        return "log_off";
    }
}
