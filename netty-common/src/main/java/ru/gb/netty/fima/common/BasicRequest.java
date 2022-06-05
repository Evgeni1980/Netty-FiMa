package ru.gb.netty.fima.common;

import java.io.Serializable;

public interface BasicRequest extends Serializable {

    String getType();
}
