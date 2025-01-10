package edu.pies.sysaguaapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {@Override
public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
    BorderPane root = loader.load();
    Scene scene = new Scene(root, 800, 600);

//    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/loginstyle.css")).toExternalForm());

    primaryStage.setTitle("SysAgua - Controle de Pedidos");
    primaryStage.setScene(scene);
    primaryStage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}