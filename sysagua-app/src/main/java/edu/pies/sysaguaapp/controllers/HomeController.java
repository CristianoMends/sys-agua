package edu.pies.sysaguaapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class HomeController {
    public Pane spacer;
    @FXML
    private StackPane contentArea;

    @FXML
    private void handleDashboard() {
        showMessage("Dashboard");
    }

    @FXML
    private void handleCadastrosGerais() {
        showMessage("Cadastros Gerais");
    }

    @FXML
    private void handleEntregas() {
        showMessage("Entregas");
    }

    @FXML
    private void handleCompras() {
        showMessage("Compras");
    }

    @FXML
    private void handleEstoque() {
        showMessage("Estoque");
    }

    @FXML
    private void handleCaixa() {
        showMessage("Caixa");
    }

    @FXML
    private void handleRelatorio() {
        showMessage("Relatório");
    }

    @FXML
    private void handleUsuario() {
        showMessage("Usuário");
    }

    @FXML
    private void handleSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente sair?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.exit(0);
            }
        });
    }

    private void showMessage(String message) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new javafx.scene.control.Label(message));
    }
}
