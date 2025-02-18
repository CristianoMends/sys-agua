package edu.pies.sysaguaapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private StackPane rootPane; //toda a pilha da tela de home

    @FXML
    private VBox sideMenu; // VBox principal do menu lateral.

    @FXML
    private HBox contentBody; //Hbox do menu do meio

    public Pane spacer; //espaçador do botão de sair

    @FXML
    private Button btnCompras, btnEstoque;

    @FXML
    private TitledPane btnCadastrosGerais;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadInitView();
        btnCompras.setCursor(Cursor.HAND);
        btnEstoque.setCursor(Cursor.HAND);
        btnCadastrosGerais.setCursor(Cursor.HAND);
    }

    private void loadInitView() {
        loadView("/views/Dashboard.fxml");
    }

    //---------------- menu superior -------------------//
    @FXML
    private void handlePesquisar() {
//        String searchText = searchField.getText();
        // Lógica de pesquisa (por enquanto só imprime)
        showMessage("Pesquisar");
    }


    @FXML
    private void handleUsuario() {
        // Lógica para abrir configurações
        showMessage("Abrir Configurações");
    }

    @FXML
    private void handleNotificacoes() {
        // Lógica para abrir notificações
        showMessage("Abrir Notificações");
    }

    //------------------menu lateral ---------------------//

    @FXML
    private void handleDashboard() {
        loadView("/views/Dashboard.fxml");
    }


    @FXML
    private void handleEntregas() {
        loadView("/views/Pedidos/Pedido.fxml");
    }

    @FXML
    private void handleCompras() {
        loadView("/views/Compras/Compras.fxml");
    }

    @FXML
    private void handleEstoque() {
        loadView("/views/Estoque/Estoque.fxml");
    }

    @FXML
    private void handleFinanceiro() {
        showMessage("Caixa");
    }

    @FXML
    private void handleRelatorio() {
        showMessage("Relatório");
    }

    @FXML
    private void handleAjuda() {
        loadView("/views/Ajuda.fxml");
    }

    @FXML
    private void handleCadastrosGerais() {
        // Removed submenu handling logic
    }

    //------------------submenu cadastros gerais ---------------------//
    @FXML
    private void handleLoadClientesView() {
        loadView("/views/Clientes/Clientes.fxml");
    }

    @FXML
    private void handleProdutos() {
        loadView("/views/Produtos/Produtos.fxml");
    }

    @FXML
    private void handleEntregador() {
        loadView("/views/Entregador/Entregador.fxml");
    }

    @FXML
    private void handleFornecedor() {
        loadView("/views/Fornecedor/Fornecedor.fxml");
    }

    @FXML
    private void handleSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente sair?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    // Fecha a janela atual (Home)
                    Stage currentStage = (Stage) rootPane.getScene().getWindow();
                    currentStage.close();

                    // Carrega a tela de login
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
                    Parent root = loader.load();

                    // Cria uma nova cena e exibe a nova janela
                    Stage loginStage = new Stage();
                    loginStage.setScene(new Scene(root, 800, 600));
                    loginStage.setTitle("SysAgua - Entrar");
                    loginStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //----------------------------contentBody--------//
    @FXML
    private void handleContent() {
        showMessage("Algum conteúdo aqui");
    }

    private void showMessage(String message) {
        // Cria um novo label com a mensagem
        javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);
        contentBody.getChildren().clear();
        contentBody.getChildren().add(messageLabel);
    }


    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            HBox.setHgrow(view, Priority.ALWAYS); // Configurar crescimento horizontal
            contentBody.getChildren().clear(); // Limpar conteúdos existentes
            contentBody.getChildren().add(view); // Adicionar nova view
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
