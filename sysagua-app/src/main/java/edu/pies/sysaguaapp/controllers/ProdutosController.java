package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.math.BigDecimal;
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
    private TextField categoriaField;

    @FXML
    private TextField custoField;

    @FXML
    private TextField precoUnitarioField;

    @FXML
    private TextField marcaField;

    @FXML
    private TextField unidadeField;

    @FXML
    private TextField ncmFiel;

    @FXML
    private TextField descricaoField;

    @FXML
    private TextField cestField;

    @FXML
    private TextField gtinField;

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

    @FXML
    private Label successMessage;

    @FXML
    private Label nomeErrorLabel;

    @FXML
    private Label categoriaErrorLabel;

    private ObservableList<Produto> produtosObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public ProdutosController() {
        this.produtoService = new ProdutoService();
        this.produtosObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProdutos();

        // Validar números
        configurarValidacaoNumerica(custoField);
        configurarValidacaoNumerica(precoUnitarioField);
        configurarValidacaoNumerica(cestField);
        configurarValidacaoNumerica(gtinField);

        // Validar texto
        configurarValidacaoTexto(nomeField);
        nomeField.textProperty().addListener((obs, oldText, newText) -> {
            nomeErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(categoriaField);
        categoriaField.textProperty().addListener((obs, oldText, newText) -> {
            categoriaErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(marcaField);
        configurarValidacaoTexto(unidadeField);
        configurarValidacaoTexto(descricaoField);
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
        String categoria = categoriaField.getText();
        String unidade = unidadeField.getText();
        BigDecimal precoUnitario = new BigDecimal(precoUnitarioField.getText());
        BigDecimal custo = new BigDecimal(custoField.getText());
        String marca = marcaField.getText();

        //Fazer o tratamento correto de texto e numeros

        Produto novoProduto = new Produto();
        novoProduto.setName(nome);
        novoProduto.setCategory(categoria);
        novoProduto.setUnit(unidade);
        novoProduto.setPrice(precoUnitario);
        novoProduto.setBrand(marca);
        novoProduto.setCost(custo);

        try {
            String token = TokenManager.getInstance().getToken();
//            Produto produtoCriado = produtoService.criarProduto(novoProduto, token);
//            produtosObservable.add(produtoCriado);
            produtoService.criarProduto(novoProduto, token);


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao criar produto");
            System.out.println(e.getMessage());
            alert.setContentText(e.getMessage());
            hideForm();
            hideOverlay();
            alert.showAndWait();
        }

        hideForm();
        hideOverlay();
        clearFieldForm();
        listProductView.setDisable(false);
        showSucessMessage();
        carregarProdutos();
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

        TableColumn<Produto, Double> colunaCusto = new TableColumn<>("Custo");
        colunaCusto.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tabelaProdutos.getColumns().addAll(colunaNome, colunaCategoria, colunaMarca ,colunaUnidade,colunaPreco, colunaCusto);


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

    private void clearFieldForm() {
        nomeField.clear();
        categoriaField.clear();
        unidadeField.clear();
        precoUnitarioField.clear();
        custoField.clear();
        marcaField.clear();
    }


    /*--------- validações ----------*/

    private void configurarValidacaoNumerica(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getText().matches("\\d*([.]\\d*)?")) { // Permite números e ponto decimal
                return change;
            }
            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void configurarValidacaoTexto(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s\\d.,-]*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
            }
        });
    }

    /*------------------------ mensagens ---------------*/

    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }







}
