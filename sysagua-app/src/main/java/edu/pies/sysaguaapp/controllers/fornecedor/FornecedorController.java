package edu.pies.sysaguaapp.controllers.fornecedor;

import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.services.FornecedorService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class FornecedorController {
    private final FornecedorService fornecedorService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Fornecedor> tabelaFornecedor;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar;

    @FXML
    private Label successMessage;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private ObservableList<Fornecedor> fornecedorObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public FornecedorController() {
        this.fornecedorService = new FornecedorService();
        token = TokenManager.getInstance().getToken();
        this.fornecedorObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarFornecedores();
        showMenuContext();
        btnAdicionar.setCursor(Cursor.HAND);

        exibirInativosCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> carregarFornecedores());
    }



    /*------------------- fornecedor --------------*/

    @FXML
    private void handleAddFornecedor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Fornecedor/AddFornecedor.fxml"));
            Parent form = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(form);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar formulário de fornecedor: " + e.getMessage());
        }
    }

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarItem = new MenuItem("Editar fornecedor");
        MenuItem inativarItem = new MenuItem("Inativar fornecedor");

        contextMenu.getItems().addAll(editarItem, inativarItem);
        editarItem.setOnAction(event -> handleEditarFornecedor());
        inativarItem.setOnAction(event -> handleInativarFornecedor());

        tabelaFornecedor.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaFornecedor.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaFornecedor, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleEditarFornecedor() {
        Fornecedor fornecedorSelecionado = tabelaFornecedor.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Fornecedor/AddFornecedor.fxml"));
                Parent form = loader.load();

                // Obter o controlador da nova tela
                AddFornecedorController controller = loader.getController();
                // Passar o ID do fornecedor para o controlador
                controller.setFornecedorEditando(String.valueOf(fornecedorSelecionado.getId()));

                rootPane.getChildren().clear();
                rootPane.getChildren().add(form);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao carregar formulário de fornecedor: " + e.getMessage());
            }
        }
    }

    private void handleInativarFornecedor() {
        Fornecedor fornecedorSelecionado = tabelaFornecedor.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado != null) {
            try {
                fornecedorService.inativarFornecedor(fornecedorSelecionado, token);
                tabelaFornecedor.refresh();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao inativar fornecedor: " + e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Fornecedor, String> colunaRazaosocial = new TableColumn<>("Razão social");
        colunaRazaosocial.setCellValueFactory(new PropertyValueFactory<>("socialReason"));

        TableColumn<Fornecedor, Long> colunaCodigo = new TableColumn<>("Código");
        colunaCodigo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colunaCodigo.setStyle("-fx-alignment: CENTER;");
        colunaCodigo.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn<Fornecedor, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colunaTelefone.setStyle("-fx-alignment: CENTER;");

        TableColumn<Fornecedor, String> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colunaCnpj.setStyle("-fx-alignment: CENTER;");

        TableColumn<Fornecedor, String> colunaEndereco = new TableColumn<>("Endereço");
        colunaEndereco.setCellValueFactory(celldata -> {
            String rua = celldata.getValue().getAddress().getStreet();
            String numero = celldata.getValue().getAddress().getNumber();
            String bairro = celldata.getValue().getAddress().getNeighborhood();
            return new SimpleStringProperty(rua + ", " + numero + ", " + bairro);
        });

        TableColumn<Fornecedor, String> columnCidade = new TableColumn<>("Cidade");
        columnCidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        TableColumn<Fornecedor, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(cellData -> {
            if (cellData.getValue().getActive()) {
                return new SimpleStringProperty("Ativo");
            }
            return new SimpleStringProperty("Inativo");
        });
        colunaStatus.setStyle("-fx-alignment: CENTER;");

        tabelaFornecedor.getColumns().addAll(colunaCodigo,colunaRazaosocial, colunaTelefone, colunaCnpj, colunaEndereco, columnCidade, colunaStatus);

        tabelaFornecedor.setItems(fornecedorObservable);

        tabelaFornecedor.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Fornecedor fornecedor, boolean empty) {
                super.updateItem(fornecedor, empty);
                if (fornecedor == null || empty) {
                    setStyle("");
                } else if (!fornecedor.isActive()) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void carregarFornecedores() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Fornecedor> todosFornecedores = fornecedorService.buscarFornecedores(token);

            if (!exibirInativosCheckBox.isSelected()) {
                todosFornecedores.removeIf(fornecedor -> !fornecedor.getActive());
            }

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosFornecedores.size() / itensPorPagina);

            // Filtra os fornecedores para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosFornecedores.size());
            List<Fornecedor> fornecedoresPagina = todosFornecedores.subList(inicio, fim);

            // Atualiza a lista observável
            fornecedorObservable.setAll(fornecedoresPagina);

            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
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
        carregarFornecedores();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarFornecedores();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarFornecedores();
        }
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
