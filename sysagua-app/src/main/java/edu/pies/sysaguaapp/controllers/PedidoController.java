package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.Pedido.PedidoStatus;
import edu.pies.sysaguaapp.models.Pedido.PedidoStatusPagamento;
import edu.pies.sysaguaapp.services.PedidoService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class PedidoController {
    @FXML
    private TableView<Pedido> tabelaPedidos;

    @FXML
    private TextField campoNomeCliente;

    @FXML
    private TextField campoNomeEntregador;

    private ObservableList<Pedido> pedidosObservable;

    @FXML
    private VBox listPedidoView;

    @FXML
    private DatePicker datePicker;

    private PedidoService pedidoService;

    @FXML
    private StackPane rootPane;

    @FXML
    private Label successMessage;

    @FXML
    private Button btnAnterior;

    @FXML
    private Rectangle overlay;

    @FXML
    private Button btnProximo;

    @FXML
    private VBox detailsFormPedido;

    @FXML
    private HBox paginationContainer;

    @FXML
    private BorderPane formCadastroPedido;

    @FXML
    private ComboBox<PedidoStatus> comboBoxStatus;



    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private Pedido pedidoEditado = null;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public PedidoController() {
        this.pedidoService = new PedidoService();
        this.pedidosObservable = FXCollections.observableArrayList();
    }
    @FXML
    public void initialize(){
        comboBoxStatus.setItems(FXCollections.observableArrayList(PedidoStatus.values()));
        comboBoxStatus.setConverter(createPedidoStatusStringConverter());
        configurarTabela();
    }
    private StringConverter<PedidoStatus> createPedidoStatusStringConverter() {
        return new StringConverter<PedidoStatus>() {
            @Override
            public String toString(PedidoStatus status) {
                return status != null ? status.getDescricao() : "";
            }

            @Override
            public PedidoStatus fromString(String string) {
                return null; // Não utilizado nesse caso específico
            }
        };
    }
    private void showOverlay() {
        overlay.maxWidth(rootPane.widthProperty().get());
        overlay.maxHeight(rootPane.heightProperty().get());
        overlay.widthProperty().bind(rootPane.widthProperty());
        overlay.heightProperty().bind(rootPane.heightProperty());
        overlay.setVisible(true);
        overlay.setManaged(true);
    }

    @FXML
    private void handleAbrirCalendario() {
        // Exibe o DatePicker ao clicar no ícone ou no texto
        if (datePicker != null) {
            datePicker.show();
        }
    }

    private void hideOverlay() {
        overlay.setVisible(false);
        overlay.setManaged(false);
    }

    @FXML
    private void updateButtonText() {
        if (pedidoEditado != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }

    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFieldForm() {

    }

    @FXML
    private void handleCancelar() {
        hideForm();
        hideOverlay();
        listPedidoView.setDisable(false);
    }
    @FXML
    private void handleDetailsPedidoModal() {
        if (!detailsFormPedido.isVisible()) {
            showModal(detailsFormPedido);
        }
    }

    private void showModal(VBox toShow) {
        toShow.setManaged(true);
        toShow.toFront();
        toShow.setVisible(true);
    }

    @FXML
    private void hideDetailsModal(VBox root) {
        root.setManaged(false);
        root.setVisible(false);
    }

    @FXML
    private void handleAddPedido() {
        if (!formCadastroPedido.isVisible()) {
            showOverlay();
            showForm();
            listPedidoView.setDisable(true);
        }
    }
    private void showForm() {
        formCadastroPedido.setManaged(true);
        formCadastroPedido.toFront();
        formCadastroPedido.setVisible(true);
    }

    private void hideForm() {
        formCadastroPedido.setVisible(false);
        formCadastroPedido.setManaged(false);
    }

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarPedido= new MenuItem("Editar Pedido");
        MenuItem inativarPedido = new MenuItem("Inativar Pedido");

        contextMenu.getItems().addAll(editarPedido, inativarPedido);

        editarPedido.setOnAction(event -> handleEditarPedido());


        inativarPedido.setOnAction(event -> handleInativarPedido());

        tabelaPedidos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaPedidos.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaPedidos, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }
    private void handleEditarPedido() {
        Pedido pedidoSelecionado = (Pedido) tabelaPedidos.getSelectionModel().getSelectedItem();

        if (pedidoSelecionado != null) {
            try{
                clearFieldForm();
                preencherCampos(pedidoSelecionado);
                pedidoEditado = pedidoSelecionado;
                updateButtonText();
                showOverlay();
                listPedidoView.setDisable(true);
                showForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void preencherCampos(Pedido pedidos) {

    }

    private void handleInativarPedido() {
        Pedido pedidoSelecionado = tabelaPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSelecionado != null) {
            try{
                String token = TokenManager.getInstance().getToken();
                PedidoService.inativarPedido(pedidoSelecionado, token);
                tabelaPedidos.getItems().remove(pedidoSelecionado);
                pedidosObservable.remove(pedidoSelecionado);

                showSuccessMessageInativarCliente("Pedido inativado com sucesso!");
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Erro ao inativar pedido: " + e.getMessage());
            }
        }
        else{
            System.out.println("Nenhum pedido selecionado!");
        }
    }

    private void showSuccessMessageInativarCliente(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void carregarPedidos() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Pedido> todosPedidos = pedidoService.buscarPedidos(token);


            List<Pedido> pedidosAtivos = todosPedidos.stream()
                    .filter(pedido -> pedido.isActive())
                    .collect(Collectors.toList());

            totalPaginas = (int) Math.ceil((double) pedidosAtivos.size() / itensPorPagina);

            // Filtra os produtos para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, pedidosAtivos.size());
            List<Pedido> pedidosPagina = pedidosAtivos.subList(inicio, fim);

            // Atualiza a lista observável
            pedidosObservable.setAll(pedidosPagina);
            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
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
        carregarPedidos();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarPedidos();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarPedidos();
        }
    }
    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Pedido, Long> colunaNumeroPedido = new TableColumn<>("Número do Pedido");
        colunaNumeroPedido.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumeroPedido()));

        TableColumn<Pedido, String> colunaNomeCliente = new TableColumn<>("Nome do Cliente");
        colunaNomeCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getName()));

        TableColumn<Pedido, LocalDateTime> colunaDataPedido = new TableColumn<>("Data do Pedido");
        colunaDataPedido.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataPedido()));

        TableColumn<Pedido, PedidoStatus> colunaStatusPedido = new TableColumn<>("Status do Pedido");
        colunaStatusPedido.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        TableColumn<Pedido, PedidoStatusPagamento> colunaStatusPagamento = new TableColumn<>("Status do pagamento");
        colunaStatusPagamento.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatusPagamento()));

        TableColumn<Pedido, BigDecimal> colunaValorTotal = new TableColumn<>("Valor Total");
        colunaValorTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValorTotal()));

        TableColumn<Pedido, String> colunaNomeEntregador = new TableColumn<>("Nome do Entregador");
        colunaNomeEntregador.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEntregador().getName()));

        tabelaPedidos.getColumns().addAll(colunaNumeroPedido, colunaNomeCliente, colunaDataPedido, colunaStatusPedido, colunaStatusPagamento, colunaValorTotal, colunaNomeEntregador);
        tabelaPedidos.setItems(pedidosObservable);
    }

    @FXML
    private void handlePesquisarPedidos() {
        String nomeCliente = campoNomeCliente.getText().trim();

        if (!nomeCliente.isEmpty()) {
            List<Pedido> pedidosFiltrados = pedidosObservable.stream()
                    .filter(p -> p.getCliente().getName().toLowerCase().contains(nomeCliente.toLowerCase()))
                    .collect(Collectors.toList());
            tabelaPedidos.setItems(FXCollections.observableArrayList(pedidosFiltrados));
        } else {
            tabelaPedidos.setItems(pedidosObservable); // Resetar a tabela se o campo estiver vazio
        }
    }

    @FXML
    private void handleFiltrarPedidos() {
        String nomeEntregador = campoNomeEntregador.getText().trim();

        if (!nomeEntregador.isEmpty()) {
            List<Pedido> pedidosFiltrados = pedidosObservable.stream()
                    .filter(p -> p.getEntregador().getName().toLowerCase().contains(nomeEntregador.toLowerCase()))
                    .collect(Collectors.toList());
            tabelaPedidos.setItems(FXCollections.observableArrayList(pedidosFiltrados));
        } else {
            tabelaPedidos.setItems(pedidosObservable    ); // Resetar a tabela se o campo estiver vazio
        }
    }


}
