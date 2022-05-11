package ru.gb.netty.fima.autReg;

public class DisconnectRequest implements BasicRequest {

    @Override
    public String getType() {
        return "log_off";
    }
}
