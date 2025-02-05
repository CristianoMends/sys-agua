module edu.pies.sysaguaapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires java.xml.crypto;
    requires com.fasterxml.jackson.databind;
    requires static lombok;

    opens edu.pies.sysaguaapp to javafx.fxml;
    exports edu.pies.sysaguaapp;
    exports edu.pies.sysaguaapp.controllers;
    opens edu.pies.sysaguaapp.controllers to javafx.fxml;
    exports edu.pies.sysaguaapp.models;
    exports edu.pies.sysaguaapp.controllers.estoque;
    opens edu.pies.sysaguaapp.controllers.estoque to javafx.fxml;
    exports edu.pies.sysaguaapp.controllers.fornecedor;
    opens edu.pies.sysaguaapp.controllers.fornecedor to javafx.fxml;
    exports edu.pies.sysaguaapp.controllers.produto;
    opens edu.pies.sysaguaapp.controllers.produto to javafx.fxml;
}