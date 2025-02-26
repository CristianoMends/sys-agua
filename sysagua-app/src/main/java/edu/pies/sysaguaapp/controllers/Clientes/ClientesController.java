package edu.pies.sysaguaapp.controllers.Clientes;

import edu.pies.sysaguaapp.models.Address;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.services.ClientesService;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.util.List;

public class ClientesController {

    private ClientesService clienteService;
    private final String token;
    private Address address;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Clientes> tabelaClientes;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar;

    @FXML
    private Label successMessage;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private ObservableList<Clientes> clientesObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;


    public ClientesController() {
        this.clienteService = new ClientesService();
        token = TokenManager.getInstance().getToken();
        this.clientesObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarClientes();
        showMenuContext();
        btnAdicionar.setCursor(Cursor.HAND);
        exibirInativosCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> carregarClientes());
    }

    @FXML
    private void handleAddCliente() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Clientes/AddCliente.fxml"));
            Parent form = loader.load();
            rootPane.getChildren().clear();
            rootPane.getChildren().add(form);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro ao carregar formulário de cliente: " + e.getMessage());
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

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarCliente= new MenuItem("Editar Cliente");
        MenuItem inativarCliente = new MenuItem("Inativar Cliente");

        // Adiciona as opções ao menu
        contextMenu.getItems().addAll(editarCliente, inativarCliente);

        // Ação para Editar Cliente
        editarCliente.setOnAction(event -> handleEditarCliente());

        // Ação para Inativar Cliente
        inativarCliente.setOnAction(event -> handleInativarCliente());

        tabelaClientes.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaClientes.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaClientes, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleEditarCliente() {
        Clientes clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Clientes/AddCliente.fxml"));
                Parent form = loader.load();

                AddClientesController controller = loader.getController();

                controller.setClienteEditando(String.valueOf(clienteSelecionado.getId()));

                rootPane.getChildren().clear();
                rootPane.getChildren().add(form);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao carregar formulário de cliente: " + e.getMessage());
            }
        }
    }

    private void handleInativarCliente() {
        Clientes clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            try{
                String token = TokenManager.getInstance().getToken();
                ClientesService.inativarCliente(clienteSelecionado, token);
                tabelaClientes.getItems().remove(clienteSelecionado);
                tabelaClientes.refresh();
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Erro ao inativar cliente: " + e.getMessage());
            }            
        }
    }

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Clientes, Long> colunaId = new TableColumn<>("Código");
        colunaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colunaId.setStyle("-fx-alignment: CENTER;");
        colunaId.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn<Clientes, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colunaNome.setStyle("-fx-alignment: CENTER;");

        TableColumn<Clientes, String> colunaEnderecoCompleto = new TableColumn<>("Endereço");
        colunaEnderecoCompleto.setCellValueFactory(cellData -> {
            String numero = cellData.getValue().getAddress().getNumber();
            String rua = cellData.getValue().getAddress().getStreet();
            String bairro = cellData.getValue().getAddress().getNeighborhood();
            String cidade = cellData.getValue().getAddress().getCity();
            String estado = cellData.getValue().getAddress().getState();

            String enderecoFormatado = String.format("%s, %s - %s, %s - %s", bairro, numero, rua, cidade, estado);
            return new SimpleStringProperty(enderecoFormatado);
        });

        TableColumn<Clientes, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Clientes, String> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        TableColumn<Clientes, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(cellData -> {
            if(cellData.getValue().getActive()){
                return new SimpleStringProperty("Ativo");
            }
            return new SimpleStringProperty("Inativo");
        });
        colunaStatus.setStyle("-fx-alignment: CENTER;");

        tabelaClientes.getColumns().addAll(colunaId, colunaNome, colunaEnderecoCompleto, colunaTelefone, colunaCnpj);
        tabelaClientes.setItems(clientesObservable);

        tabelaClientes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Clientes cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (cliente == null || empty) {
                    setStyle("");
                } else if (!cliente.getActive()) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });

    }

    private void carregarClientes() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Clientes> todosClientes = clienteService.buscarClientes(token);

            if(!exibirInativosCheckBox.isSelected()) {
                todosClientes.removeIf(cliente -> !cliente.getActive());
            }

            totalPaginas = (int) Math.ceil((double) todosClientes.size() / itensPorPagina);

            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosClientes.size());
            List<Clientes> clientesPagina = todosClientes.subList(inicio, fim);

            clientesObservable.setAll(clientesPagina);
            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista:" + e.getMessage());
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
        carregarClientes();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarClientes();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarClientes();
        }
    }

    
    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }
}