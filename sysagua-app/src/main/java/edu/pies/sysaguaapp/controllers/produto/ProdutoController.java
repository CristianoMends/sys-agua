package edu.pies.sysaguaapp.controllers.produto;

import edu.pies.sysaguaapp.models.ProductCategory;
import edu.pies.sysaguaapp.models.ProductLine;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.produto.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.services.produto.ProductCategoryService;
import edu.pies.sysaguaapp.services.produto.ProductLineService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;

public class ProdutoController {

    private final ProdutoService produtoService;
    private final ProductCategoryService productCategoryService;
    private final ProductLineService productLineService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Produto> tabelaProdutos;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar;

    @FXML
    private Label successMessage;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private ObservableList<Produto> produtosObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;
    private Produto produtoEditando = null;
    private List<ProductCategory> categorias;
    private List<ProductLine> linhas;


    public ProdutoController() {
        this.produtoService = new ProdutoService();
        this.productCategoryService = new ProductCategoryService();
        this.productLineService = new ProductLineService();
        this.produtosObservable = FXCollections.observableArrayList();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProdutos();
        carregarCategorias();
        carregarLinhas();
        showMenuContext();
        configurarFiltroInativos();
        btnAdicionar.setCursor(Cursor.HAND);
    }

    /*------------------- produtos --------------*/

    @FXML
    private void handleAddProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Produtos/AddProdutos.fxml"));
            Parent form = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(form);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar formulário de produtos: " + e.getMessage());
        }
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
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Produtos/AddProdutos.fxml"));
                Parent form = loader.load();

                AddProdutoController controller = loader.getController();
                controller.setProdutoEditando(String.valueOf(produtoSelecionado.getId()));

                rootPane.getChildren().clear();
                rootPane.getChildren().add(form);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao carregar formulário de produto: " + e.getMessage());
            }
        }
    }

    private void handleClonarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Produtos/AddProdutos.fxml"));
                Parent form = loader.load();

                AddProdutoController controller = loader.getController();
                controller.setProdutoClonado(String.valueOf(produtoSelecionado.getId()));

                rootPane.getChildren().clear();
                rootPane.getChildren().add(form);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao carregar formulário de produto: " + e.getMessage());
            }
        }
    }

    private void handleInativarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                produtoService.inativarProduto(produtoSelecionado, token);
                int index = produtosObservable.indexOf(produtoSelecionado);
                if (index != -1) {
                    produtosObservable.set(index, produtoSelecionado);
                }
                tabelaProdutos.refresh();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Falha ao inativar produto");
                alert.setContentText("Produto não pode ser inativado pois compõe estoque. Zere o estoque primeiro.");
                alert.showAndWait();
            }
        }
    }

    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Produto, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produto, Long> colunaCodigo = new TableColumn<>("Código");
        colunaCodigo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colunaCodigo.setStyle("-fx-alignment: CENTER;");
        colunaCodigo.setSortType(TableColumn.SortType.ASCENDING);

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
        colunaUnidade.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, String> colunaPreco = new TableColumn<>("Preço");
        colunaPreco.setCellValueFactory(cellData ->{
            BigDecimal preco = cellData.getValue().getPrice();
            return new SimpleStringProperty(preco != null ? "R$ " + preco.setScale(4, RoundingMode.HALF_UP).toString() : "");
        });
        colunaPreco.setStyle("-fx-alignment: CENTER;");

        TableColumn<Produto, String> colunaCusto = new TableColumn<>("Custo");
        colunaCusto.setCellValueFactory(cellData -> {
            BigDecimal custo = cellData.getValue().getPrice();
            return new SimpleStringProperty(custo != null ? "R$ " + custo.setScale(4, RoundingMode.HALF_UP).toString() : "");
        });
        colunaCusto.setStyle("-fx-alignment: CENTER;");

        tabelaProdutos.getColumns().addAll(colunaCodigo, colunaNome, colunaCategoria, colunaMarca ,colunaUnidade,colunaPreco, colunaCusto);

        tabelaProdutos.getSortOrder().add(colunaCodigo);

        tabelaProdutos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Produto produto, boolean empty) {
                super.updateItem(produto, empty);
                if (produto == null || empty) {
                    setStyle("");
                } else if (!produto.isActive()) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });

        tabelaProdutos.setItems(produtosObservable);
    }

    private void carregarProdutos() {
        try {
            List<Produto> todosProdutos = produtoService.buscarProdutos(token);

            // Filtra os produtos ativos se o checkbox não estiver marcado
            if (!exibirInativosCheckBox.isSelected()) {
                todosProdutos = todosProdutos.stream()
                    .filter(Produto::isActive)
                    .toList();
            }

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarLinhas() {
        try {
            linhas = productLineService.buscarLinhas();
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

    private void configurarFiltroInativos() {
        exibirInativosCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> carregarProdutos());
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
