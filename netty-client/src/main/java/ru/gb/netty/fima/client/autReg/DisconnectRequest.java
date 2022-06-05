package ru.gb.netty.fima.client.autReg;

public class DisconnectRequest implements BasicRequest {

    @Override
    public String getType() {
        return "log_off";
    }
}
