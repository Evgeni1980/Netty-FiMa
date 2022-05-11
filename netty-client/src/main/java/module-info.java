module ru.gb.netty.fima.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.gb.netty.fima.client to javafx.fxml;
    exports ru.gb.netty.fima.client;
}