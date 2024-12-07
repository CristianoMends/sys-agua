module com.app.sysagua {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.app.sysagua to javafx.fxml;
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    exports com.app.sysagua;
    exports com.app.sysagua.controller;
    exports com.app.sysagua.dto;
    opens com.app.sysagua.controller to javafx.fxml;
}