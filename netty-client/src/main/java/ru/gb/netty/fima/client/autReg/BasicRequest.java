package ru.gb.netty.fima.client.autReg;

import java.io.Serializable;

public interface BasicRequest extends Serializable {

    String getType();
}
