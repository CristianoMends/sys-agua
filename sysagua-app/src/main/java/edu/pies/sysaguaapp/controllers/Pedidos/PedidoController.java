package edu.pies.sysaguaapp.controllers.Pedidos;

import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatusPagamento;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.PedidoService;
import edu.pies.sysaguaapp.services.EntregadorService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PedidoController {

    private final EntregadorService entregadorService;
    private final String token;
    private final PedidoService pedidoService;

    private final ClientesService clientesService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TreeTableView<Pedido> tabelaPedido;

    @FXML
    private Label successMessage;

    @FXML
    private Button btnAdicionar, btnFiltrar, btnClearFilter;

    @FXML
    private DatePicker datePickerInicio, datePickerFim;

    @FXML
    private ComboBox<Entregador> comboEntregador;

    @FXML
    private ComboBox<PaymentStatus> comboStatusPagamento;

    @FXML
    private ComboBox<Clientes> comboClientes;

    @FXML
    private ComboBox<PedidoStatus> comboStatusPedido;

    @FXML
    private CheckBox exibirCompraMes;

    private ObservableList<Pedido> pedidoObservable;

    public PedidoController() {
        pedidoService = new PedidoService();
        entregadorService = new EntregadorService();
        clientesService = new ClientesService();
        this.pedidoObservable = FXCollections.observableArrayList();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarPedidos();
        showMenuContext();
        btnAdicionar.setCursor(Cursor.HAND);
        btnFiltrar.setCursor(Cursor.HAND);
        btnClearFilter.setCursor(Cursor.HAND);
        popularFiltros();
        exibirCompraMes.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                filtrarPorMesAtual();
            } else {
                carregarPedidos();
            }
        });
        exibirCompraMes.setSelected(true);
    }


    /*------------------- pedidos --------------*/

    @FXML
    private void handleAddPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Pedidos/AddPedido.fxml"));
            Parent form = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(form);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar formulário de pedido: " + e.getMessage());
        }
    }

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cancelarPedido = new MenuItem("Cancelar pedido");

        contextMenu.getItems().addAll(cancelarPedido);


        cancelarPedido.setOnAction(event -> handleCancelarPedido());

        tabelaPedido.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaPedido.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaPedido, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleCancelarPedido() {
        Pedido pedidoSelecionado = tabelaPedido.getSelectionModel().getSelectedItem().getValue();
        if (pedidoSelecionado != null) {
            try {
                pedidoService.cancelarPedido(pedidoSelecionado, token);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao cancelar pedido " + e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {

        TreeTableColumn<Pedido, Long> colunaNumeroPedido = new TreeTableColumn<>("Número do Pedido");
        colunaNumeroPedido.setCellValueFactory(param -> {
            Long id = param.getValue().getValue().getId();
            if(id != null && id >= 0){
                return new SimpleLongProperty(id).asObject();
            }
            return new SimpleLongProperty(0).asObject();
        });

        TreeTableColumn<Pedido, String> colunaNomeCliente = new TreeTableColumn<>("Nome do Cliente");
        colunaNomeCliente.setCellValueFactory(param -> {
            Pedido pedido = param.getValue().getValue();

            if (pedido == null || pedido.getCliente() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(pedido.getCliente().getName());
        });

        TreeTableColumn<Pedido, String> colunaData = new TreeTableColumn<>("Data do Pedido");
        colunaData.setCellValueFactory(param -> {
            LocalDateTime dataPedido = param.getValue().getValue().getDataPedido();
            if (dataPedido != null) {
                return new SimpleStringProperty(dataPedido.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return new SimpleStringProperty("");
        });

        TreeTableColumn<Pedido, String> colunaStatusPedido = new TreeTableColumn<>("Status do Pedido");
        colunaStatusPedido.setCellValueFactory(param -> {
            Enum status = param.getValue().getValue().getStatus();
            if (status != null) {
                return new SimpleStringProperty(status.name());
            }
            return new SimpleStringProperty("");
        });

        TreeTableColumn<Pedido, String> colunaStatusPagamento = new TreeTableColumn<>("Status de Pagamento");
        colunaStatusPagamento.setCellValueFactory(param -> {
            Pedido pedido = param.getValue().getValue();

            if (pedido == null || pedido.getCliente() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(pedido.getStatusPagamento().getDescricao());
        });


        TreeTableColumn<Pedido, BigDecimal> colunaValorTotal = new TreeTableColumn<>("Valor Total");
        colunaValorTotal.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getValue().getValorTotal()));


        TreeTableColumn<Pedido, String> colunaNomeEntregador = new TreeTableColumn<>("Nome do Entregador");
        colunaNomeEntregador.setCellValueFactory(param -> {
            Pedido pedido = param.getValue().getValue();

            if (pedido == null || pedido.getEntregador() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(pedido.getEntregador().getName());
        });


        // Adiciona as colunas na TreeTableView
        tabelaPedido.getColumns().clear();
        tabelaPedido.getColumns().addAll(colunaNumeroPedido, colunaNomeCliente, colunaData, colunaStatusPedido, colunaStatusPagamento,  colunaValorTotal, colunaNomeEntregador);

        tabelaPedido.setRowFactory(tv -> {
            TreeTableRow<Pedido> row = new TreeTableRow<Pedido>() {
                @Override
                protected void updateItem(Pedido item, boolean empty) {
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
        tabelaPedido.setRoot(new TreeItem<>(new Pedido()));
        tabelaPedido.setShowRoot(false);
    }

    @FXML
    private void handleFiltrar() {
        try {
            List<Pedido> listaPedido = pedidoService.buscarPedidos(token);

            LocalDate inicio = datePickerInicio.getValue();
            LocalDate fim = datePickerFim.getValue();
            if (inicio != null) {
                listaPedido = listaPedido.stream()
                        .filter(pedido -> pedido.getDataPedido() != null &&
                                !pedido.getDataPedido().toLocalDate().isBefore(inicio))
                        .collect(Collectors.toList());
            }
            if (fim != null) {
                listaPedido = listaPedido.stream()
                        .filter(pedido -> pedido.getDataPedido() != null &&
                                !pedido.getDataPedido().toLocalDate().isAfter(fim))
                        .collect(Collectors.toList());
            }

            if (comboClientes.getValue() != null) {
                String nomeClienteSelecionado = comboClientes.getValue().getName();
                if (nomeClienteSelecionado != null) {
                    listaPedido = listaPedido.stream()
                            .filter(pedido -> pedido.getCliente() != null &&
                                    nomeClienteSelecionado.equals(pedido.getCliente().getName()))
                            .collect(Collectors.toList());
                }
            }
            if (comboStatusPedido.getValue() != null) {
                PedidoStatus statusSelecionado = comboStatusPedido.getValue();
                if (statusSelecionado != null) {
                    listaPedido = listaPedido.stream()
                            .filter(pedido -> statusSelecionado.equals(pedido.getStatus()))
                            .collect(Collectors.toList());
                }
            }


            if (comboStatusPagamento.getValue() != null) {
                PaymentStatus statusPagamentoSelecionado = comboStatusPagamento.getValue();
                if (statusPagamentoSelecionado != null) {
                    listaPedido = listaPedido.stream()
                            .filter(pedido -> statusPagamentoSelecionado.equals(pedido.getStatusPagamento().getDescricao()))
                            .collect(Collectors.toList());
                }
            }

            if (comboEntregador.getValue() != null) {
                String nomeEntregadorSelecionado = comboEntregador.getValue().getName();
                if (nomeEntregadorSelecionado != null) {
                    listaPedido = listaPedido.stream()
                            .filter(pedido -> pedido.getEntregador() != null &&
                                    nomeEntregadorSelecionado.equals(pedido.getEntregador().getName()))
                            .collect(Collectors.toList());
                }
            }

            // Reagrupar as compras filtradas por data
            agruparPedidos(listaPedido);

            btnClearFilter.setVisible(true);
            btnClearFilter.setManaged(true);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao aplicar os filtros: " + e.getMessage());
        }
    }

    private void filtrarPorMesAtual() {
        try {
            List<Pedido> listaPedido = pedidoService.buscarPedidos(token);
            LocalDate hoje = LocalDate.now();
            int mesAtual = hoje.getMonthValue();
            int anoAtual = hoje.getYear();

            listaPedido = listaPedido.stream()
                    .filter(pedido -> pedido.getDataPedido() != null &&
                            pedido.getDataPedido().toLocalDate().getMonthValue() == mesAtual &&
                            pedido.getDataPedido().toLocalDate().getYear() == anoAtual)
                    .collect(Collectors.toList());

            Map<LocalDate, TreeItem<Pedido>> gruposPorData = new LinkedHashMap<>();
            for (Pedido pedido : listaPedido) {
                LocalDate data = pedido.getDataPedido().toLocalDate();
                gruposPorData.putIfAbsent(data, new TreeItem<>(new Pedido()));
                gruposPorData.get(data).getChildren().add(new TreeItem<>(pedido));
            }
            TreeItem<Pedido> root = new TreeItem<>(new Pedido());
            root.getChildren().addAll(gruposPorData.values());
            tabelaPedido.setRoot(root);
            tabelaPedido.setShowRoot(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao filtrar por mês atual: " + e.getMessage());
        }
    }


    @FXML
    private void handleLimparFiltros() {
        datePickerInicio.setValue(null);
        datePickerFim.setValue(null);
        comboStatusPedido.setValue(null);
        comboStatusPagamento.setValue(null);
        comboClientes.setValue(null);
        carregarPedidos();
        filtrarPorMesAtual();
        btnClearFilter.setVisible(false);
        btnClearFilter.setManaged(false);
    }

    //------------------- Carregar dados -------------------//

    private void carregarPedidos() {
        try {
            List<Pedido> listaPedidos = pedidoService.buscarPedidos(token);

            agruparPedidos(listaPedidos);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
        }
    }

    private void agruparPedidos(List<Pedido> listaPedidos) {
        Map<LocalDate, TreeItem<Pedido>> gruposPorData = new LinkedHashMap<>();

        for (Pedido pedido : listaPedidos) {
            LocalDate data = pedido.getDataPedido().toLocalDate();

            gruposPorData.putIfAbsent(data, new TreeItem<>(new Pedido(data.atStartOfDay())));

            gruposPorData.get(data).getChildren().add(new TreeItem<>(pedido));
        }

        // Define a raiz da tabela com os grupos de data
        TreeItem<Pedido> root = new TreeItem<>(new Pedido());
        root.getChildren().addAll(gruposPorData.values());

        // Atualiza a TreeTableView com a raiz e oculta a raiz principal
        tabelaPedido.setRoot(root);
        tabelaPedido.setShowRoot(false); // Oculta a raiz principal
    }

    private void carregarListaClientes(){

        try {
            List<Clientes> clientes = clientesService.buscarClientes(token).stream()
                    .filter(Clientes::getActive)
                    .sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
                    .collect(Collectors.toList());
            comboClientes.setItems(FXCollections.observableArrayList(clientes));

            comboClientes.setCellFactory(param -> new ListCell<Clientes>() {
                @Override
                protected void updateItem(Clientes item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });

            comboClientes.setButtonCell(new ListCell<Clientes>() {
                protected void updateItem(Clientes item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
    private void carregarListaEntregadores(){

        try {
            List<Entregador> entregadores = entregadorService.buscarEntregadores(token).stream()
                    .filter(Entregador::getActive)
                    .sorted((e1, e2) -> Long.compare(e1.getId(), e2.getId()))
                    .collect(Collectors.toList());
            comboEntregador.setItems(FXCollections.observableArrayList(entregadores));

            comboEntregador.setCellFactory(param -> new ListCell<Entregador>() {
                @Override
                protected void updateItem(Entregador item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });

            comboEntregador.setButtonCell(new ListCell<Entregador>() {
                protected void updateItem(Entregador item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
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
            carregarListaClientes();
            carregarListaEntregadores();
            comboStatusPedido.setItems(FXCollections.observableArrayList(PedidoStatus.values()));
            comboStatusPedido.setConverter(new StringConverter<PedidoStatus>() {
                @Override
                public String toString(PedidoStatus statusPedido) {
                    return statusPedido != null ? statusPedido.getDescricao() : "";
                }

                @Override
                public PedidoStatus fromString(String string) {
                    for (PedidoStatus statusPedido : PedidoStatus.values()) {
                        if (statusPedido.getDescricao().equals(string)) {
                            return statusPedido;
                        }
                    }
                    return null;
                }
            });
            comboStatusPagamento.setItems(FXCollections.observableArrayList(PaymentStatus.values()));
            comboStatusPagamento.setConverter(new StringConverter<PaymentStatus>() {
                public String toString(PaymentStatus status) {
                    return status != null ? status.getDescription() : "";
                }
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
