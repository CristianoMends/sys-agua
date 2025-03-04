module edu.pies.sysaguaapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires java.xml.crypto;
    requires static lombok;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires jdk.unsupported.desktop;
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
    exports edu.pies.sysaguaapp.models.compras;
    exports edu.pies.sysaguaapp.controllers.compras;
    opens edu.pies.sysaguaapp.controllers.compras to javafx.fxml;
    opens edu.pies.sysaguaapp.dtos.compra to com.fasterxml.jackson.databind;
    opens edu.pies.sysaguaapp.models.compras;
    exports edu.pies.sysaguaapp.enumeration;
    opens edu.pies.sysaguaapp.controllers.Clientes to javafx.fxml;
    exports edu.pies.sysaguaapp.controllers.Pedidos to com.fasterxml.jackson.databind;
    opens edu.pies.sysaguaapp.controllers.Pedidos to javafx.fxml;

    exports edu.pies.sysaguaapp.models.Pedido to com.fasterxml.jackson.databind;
    opens edu.pies.sysaguaapp.models.Pedido to com.fasterxml.jackson.databind;
    exports edu.pies.sysaguaapp.enumeration.Pedidos to com.fasterxml.jackson.databind;
    opens edu.pies.sysaguaapp.enumeration.Pedidos to com.fasterxml.jackson.databind;
    exports edu.pies.sysaguaapp.dtos.pedido to com.fasterxml.jackson.databind;

    exports edu.pies.sysaguaapp.controllers.Usuarios to javafx.fxml;
    opens edu.pies.sysaguaapp.controllers.Usuarios to javafx.fxml;

    exports edu.pies.sysaguaapp.enumeration.Usuarios to com.fasterxml.jackson.databind;

    exports edu.pies.sysaguaapp.models.produto;
}