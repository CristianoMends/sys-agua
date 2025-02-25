package edu.pies.sysaguaapp.controllers.compras;

import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.services.CompraService;
import edu.pies.sysaguaapp.services.FornecedorService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CompraController {

    private final CompraService compraService;
    private final String token;
    private final FornecedorService fornecedorService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TreeTableView<Compra> tabelaCompra;

    @FXML
    private Label successMessage;

    @FXML
    private Button btnAdicionar, btnFiltrar, btnClearFilter;

    @FXML
    private DatePicker datePickerInicio, datePickerFim;

    @FXML
    private ComboBox<Fornecedor> comboFornecedor;

    @FXML
    private ComboBox<PaymentStatus> comboStatusPagamento;

    @FXML
    private CheckBox exibirCompraMes;

    private ObservableList<Compra> compraObservable;

    public CompraController() {
        compraService = new CompraService();
        fornecedorService = new FornecedorService();
        this.compraObservable = FXCollections.observableArrayList();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarCompras();
        showMenuContext();
        btnAdicionar.setCursor(Cursor.HAND);
        btnFiltrar.setCursor(Cursor.HAND);
        btnClearFilter.setCursor(Cursor.HAND);
        popularFiltros();
        exibirCompraMes.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                filtrarPorMesAtual();
            } else {
                carregarCompras();
            }
        });
        exibirCompraMes.setSelected(true);
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
            try {
                compraService.cancelarCompra(compraSelecionada, token);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao cancelar compra " + e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {

        TreeTableColumn<Compra, String> colunaData = new TreeTableColumn<>("Data");
        colunaData.setCellValueFactory(param -> {
            LocalDateTime entryAt = param.getValue().getValue().getEntryAt();
            if (entryAt != null) {
                return new SimpleStringProperty(entryAt.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
                new SimpleObjectProperty<>(param.getValue().getValue().getTotalAmount()));

        TreeTableColumn<Compra, String> colunaStatus = new TreeTableColumn<>("Pagamento");
        colunaStatus.setCellValueFactory(param -> {
            Compra compra = param.getValue().getValue();

            if (compra == null || compra.getSupplier() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(compra.getPaymentStatus().getDescription());
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
                            // Linha de agrupamento (Ignorar no clique)
                            setStyle("-fx-background-color: red");
                            getStyleClass().add("agrupamento");
                        } else {
                            // Linha normal (clicável)
                            setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 0 0 1px 0;");
                        }
                    } else {
                        // Linha vazia
                        setStyle("-fx-background-color: transparent;");
                    }
                }
            };

            // Adiciona evento de duplo clique para abrir os detalhes da compra
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Compra compraSelecionada = row.getItem();

                    if (compraSelecionada != null && row.getTreeItem().getParent() != null) {
                        abrirDetalhesCompra(compraSelecionada);
                    }
                }
            });

            return row;
        });

        // Define o root inicial (vazio)
        tabelaCompra.setRoot(new TreeItem<>(new Compra()));
        tabelaCompra.setShowRoot(false);
    }

    @FXML
    private void handleFiltrar() {
        try {
            List<Compra> listaCompras = compraService.buscarCompras(token);

            LocalDate inicio = datePickerInicio.getValue();
            LocalDate fim = datePickerFim.getValue();
            if (inicio != null) {
                listaCompras = listaCompras.stream()
                        .filter(compra -> compra.getEntryAt() != null && 
                                !compra.getEntryAt().toLocalDate().isBefore(inicio))
                        .collect(Collectors.toList());
            }
            if (fim != null) {
                listaCompras = listaCompras.stream()
                        .filter(compra -> compra.getEntryAt() != null && 
                                !compra.getEntryAt().toLocalDate().isAfter(fim))
                        .collect(Collectors.toList());
            }

            if (comboFornecedor.getValue() != null) {
                Long fornecedorSelecionadoId = comboFornecedor.getValue().getId();
                if (fornecedorSelecionadoId != null) {
                    listaCompras = listaCompras.stream()
                            .filter(compra -> compra.getSupplier().getId() != null &&
                                    fornecedorSelecionadoId.equals(compra.getSupplier().getId()))
                            .collect(Collectors.toList());
                }
            }


            if (comboStatusPagamento.getValue() != null) {
                String statusSelecionado = comboStatusPagamento.getValue().getDescription();
                if (statusSelecionado != null) {
                    listaCompras = listaCompras.stream()
                            .filter(compra -> statusSelecionado.equals(compra.getPaymentStatus().getDescription()))
                            .collect(Collectors.toList());
                }
            }

            // Reagrupar as compras filtradas por data
            agruparCompras(listaCompras);

            btnClearFilter.setVisible(true);
            btnClearFilter.setManaged(true);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao aplicar os filtros: " + e.getMessage());
        }
    }

    private void filtrarPorMesAtual() {
        try {
            List<Compra> listaCompras = compraService.buscarCompras(token);
            LocalDate hoje = LocalDate.now();
            int mesAtual = hoje.getMonthValue();
            int anoAtual = hoje.getYear();
    
            listaCompras = listaCompras.stream()
                    .filter(compra -> compra.getEntryAt() != null &&
                            compra.getEntryAt().toLocalDate().getMonthValue() == mesAtual &&
                            compra.getEntryAt().toLocalDate().getYear() == anoAtual)
                    .collect(Collectors.toList());
    
            Map<LocalDate, TreeItem<Compra>> gruposPorData = new LinkedHashMap<>();
            for (Compra compra : listaCompras) {
                LocalDate data = compra.getEntryAt().toLocalDate();
                gruposPorData.putIfAbsent(data, new TreeItem<>(new Compra(data.atStartOfDay())));
                gruposPorData.get(data).getChildren().add(new TreeItem<>(compra));
            }
            TreeItem<Compra> root = new TreeItem<>(new Compra());
            root.getChildren().addAll(gruposPorData.values());
            tabelaCompra.setRoot(root);
            tabelaCompra.setShowRoot(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao filtrar por mês atual: " + e.getMessage());
        }
    }

    
    @FXML
    private void handleLimparFiltros() {
        datePickerInicio.setValue(null);
        datePickerFim.setValue(null);
        comboFornecedor.setValue(null);
        comboStatusPagamento.setValue(null);
        carregarCompras();
        filtrarPorMesAtual();
        
        btnClearFilter.setVisible(false);
        btnClearFilter.setManaged(false);
    }
    
    //------------------- Carregar dados -------------------//

    private void carregarCompras() {
        try {
            List<Compra> listaCompras = compraService.buscarCompras(token);

            agruparCompras(listaCompras);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
        }
    }

    private void agruparCompras(List<Compra> listaCompras) {
        Map<LocalDate, TreeItem<Compra>> gruposPorData = new LinkedHashMap<>();

        for (Compra compra : listaCompras) {
            LocalDate data = compra.getEntryAt().toLocalDate();

            gruposPorData.putIfAbsent(data, new TreeItem<>(new Compra(data.atStartOfDay())));

            gruposPorData.get(data).getChildren().add(new TreeItem<>(compra));
        }

        // Define a raiz da tabela com os grupos de data
        TreeItem<Compra> root = new TreeItem<>(new Compra());
        root.getChildren().addAll(gruposPorData.values());

        // Atualiza a TreeTableView com a raiz e oculta a raiz principal
        tabelaCompra.setRoot(root);
        tabelaCompra.setShowRoot(false); // Oculta a raiz principal
    }

    private void carregarListaFornecedores(){

        try {
            List<Fornecedor> fornecedores = fornecedorService.buscarFornecedores(token).stream()
                    .filter(Fornecedor::isActive)
                    .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
                    .collect(Collectors.toList());
            comboFornecedor.setItems(FXCollections.observableArrayList(fornecedores));

            comboFornecedor.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Fornecedor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getSocialReason());
                    }
                }
            });

            comboFornecedor.setButtonCell(new ListCell<Fornecedor>() {
                protected void updateItem(Fornecedor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getSocialReason());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    private void popularFiltros() {
        try {
            carregarListaFornecedores();
            comboStatusPagamento.setItems(FXCollections.observableArrayList(PaymentStatus.values()));
            comboStatusPagamento.setConverter(new StringConverter<PaymentStatus>() {
                @Override
                public String toString(PaymentStatus status) {
                    return status != null ? status.getDescription() : "";
                }
                @Override
                public PaymentStatus fromString(String string) {
                    for (PaymentStatus status : PaymentStatus.values()) {
                        if (status.getDescription().equals(string)) {
                            return status;
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar filtros: " + e.getMessage());
        }
    }

    /*---------------- formatações -------------------*/
    private String configurarData(String data) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDateTime date = LocalDateTime.parse(data, inputFormatter);

        return date.format(outputFormatter);
    }

    private void abrirDetalhesCompra(Compra compra) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/compras/CompraDetalhes.fxml"));
            CompraDetalhesController controller = new CompraDetalhesController(compraService, token, compra);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Detalhes da Compra");
            stage.setScene(new Scene(root, 800, 600));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir detalhes: " + e.getMessage());
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
