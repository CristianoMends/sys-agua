package edu.pies.sysaguaapp.controllers.Pedidos;

import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.PedidoService;
import edu.pies.sysaguaapp.services.EntregadorService;
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
        carregarListaClientes();
        carregarListaEntregadores();
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
                String token = TokenManager.getInstance().getToken();
                pedidoService.cancelarPedido(pedidoSelecionado, token);

                pedidoSelecionado.setDeliveryStatus(PedidoStatus.CANCELED);
                tabelaPedido.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao cancelar pedido " + e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        TreeTableColumn<Pedido, String> colunaData = new TreeTableColumn<>("Data");
        colunaData.setCellValueFactory(param -> {
            LocalDateTime createAt = param.getValue().getValue().getCreatedAt();
            if (createAt != null) {
                return new SimpleStringProperty(createAt.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return new SimpleStringProperty("");
        });

        TreeTableColumn<Pedido, String> colunaNomeCliente = new TreeTableColumn<>("Cliente");
        colunaNomeCliente.setCellValueFactory(param -> {
            Pedido pedido = param.getValue().getValue();

            if (pedido == null || pedido.getCustomer() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(pedido.getCustomer().getName());
        });


        TreeTableColumn<Pedido, BigDecimal> colunaValorTotal = new TreeTableColumn<>("Valor Total");
        colunaValorTotal.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getValue().getTotalAmount()));

        TreeTableColumn<Pedido, String> colunaStatus = new TreeTableColumn<>("Pagamento");
        colunaStatus.setCellValueFactory(param-> {
            Pedido pedido = param.getValue().getValue();
            if (pedido.getDeliveryStatus() == PedidoStatus.CANCELED) {
                return new SimpleObjectProperty<>("Cancelado");
            }
            if(pedido.getPaymentStatus() != null){
                return new SimpleObjectProperty<>(pedido.getPaymentStatus().getDescription());
            }
            return new SimpleObjectProperty<>("");
        });


        TreeTableColumn<Pedido, String> colunaNomeEntregador = new TreeTableColumn<>("Entregador");
        colunaNomeEntregador.setCellValueFactory(param -> {
            Pedido pedido = param.getValue().getValue();

            if (pedido == null || pedido.getDeliveryPerson() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(pedido.getDeliveryPerson().getName());
        });


        // Adiciona as colunas na TreeTableView
        tabelaPedido.getColumns().clear();
        tabelaPedido.getColumns().addAll(colunaData, colunaNomeCliente , colunaValorTotal, colunaStatus, colunaNomeEntregador);

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

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Pedido pedidoSelecionado = row.getItem();
                    if (pedidoSelecionado != null && row.getTreeItem().getParent() != null) {
                        abrirDetalhesPedido(pedidoSelecionado);
                    }
                }
            });

            return row;
        });

        // Define o root inicial (vazio)
        tabelaPedido.setRoot(new TreeItem<>(new Pedido()));
        tabelaPedido.setShowRoot(false);
    }

    private void abrirDetalhesPedido(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Pedidos/PedidoDetalhes.fxml"));
            PedidoDetalhesController controller = new PedidoDetalhesController(pedidoService, token, pedido);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Detalhes do Pedido");
            stage.setScene(new Scene(root, 800, 600));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir detalhes: " + e.getMessage());
        }
    }

    @FXML
    private void handleFiltrar() {
        try {
            List<Pedido> listaPedido = pedidoService.buscarPedidos(token);

            LocalDate inicio = datePickerInicio.getValue();
            LocalDate fim = datePickerFim.getValue();
            // Filtrar pedidos baseado no intervalo de datas
            listaPedido = listaPedido.stream()
                    .filter(pedido -> {
                        boolean dentroDoIntervalo = true;

                        // Verificar data de início
                        if (inicio != null && pedido.getCreatedAt() != null) {
                            dentroDoIntervalo &= !pedido.getCreatedAt().toLocalDate().isBefore(inicio);
                        }

                        // Verificar data de fim
                        if (fim != null && pedido.getCreatedAt() != null) {
                            dentroDoIntervalo &= !pedido.getCreatedAt().toLocalDate().isAfter(fim);
                        }

                        return dentroDoIntervalo;
                    })
                    .filter(pedido -> {
                        // Filtrando por status de pagamento (Cancelado ou outro)
                        if (comboStatusPagamento.getValue() != null) {
                            String statusPagamentoSelecionado = comboStatusPagamento.getValue().getDescription();
                            if ("Cancelado".equals(statusPagamentoSelecionado)) {
                                pedido.setDeliveryStatus(PedidoStatus.CANCELED);
                                return pedido.getDeliveryStatus() == PedidoStatus.CANCELED;
                            } else if (pedido.getPaymentStatus() != null) {
                                return statusPagamentoSelecionado.equals(pedido.getPaymentStatus().getDescription());
                            }
                        }
                        return true; // Se não houver filtro de status de pagamento, mantemos o pedido
                    })
                    .filter(pedido -> {
                        // Filtrando por nome do cliente
                        if (comboClientes.getValue() != null) {
                            String nomeClienteSelecionado = comboClientes.getValue().getName();
                            if (nomeClienteSelecionado != null && pedido.getCustomer() != null) {
                                return nomeClienteSelecionado.equals(pedido.getCustomer().getName());
                            }
                        }
                        return true; // Se não houver filtro de cliente, mantemos o pedido
                    })
                    .filter(pedido -> {
                        // Filtrando por nome do entregador
                        if (comboEntregador.getValue() != null) {
                            String nomeEntregadorSelecionado = comboEntregador.getValue().getName();
                            if (nomeEntregadorSelecionado != null && pedido.getDeliveryPerson() != null) {
                                return nomeEntregadorSelecionado.equals(pedido.getDeliveryPerson().getName());
                            }
                        }
                        return true; // Se não houver filtro de entregador, mantemos o pedido
                    })
                    .collect(Collectors.toList());

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
                    .filter(pedido -> pedido.getCreatedAt() != null &&
                            pedido.getCreatedAt().toLocalDate().getMonthValue() == mesAtual &&
                            pedido.getCreatedAt().toLocalDate().getYear() == anoAtual)
                    .collect(Collectors.toList());

            Map<LocalDate, TreeItem<Pedido>> gruposPorData = new LinkedHashMap<>();
            for (Pedido pedido : listaPedido) {
                LocalDate data = pedido.getCreatedAt().toLocalDate();
                gruposPorData.putIfAbsent(data, new TreeItem<>(new Pedido(data.atStartOfDay())));
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
        comboStatusPagamento.setValue(null);
        comboClientes.setValue(null);
        comboEntregador.setValue(null);
        carregarPedidos();
        filtrarPorMesAtual();
        btnClearFilter.setVisible(false);
        btnClearFilter.setManaged(false);
    }

    //------------------- Carregar dados -------------------//

    private void carregarPedidos() {
        try {
            List<Pedido> listaPedidos = pedidoService.buscarPedidos(token);
            if (listaPedidos != null && !listaPedidos.isEmpty()) {
                agruparPedidos(listaPedidos);
            } else {
                System.out.println("Nenhum pedido encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
        }
    }

    private void agruparPedidos(List<Pedido> listaPedidos) {
        Map<LocalDate, TreeItem<Pedido>> gruposPorData = new LinkedHashMap<>();

        for (Pedido pedido : listaPedidos) {
            LocalDate data = pedido.getCreatedAt().toLocalDate();

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
