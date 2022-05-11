module ru.gb.netty.fima.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires javax.inject;
    requires java.sql;


    opens ru.gb.netty.fima.client to javafx.fxml;
    exports ru.gb.netty.fima.client;
}