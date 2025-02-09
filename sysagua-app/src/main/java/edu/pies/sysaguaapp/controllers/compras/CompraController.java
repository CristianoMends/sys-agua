package edu.pies.sysaguaapp.controllers.compras;

import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.services.CompraService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CompraController {

    private final CompraService compraService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TreeTableView<Compra> tabelaCompra;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo;

    @FXML
    private Label successMessage;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private ObservableList<Compra> compraObservable;

    public CompraController() {
        this.compraService = new CompraService();
        this.compraObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarCompras();
        showMenuContext();
    }



    /*------------------- fornecedor --------------*/

    @FXML
    private void handleAddCompra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Compras/AddCompra.fxml"));
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

        MenuItem cancelarCompra = new MenuItem("Cancelar compra");

        contextMenu.getItems().addAll(cancelarCompra);


        cancelarCompra.setOnAction(event -> handleCancelarCompra());

        tabelaCompra.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaCompra.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaCompra, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleCancelarCompra() {
        Compra compraSelecionada = tabelaCompra.getSelectionModel().getSelectedItem().getValue();
        if (compraSelecionada != null) {
            System.out.println("inativar compra");
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {

        TreeTableColumn<Compra, String> colunaData = new TreeTableColumn<>("Data");
        colunaData.setCellValueFactory(param -> {
            String createdAt = param.getValue().getValue().getCreatedAt();
            if (createdAt != null && !createdAt.isEmpty()) {
                return configurarData(createdAt);
            }
            return new SimpleStringProperty("");
        });


        TreeTableColumn<Compra, String> colunaFornecedor = new TreeTableColumn<>("Fornecedor");
        colunaFornecedor.setCellValueFactory(param -> {
            Compra compra = param.getValue().getValue();

            if (compra == null || compra.getSupplier() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(compra.getSupplier().getSocialReason());
        });


        TreeTableColumn<Compra, BigDecimal> colunaValorTotal = new TreeTableColumn<>("Valor Total");
        colunaValorTotal.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getValue().getTotalValue()));

        TreeTableColumn<Compra, String> colunaStatus = new TreeTableColumn<>("Status");
        colunaStatus.setCellValueFactory(param -> {
            Compra compra = param.getValue().getValue();

            if (compra == null || compra.getSupplier() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(compra.getActive() ? "Ativo" : "Inativo");

        });

        // Adiciona as colunas na TreeTableView
        tabelaCompra.getColumns().clear();
        tabelaCompra.getColumns().addAll(colunaData, colunaFornecedor, colunaValorTotal, colunaStatus);


        tabelaCompra.setRowFactory(tv -> {
            TreeTableRow<Compra> row = new TreeTableRow<Compra>() {
                @Override
                protected void updateItem(Compra item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item != null && getTreeItem() != null) {

                        if (getTreeItem().getParent() == null) {

                            setStyle("-fx-background-color: red");
                            getStyleClass().add("agrupamento");
                        } else {
                            setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 0 0 1px 0;");
                        }
                    } else {
                        // Caso a linha esteja vazia
                        setStyle("-fx-background-color: transparent;");
                    }
                }
            };
            return row;
        });

        // Define o root inicial (vazio)
        tabelaCompra.setRoot(new TreeItem<>(new Compra()));
        tabelaCompra.setShowRoot(false);
    }


    private void carregarCompras() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Compra> listaCompras = compraService.buscarCompras(token);

//            if (!exibirInativosCheckBox.isSelected()) {
//                listaCompras.removeIf(compra -> !compra.getActive());
//            }

            // Criar um mapa de agrupamento por data
            Map<String, TreeItem<Compra>> gruposPorData = new LinkedHashMap<>();

            for (Compra compra : listaCompras) {
                String data = compra.getCreatedAt();

                // Se ainda não houver um grupo para essa data, criamos
                gruposPorData.putIfAbsent(data, new TreeItem<>(new Compra(data)));

                // Adicionamos a compra como filha desse grupo
                gruposPorData.get(data).getChildren().add(new TreeItem<>(compra));
            }

            // Define a raiz da tabela com os grupos de data
            TreeItem<Compra> root = new TreeItem<>(new Compra());
            root.getChildren().addAll(gruposPorData.values());

            tabelaCompra.setRoot(root);
            tabelaCompra.setShowRoot(false); // Oculta a raiz principal

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
        }
    }

    /*---------------- formatações -------------------*/
    private SimpleStringProperty configurarData(String data) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate date = LocalDate.parse(data, inputFormatter);

        return new SimpleStringProperty(date.format(outputFormatter));
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
