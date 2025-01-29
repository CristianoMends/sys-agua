package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.ProductCategory;
import edu.pies.sysaguaapp.models.ProductLine;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.services.ProductCategoryService;
import edu.pies.sysaguaapp.services.ProductLineService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.RoundingMode;

public class ProdutosController{

    private final ProdutoService produtoService;
    private final ProductCategoryService productCategoryService;
    private final ProductLineService productLineService;

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
    private ComboBox<String> categoriaComboBox;

    @FXML
    private TextField custoField;

    @FXML
    private TextField precoUnitarioField;

    @FXML
    private TextField marcaField;

    @FXML
    private TextField unidadeField;

    @FXML
    private TextField lineField;

    @FXML
    private TextField ncmField;

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

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<String> linhaComboBox;

    @FXML
    private Label custoErrorLabel;

    @FXML
    private Label precoUnitarioErrorLabel;

    @FXML
    private Label marcaErrorLabel;

    @FXML
    private Label linhaErrorLabel;

    @FXML
    private Label unidadeErrorLabel;

    @FXML
    private Label ncmErrorLabel;

    @FXML
    private Label descricaoErrorLabel;

    @FXML
    private Label cestErrorLabel;

    @FXML
    private Label gtinErrorLabel;

    private ObservableList<Produto> produtosObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;
    private Produto produtoEditando = null;
    private List<ProductCategory> categorias;
    private List<ProductLine> linhas;


    public ProdutosController() {
        this.produtoService = new ProdutoService();
        this.productCategoryService = new ProductCategoryService();
        this.productLineService = new ProductLineService();
        this.produtosObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProdutos();
        carregarCategorias();
        carregarLinhas();
        showMenuContext();
        validarCampos();
    }


    /*---------------------- modal ---------*/


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

