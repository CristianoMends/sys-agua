package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class ProdutosController{

    private final ProdutoService produtoService;

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

    private ObservableList<Produto> produtosObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 10;
    private int totalPaginas;

    public ProdutosController() {
        this.produtoService = new ProdutoService();
        this.produtosObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProdutos();
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
        // Configuração das colunas da tabela
        TableColumn<Produto, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produto, String> colunaCategoria = new TableColumn<>("Categoria");
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Produto, String> colunaMarca = new TableColumn<>("Marca");
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Produto, String> colunaUnidade = new TableColumn<>("Unidade");
        colunaUnidade.setCellValueFactory(new PropertyValueFactory<>("unit"));

        TableColumn<Produto, Double> colunaPreco = new TableColumn<>("Preço");
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("price"));

        tabelaProdutos.getColumns().addAll(colunaNome, colunaCategoria, colunaMarca ,colunaUnidade,colunaPreco);


        tabelaProdutos.setItems(produtosObservable);
    }

    private void carregarProdutos() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Produto> todosProdutos = produtoService.buscarProdutos(token);

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosProdutos.size() / itensPorPagina);

            // Filtra os produtos para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosProdutos.size());
            List<Produto> produtosPagina = todosProdutos.subList(inicio, fim);

            // Atualiza a lista observável
            produtosObservable.setAll(produtosPagina);

            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void atualizarPaginacao() {
        paginationContainer.getChildren().clear();
        paginationContainer.getChildren().add(btnAnterior);

        for (int i = 0; i < totalPaginas; i++) {
            int indice = i;
            Button botaoPagina = new Button(String.valueOf(i + 1));
            botaoPagina.setOnAction(event -> irParaPagina(indice));
            paginationContainer.getChildren().add(botaoPagina);
        }

        paginationContainer.getChildren().add(btnProximo);
    }

    private void atualizarEstadoBotoes() {
        btnAnterior.setDisable(paginaAtual == 0);
        btnProximo.setDisable(paginaAtual >= totalPaginas - 1);
    }

    private void irParaPagina(int pagina) {
        paginaAtual = pagina;
        carregarProdutos();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarProdutos();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarProdutos();
        }
    }



}
