package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class ProdutosController{

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Produto> tabelaProdutos;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnProximo;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private BorderPane formCadastroProduto;

    @FXML
    private VBox detailsFormProduto;

    @FXML
    private VBox detailsFormFiscal;

    @FXML
    private Rectangle overlay;

    @FXML
    private VBox listProductView;

    private int paginaAtual = 0;

    private final int itensPorPagina = 10;

    private int totalPaginas;

    @FXML
    public void initialize() {
        configurarTabela();
        atualizarPaginacao();
    }

    private void showOverlay() {
        overlay.maxWidth(rootPane.widthProperty().get());
        overlay.maxHeight(rootPane.heightProperty().get());
        overlay.widthProperty().bind(rootPane.widthProperty());
        overlay.heightProperty().bind(rootPane.heightProperty());
        overlay.setVisible(true);
        overlay.setManaged(true);
    }

    private void hideOverlay() {
        overlay.setVisible(false);
        overlay.setManaged(false);
    }

    /*---------------------- modal ---------*/

    @FXML
    private void handleSalvar() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        hideForm();
        hideOverlay();
        listProductView.setDisable(false);
    }

    @FXML
    private void handleCancelar() {
       hideForm();
       hideOverlay();
        listProductView.setDisable(false);
    }

    @FXML
    private void handleDetailsProdutoModal() {
        if (!detailsFormProduto.isVisible()) {
            showModal(detailsFormProduto, detailsFormFiscal);
        }
    }

    @FXML
    private void handleDetailsFiscalModal() {
        if (!detailsFormFiscal.isVisible()) {
            showModal(detailsFormFiscal, detailsFormProduto);
        }
    }

    private void showModal(VBox toShow, VBox toHide) {
        // Oculta o modal anterior
        hideDetailsModal(toHide);

        // Exibe o modal atual
        toShow.setManaged(true);
        toShow.toFront();
        toShow.setVisible(true);
    }

    @FXML
    private void hideDetailsModal(VBox root) {
        root.setManaged(false);
        root.setVisible(false);
    }

    /*------------------- produtos --------------*/

    @FXML
    private void handleAddProduto() {
        if (!formCadastroProduto.isVisible()) {
            showOverlay();
            showForm();
            listProductView.setDisable(true);

        }
    }

    private void showForm() {
        formCadastroProduto.setManaged(true);
        formCadastroProduto.toFront();
        formCadastroProduto.setVisible(true);
    }

    private void hideForm() {
        formCadastroProduto.setVisible(false);
        formCadastroProduto.setManaged(false);
    }

    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configure os itens da tabela (exemplo)
        // List<?> produtos = obterDados(); // Implemente a obtenção de dados
        // tabelaProdutos.getItems().setAll(produtos.subList(0, Math.min(itensPorPagina, produtos.size())));

        totalPaginas = calcularTotalPaginas(); // Substitua pela lógica correta para obter o total de páginas
    }

    private int calcularTotalPaginas() {
        int totalItens = obterTotalDeItens(); // Substitua pela lógica de obtenção do total
        return (int) Math.ceil((double) totalItens / itensPorPagina);
    }

    private int obterTotalDeItens() {
        // Substitua pela lógica correta para obter o número total de itens
        return 100; // Exemplo
    }

    private void atualizarPaginacao() {
        paginationContainer.getChildren().clear();
        paginationContainer.getChildren().add(btnAnterior);

        for (int i = 0; i < totalPaginas; i++) {
            int indice = i; // Necessário para capturar o índice corretamente no lambda
            Button botaoPagina = new Button(String.valueOf(i + 1));
            botaoPagina.setOnAction(event -> irParaPagina(indice));
            paginationContainer.getChildren().add(botaoPagina);
        }

        paginationContainer.getChildren().add(btnProximo);
        atualizarEstadoBotoes();
    }

    private void atualizarEstadoBotoes() {
        btnAnterior.setDisable(paginaAtual == 0);
        btnProximo.setDisable(paginaAtual == totalPaginas - 1);
    }

    private void irParaPagina(int pagina) {
        paginaAtual = pagina;
        carregarDados();
    }

    private void carregarDados() {
        // Lógica para carregar os itens da tabela com base na página atual
        int inicio = paginaAtual * itensPorPagina;
        int fim = Math.min(inicio + itensPorPagina, obterTotalDeItens());
        // Exemplo: List<?> produtos = obterDados().subList(inicio, fim);
        // tabelaProdutos.getItems().setAll(produtos);
        atualizarEstadoBotoes();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarDados();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarDados();
        }
    }



}
