module edu.pies.sysaguaapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens edu.pies.sysaguaapp to javafx.fxml;
    exports edu.pies.sysaguaapp;
    exports edu.pies.sysaguaapp.controllers;
    opens edu.pies.sysaguaapp.controllers to javafx.fxml;
    exports edu.pies.sysaguaapp.models;
}