    @FXML
    private void updateButtonText() {
        if (produtoEditando != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }

    private Produto criarProduto() {
        Produto novoProduto = new Produto();
        novoProduto.setName(nomeField.getText());
        ProductCategory categoriaSelecionada = categorias.stream()
            .filter(c -> c.getName().equals(categoriaComboBox.getValue()))
            .findFirst()
            .orElse(null);
        ProductLine linhaSelecionada = linhas.stream()
            .filter(l -> l.getName().equals(linhaComboBox.getValue()))
            .findFirst()
            .orElse(null);
        novoProduto.setCategoryId(categoriaSelecionada != null ? categoriaSelecionada.getId() : null);
        novoProduto.setLineId(linhaSelecionada != null ? linhaSelecionada.getId() : null);
        novoProduto.setCost(new BigDecimal(custoField.getText().replace(",", ".")));
        novoProduto.setPrice(new BigDecimal(precoUnitarioField.getText().replace(",", ".")));
        novoProduto.setUnit(unidadeField.getText());
        novoProduto.setBrand(marcaField.getText());
        novoProduto.setNcm(ncmField.getText()); // Corrected from ncmFiel to ncmField
        return novoProduto;
    }

    @FXML
    private void handleSalvar() {
        if (validarFormulario()) {
            Produto novoProduto = criarProduto();

            try {
                String token = TokenManager.getInstance().getToken();
                if (produtoEditando != null) {
                    novoProduto.setId(produtoEditando.getId());
                    novoProduto.setActive(true);
                    produtoService.editarProduto(novoProduto, token);
                    produtosObservable.set(produtosObservable.indexOf(produtoEditando), novoProduto);
                } else {
                    produtoService.criarProduto(novoProduto, token);
                    produtosObservable.add(novoProduto);
                }
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
    }

    private boolean validarFormulario() {
        boolean isValid = true;

        if (nomeField.getText().trim().isEmpty()) {
            nomeErrorLabel.setVisible(true);
            nomeErrorLabel.setManaged(true);
            isValid = false;
        }

        if (categoriaComboBox.getValue() == null || categoriaComboBox.getValue().trim().isEmpty()) {
            categoriaErrorLabel.setVisible(true);
            categoriaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (custoField.getText().trim().isEmpty()) {
            custoErrorLabel.setVisible(true);
            custoErrorLabel.setManaged(true);
            isValid = false;
        }

        if (precoUnitarioField.getText().trim().isEmpty()) {
            precoUnitarioErrorLabel.setVisible(true);
            precoUnitarioErrorLabel.setManaged(true);
            isValid = false;
        }

        if (marcaField.getText().trim().isEmpty()) {
            marcaErrorLabel.setVisible(true);
            marcaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (linhaComboBox.getValue() == null || linhaComboBox.getValue().trim().isEmpty()) {
            linhaErrorLabel.setVisible(true);
            linhaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (unidadeField.getText().trim().isEmpty()) {
            unidadeErrorLabel.setVisible(true);
            unidadeErrorLabel.setManaged(true);
            isValid = false;
        }

        if (ncmField.getText().trim().isEmpty()) {
            ncmErrorLabel.setVisible(true);
            ncmErrorLabel.setManaged(true);
            isValid = false;
        }

        if (descricaoField.getText().trim().isEmpty()) {
            descricaoErrorLabel.setVisible(true);
            descricaoErrorLabel.setManaged(true);
            isValid = false;
        }

        if (cestField.getText().trim().isEmpty()) {
            cestErrorLabel.setVisible(true);
            cestErrorLabel.setManaged(true);
            isValid = false;
        }

        if (gtinField.getText().trim().isEmpty()) {
            gtinErrorLabel.setVisible(true);
            gtinErrorLabel.setManaged(true);
            isValid = false;
        }

        return isValid;
    }

    @FXML
    private void handleCancelar() {
       hideForm();
       hideOverlay();
        listProductView.setDisable(false);
        clearFieldForm();
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

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarItem = new MenuItem("Editar Produto");
        MenuItem clonarItem = new MenuItem("Clonar Produto");
        MenuItem inativarItem = new MenuItem("Inativar Produto");

        // Adiciona as opções ao menu
        contextMenu.getItems().addAll(editarItem, clonarItem, inativarItem);

        // Ação para Editar Produto
        editarItem.setOnAction(event -> handleEditarProduto());

        // Ação para Clonar Produto
        clonarItem.setOnAction(event -> handleClonarProduto());

        // Ação para Inativar Produto
        inativarItem.setOnAction(event -> handleInativarProduto());

        tabelaProdutos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaProdutos.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaProdutos, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleEditarProduto() {
        Produto produtoSelecionado = (Produto) tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null) {
            try {
                preencherCampos(produtoSelecionado);
                produtoEditando = produtoSelecionado;
                updateButtonText();
                showOverlay();
                listProductView.setDisable(true);
                showForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void preencherCampos(Produto produto) {
        nomeField.setText(produto.getName());
        categoriaComboBox.setValue(produto.getCategory() != null ? produto.getCategory().getName() : null);
        custoField.setText(produto.getCost() != null ? produto.getCost().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        precoUnitarioField.setText(produto.getPrice() != null ? produto.getPrice().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        marcaField.setText(produto.getBrand());
        linhaComboBox.setValue(produto.getLine() != null ? produto.getLine().getName() : null);
        unidadeField.setText(produto.getUnit());
    }

    private void handleClonarProduto() {
        Produto produtoSelecionado = (Produto) tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                preencherCampos(produtoSelecionado);
                produtoEditando = null;
                updateButtonText();
                showOverlay();
                listProductView.setDisable(true);
                showForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleInativarProduto() {
        Object produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            System.out.println("Inativar Produto: " + produtoSelecionado);
        }
    }

    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Produto, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produto, String> colunaCategoria = new TableColumn<>("Categoria");
        colunaCategoria.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue();
            ProductCategory categoria = categorias.stream()
                .filter(c -> c.getId().equals(produto.getCategoryId()))
                .findFirst()
                .orElse(null);
            return new SimpleStringProperty(categoria != null ? categoria.getName() : "N/A");
        });

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

    private void carregarCategorias() {
        try {
            categorias = productCategoryService.buscarCategorias();
            categoriaComboBox.setItems(FXCollections.observableArrayList(
                categorias.stream().map(ProductCategory::getName).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarLinhas() {
        try {
            linhas = productLineService.buscarLinhas();
            linhaComboBox.setItems(FXCollections.observableArrayList(
                linhas.stream().map(ProductLine::getName).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar linhas: " + e.getMessage());
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
        categoriaComboBox.setValue(null);
        unidadeField.clear();
        precoUnitarioField.clear();
        custoField.clear();
        marcaField.clear();
        linhaComboBox.setValue(null);
    }


    /*--------- validações ----------*/

    private void validarCampos() {
        // Validar números
        configurarValidacaoNumerica(custoField, custoErrorLabel);
        configurarValidacaoNumerica(precoUnitarioField, precoUnitarioErrorLabel);
        configurarValidacaoNumerica(cestField, cestErrorLabel);
        configurarValidacaoNumerica(gtinField, gtinErrorLabel);

        // Validar texto
        configurarValidacaoTexto(nomeField, nomeErrorLabel);
        nomeField.textProperty().addListener((obs, oldText, newText) -> {
            nomeErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(categoriaComboBox.getEditor(), categoriaErrorLabel);
        categoriaComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            categoriaErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(marcaField, marcaErrorLabel);
        configurarValidacaoTexto(unidadeField, unidadeErrorLabel);
        configurarValidacaoTexto(descricaoField, descricaoErrorLabel);
        configurarValidacaoTexto(ncmField, ncmErrorLabel);
        configurarValidacaoTexto(linhaComboBox.getEditor(), linhaErrorLabel);
    }

    private void configurarValidacaoNumerica(TextField textField, Label errorLabel) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*([,]\\d{0,4})?")) { // Permite números e até 4 casas decimais
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
                return change;
            }
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void configurarValidacaoTexto(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s\\d.,-]*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
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
