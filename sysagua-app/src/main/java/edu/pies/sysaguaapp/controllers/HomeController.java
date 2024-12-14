package edu.pies.sysaguaapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {
    public Pane spacer;

    @FXML
    private VBox submenu;

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox sideMenu; // VBox principal do menu lateral.


    //---------------- menu superior -------------------//
    @FXML
    private void handlePesquisar() {
//        String searchText = searchField.getText();
        // Lógica de pesquisa (por enquanto só imprime)
        showMessage("Pesquisar");
    }

    @FXML
    private void handleConfiguracoes() {
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
        showMessage("Dashboard");
    }



//    @FXML
//    private void handleCadastrosGerais() {
//        showMessage("Cadastros Gerais");
//    }

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
    private void handleFinanceiro() {
        showMessage("Caixa");
    }

    @FXML
    private void handleRelatorio() {
        showMessage("Relatório");
    }

    @FXML
    private void handleAjuda() {
        showMessage("Usuário");
    }

    @FXML
    private void handleClientes() {
        try {
            // Carregar a nova tela de clientes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientesView.fxml"));
            AnchorPane clientesView = loader.load();

            // Criar a cena e configurar a janela
            Stage clientesStage = new Stage();
            clientesStage.setTitle("Clientes");
            clientesStage.setScene(new Scene(clientesView));
            clientesStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProdutos() {
        showMessage("Todos os produtos");
    }

    @FXML
    private void handleEntregador() {
        showMessage("Todos os entregadores");
    }

    @FXML
    private void handleFornecedor() {
        showMessage("Todos os fornecedores");
    }

    @FXML
    private void handleCadastrosGerais() {
        if (!submenu.isVisible()) {
            // Exibe o submenu ao lado
            submenu.setLayoutX(sideMenu.getWidth());
            submenu.setLayoutY(50); // Altere conforme necessário
            submenu.setVisible(true);
            submenu.setManaged(true);

            // Adiciona evento para recolher o submenu ao clicar fora
            rootPane.setOnMouseClicked(event -> {
                if (!submenu.contains(event.getX(), event.getY())) {
                    hideSubmenu();
                }
            });
        } else {
            hideSubmenu();
        }
    }

    private void hideSubmenu() {
        submenu.setVisible(false);
        submenu.setManaged(false);
        rootPane.setOnMouseClicked(null); // Remove o evento
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

    //----------------------------content--------//
    @FXML
    private void handleContent() {
        showMessage("Algum conteúdo aqui");
    }

    private void showMessage(String message) {
        // Cria um novo label com a mensagem
        javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);

        // Garantir que o menu lateral (sideMenu) não seja alterado
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(1); // Remove o conteúdo anterior (se houver)
        }

        // Adiciona o conteúdo dinamicamente no StackPane sem sobrepor o menu lateral
        StackPane.setAlignment(messageLabel, javafx.geometry.Pos.CENTER);
        rootPane.getChildren().add(messageLabel);
    }




}
