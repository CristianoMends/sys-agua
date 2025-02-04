package edu.pies.sysaguaapp.controllers;
import edu.pies.sysaguaapp.services.AuthService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    private final AuthService authService;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Label errorLabel; //exibir mensagens de erro

    public AuthController() {
        this.authService = new AuthService();
    }

    public void initialize(URL location, ResourceBundle resources) {
        //testes
        emailField.setText("dev@gmail.com");
        senhaField.setText("dev123");
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {


        String email = emailField.getText().trim();
        String senha = senhaField.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            errorLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        try {
            String token = authService.autenticar(email, senha);

            // Salva o token
            TokenManager.getInstance().setToken(token);

            // Carrega a tela Home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Home.fxml"));
            StackPane homeView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homeView);

            stage.setScene(scene);
            stage.setTitle("SysAgua - Início");
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Email ou senha inválidos.");
        }
    }

}
