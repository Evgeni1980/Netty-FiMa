package ru.gb.netty.fima.common.dto;

public class DisconnectRequest implements BasicRequest {

    @Override
    public String getType() {
        return "log_off";
    }
}
