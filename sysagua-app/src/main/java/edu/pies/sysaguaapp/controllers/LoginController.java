package edu.pies.sysaguaapp.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField senhaField;

    @FXML
    private void initialize() {
        // Dados hardcoded para simular um login
        String email = "josiasmartins@gmail.com";
        String senha = "123";

        // Preenche os TextFields com os dados
        emailField.setText(email);
        senhaField.setText(senha);
    }


    // Método acionado ao clicar no botão "Entrar"
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        try {
            // Carrega o arquivo HomeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomeView.fxml"));
            StackPane homeView = loader.load();
            // Obtém a janela atual (Stage) e troca a cena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homeView, 800, 600);

            stage.setScene(scene);
            stage.setTitle("SysAgua - Home");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar a tela HomeView.fxml");
        }
    }
}
