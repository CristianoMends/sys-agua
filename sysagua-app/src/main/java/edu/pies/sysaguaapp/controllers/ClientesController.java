package edu.pies.sysaguaapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Address;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.TokenManager;
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
import java.util.List;
import java.util.stream.Collectors;

public class ClientesController {

    private ClientesService clienteService;

    @FXML
    private StackPane rootPane;

    @FXML
    private Label successMessage;

    @FXML
    private TextField nomeField;

    @FXML
    private VBox addressBox;
    @FXML
    private TextField numberField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField neighborhoodField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;

    private Address address;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField CNPJField;

    @FXML
    private BorderPane formCadastroClientes;

    @FXML
    private Button btnAnterior;

    @FXML
    private Rectangle overlay;

    @FXML
    private Button btnProximo;

    @FXML
    private VBox listClienteView;

    @FXML
    private VBox detailsFormCliente;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TableView<Clientes> tabelaClientes;
    private ObservableList<Clientes> clientesObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;
    private Clientes clienteEditado = null;

    public ClientesController() {
        this.clienteService = new ClientesService();
        this.clientesObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarClientes();
        showMenuContext();
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

    @FXML
    private void updateButtonText() {
        if (clienteEditado != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }

    @FXML
    private void handleSalvar() {
        if (!validarCamposComErros()) {
            // Caso algum campo tenha erro, não permite salvar
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campos inválidos");
            alert.setContentText("Por favor, corrija os campos inválidos.");
            alert.showAndWait();
            return; // Interrompe o salvamento
        }

            String nome = nomeField.getText();
            String telefoneStr = telefoneField.getText();
            String cnpjStr = CNPJField.getText();
            String numeroStr = numberField.getText();
            String rua = streetField.getText();
            String bairro = neighborhoodField.getText();
            String cidade = cityField.getText();
            String estado = stateField.getText();

            validarCampos();

            // Cria o objeto Address
            Address clienteAddress = new Address();
            clienteAddress.setNumber(numeroStr);
            clienteAddress.setStreet(rua);
            clienteAddress.setNeighborhood(bairro);
            clienteAddress.setCity(cidade);
            clienteAddress.setState(estado);


            Clientes novoCliente = new Clientes();
            novoCliente.setName(nome);
            novoCliente.setAddress(clienteAddress);
            novoCliente.setPhone(telefoneStr);
            novoCliente.setCnpj(cnpjStr);

            try{
                String token = TokenManager.getInstance().getToken();
                if (clienteEditado != null){
                    novoCliente.setId(clienteEditado.getId());
                    novoCliente.setActive(true);
                    ClientesService.editarCliente(novoCliente, token);
                    clientesObservable.set(clientesObservable.indexOf(clienteEditado), novoCliente);
                }
                else{
                    novoCliente.setActive(true);
                    ClientesService.criarCliente(novoCliente, token);
                    clientesObservable.add(novoCliente);
                }        
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao criar cliente");
            System.out.println(e.getMessage());
            alert.setContentText(e.getMessage());
            hideForm();
            hideOverlay();
            alert.showAndWait();
            return;
        }
        hideForm();
        hideOverlay();
        clearFieldForm();
        listClienteView.setDisable(false);
        showSucessMessage();
        carregarClientes();
    }
    private boolean validarCamposComErros() {
        boolean isValid = true;

        // Validando número
        if (numberField.getText().isEmpty() || !numberField.getText().matches("[\\d\\s]*")) {
            numberField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            numberField.setStyle(null); // Remove o estilo de erro se válido
        }

        // Validando telefone
        if (telefoneField.getText().isEmpty() || !telefoneField.getText().matches("(\\d{2})?(\\d{2})\\d{8,9}")) {
            telefoneField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            telefoneField.setStyle(null);
        }

        // Validando CNPJ
        if (CNPJField.getText().isEmpty() || !CNPJField.getText().matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            CNPJField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            CNPJField.setStyle(null);
        }

        // Validando campos de texto
        isValid &= validarCampoTexto(nomeField);
        isValid &= validarCampoTexto(streetField);
        isValid &= validarCampoTexto(neighborhoodField);
        isValid &= validarCampoTexto(cityField);
        isValid &= validarCampoTexto(stateField);

        return isValid;
    }

    private boolean validarCampoTexto(TextField textField) {
        if (textField.getText().isEmpty() || !textField.getText().matches("[\\p{L}\\s]*")) {
            textField.setStyle("-fx-border-color: red;");
            return false;
        } else {
            textField.setStyle(null); // Remove o estilo de erro se válido
            return true;
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
        nomeField.clear();
        telefoneField.clear();
        CNPJField.clear();
        numberField.clear();
        streetField.clear();
        neighborhoodField.clear();
        cityField.clear();
        stateField.clear();
    }

    @FXML
    private void handleCancelar() {
        hideForm();
        hideOverlay();
        listClienteView.setDisable(false);
    }

    @FXML
    private void handleDetailsClienteModal() {
        if (!detailsFormCliente.isVisible()) {
            showModal(detailsFormCliente);
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
    private void handleAddCliente() {
        if (!formCadastroClientes.isVisible()) {
            showOverlay();  
            showForm();
            listClienteView.setDisable(true);
        }
    }

    private void showForm() {
        formCadastroClientes.setManaged(true);
        formCadastroClientes.toFront();
        formCadastroClientes.setVisible(true);
    }

    private void hideForm() {
        formCadastroClientes.setVisible(false);
        formCadastroClientes.setManaged(false);
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
        Clientes clienteSelecionado = (Clientes) tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            try{
                clearFieldForm();
                preencherCampos(clienteSelecionado);
                clienteEditado = clienteSelecionado;
                updateButtonText();
                showOverlay();
                listClienteView.setDisable(true);
                showForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void preencherCampos(Clientes cliente) {
        nomeField.setText(cliente.getName());
        Address clienteAddress = cliente.getAddress();
        if (clienteAddress != null) { // Verifica se o endereço não é nulo
            numberField.setText(clienteAddress.getNumber());
            streetField.setText(clienteAddress.getStreet());
            neighborhoodField.setText(clienteAddress.getNeighborhood());
            cityField.setText(clienteAddress.getCity());
            stateField.setText(clienteAddress.getState());
        } else {
            // Caso o cliente não tenha endereço, limpa os campos relacionados
            numberField.setText("");
            streetField.setText("");
            neighborhoodField.setText("");
            cityField.setText("");
            stateField.setText("");
        }
        telefoneField.setText(cliente.getPhone());
        CNPJField.setText(cliente.getCnpj());
    }

    private void handleInativarCliente() {
        Clientes clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            try{
                String token = TokenManager.getInstance().getToken();
                ClientesService.inativarCliente(clienteSelecionado, token);
                tabelaClientes.getItems().remove(clienteSelecionado);
                clientesObservable.remove(clienteSelecionado);
                
                showSuccessMessageInativarCliente("Cliente inativado com sucesso!");
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Erro ao inativar cliente: " + e.getMessage());
            }            
        }
        else{
            System.out.println("Nenhum cliente selecionado!");
        }
    }
    private void showSuccessMessageInativarCliente(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message); 
        alert.showAndWait();
    }

    private void carregarClientes() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Clientes> todosClientes = clienteService.buscarClientes(token);


            List<Clientes> clientesAtivos = todosClientes.stream()
            .filter(cliente -> cliente.getActive())
            .collect(Collectors.toList());

            totalPaginas = (int) Math.ceil((double) clientesAtivos.size() / itensPorPagina);

            // Filtra os produtos para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, clientesAtivos.size());
            List<Clientes> clientesPagina = clientesAtivos.subList(inicio, fim);

            // Atualiza a lista observável
            clientesObservable.setAll(clientesPagina);
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

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Clientes, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Clientes, String> colunaNumero = new TableColumn<>("Número");
        colunaNumero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getNumber()));

        TableColumn<Clientes, String> colunaRua = new TableColumn<>("Rua");
        colunaRua.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getStreet()));

        TableColumn<Clientes, String> colunaBairro = new TableColumn<>("Bairro");
        colunaBairro.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getNeighborhood()));

        TableColumn<Clientes, String> colunaCidade = new TableColumn<>("Cidade");
        colunaCidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        TableColumn<Clientes, String> colunaEstado = new TableColumn<>("Estado");
        colunaEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getState()));

        TableColumn<Clientes, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Clientes, String> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        tabelaClientes.getColumns().addAll(colunaNome, colunaNumero, colunaRua, colunaBairro, colunaCidade,colunaEstado, colunaTelefone, colunaCnpj);
        tabelaClientes.setItems(clientesObservable);
    }

    /*--------- validações ----------*/
    private void validarCampos() {
        // Validar números
        configurarValidacaoNumerica(numberField);
        configurarValidacaoTelefone(telefoneField);
        configurarValidacaoCNPJ(CNPJField);

        // Validar Textos
        configurarValidacaoTexto(nomeField);
        configurarValidacaoTexto(streetField);
        configurarValidacaoTexto(neighborhoodField);
        configurarValidacaoTexto(cityField);
        configurarValidacaoTexto(stateField);
    }

    private void configurarValidacaoNumerica(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getText().matches(("[\\d\\s]*"))) { // Permite números e espaços
                return change;
            }
            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void configurarValidacaoTelefone(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String novoTexto = change.getControlNewText();

            if (novoTexto.matches("(\\d{2})?(\\d{2})\\d{8,9}")) {
                // Formato: Código do país (2 dígitos) + DDD (2 dígitos) + Número (8 ou 9 dígitos)
                return change;
            }

            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void configurarValidacaoCNPJ(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String novoTexto = change.getControlNewText();

            // Regex para validar o formato de CNPJ
            if (novoTexto.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
                return change; // Aceita formato válido
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


    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }
}