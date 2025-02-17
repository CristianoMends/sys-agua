package edu.pies.sysaguaapp.controllers.Pedidos;


import edu.pies.sysaguaapp.dtos.pedido.ItemPedidoDto;
import edu.pies.sysaguaapp.dtos.pedido.SendPedidoDto;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Estoque;
import edu.pies.sysaguaapp.models.Pedido.ItemPedido;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AddPedidoController {

    private final PedidoService pedidoService;
    private final ClientesService clientesService;

    private final EntregadorService entregadorService;

    private final EstoqueService estoqueService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private DatePicker dataPedido;

    @FXML
    private TextField valorRecebidoField, precoField, quantidadeField;

    @FXML
    private Label valorRecebidoErrorLabel;

    @FXML
    private ComboBox<Clientes> clientesComboBox;

    @FXML
    private ComboBox<Produto> produtoComboBox;

    @FXML
    private ComboBox<Entregador> entregadorComboBox;

    @FXML
    private ComboBox<PaymentMethod> metodoPagamento;

    @FXML
    private ObservableList<ItemPedido> produtosAddList;

    @FXML
    private TableColumn<ItemPedido, String> produtoColumn, precoColumn, codigoColumn;

    @FXML
    private TableColumn<ItemPedido, Integer> quantidadeColumn;

    @FXML
    private Label dataErrorLabel, fornecedorErrorLabel, precoErrorLabel, statusErrorLabel, produtoErrorLabel,clientesErrorLabel, quantidadeErrorLabel, entregadorErrorLabel;

    @FXML
    private TableView<ItemPedido> adicionadosTableView;

    @FXML
    private Label totalLabel, totalItensLabel, saveErrorLabel, duplicadoErrorLabel;

    @FXML
    private Button btnSalvar, btnCancelar,  btnInserir;

    private ItemPedido itemEditando = null;

    private final ContextMenu contextMenu = new ContextMenu();


    public AddPedidoController() {
        entregadorService = new EntregadorService();
        estoqueService = new EstoqueService();
        pedidoService = new PedidoService();
        clientesService = new ClientesService();
        produtosAddList = FXCollections.observableArrayList();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        validarCampos();
        carregarListaProdutos();
        carregarListaClientes();
        carregarListaEntregadores();

        btnSalvar.setCursor(Cursor.HAND);
        btnInserir.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);

        adicionadosTableView.setItems(produtosAddList);
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getId().toString()));
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        precoColumn.setCellValueFactory(cellData -> {
            BigDecimal preco = cellData.getValue().getPurchasePrice();
            return new SimpleStringProperty(preco != null ? "R$ " + preco.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });

        produtosAddList.addListener((ListChangeListener.Change<? extends ItemPedido> change) -> {
            atualizarTotais();
        });

        metodoPagamento.setItems(FXCollections.observableArrayList(PaymentMethod.values()));
        metodoPagamento.setConverter(new StringConverter<PaymentMethod>() {
            @Override
            public String toString(PaymentMethod metodo) {
                return metodo != null ? metodo.getDescription() : "";
            }
            @Override
            public PaymentMethod fromString(String string) {
                for (PaymentMethod metodo : PaymentMethod.values()) {
                    if (metodo.getDescription().equals(string)) {
                        return metodo;
                    }
                }
                return null;
            }
        });
        atualizarTotais();
        showMenuContext();
    }

    @FXML
    private void handleInserir() {
        if (validarFormItem()) {
            ItemPedido novoItemPedido = criarItemPedido();

            if (novoItemPedido != null && novoItemPedido.getProduct() != null) {
                if (itemEditando != null) {
                    int index = produtosAddList.indexOf(itemEditando);
                    if (index != -1) {
                        produtosAddList.set(index, novoItemPedido); // Substitui o item
                    }
                    itemEditando = null;
                } else {
                    produtosAddList.add(novoItemPedido);
                }
                clearProdutoForm();
            }
        }
    }


    private ItemPedido criarItemPedido() {
        Produto produtoSelecionado = produtoComboBox.getValue();
        BigDecimal preco = new BigDecimal(precoField.getText().replace(",", "."));
        int quantidade = Integer.parseInt(quantidadeField.getText());

        ItemPedido novoItemPedido = new ItemPedido();

        if (produtoSelecionado != null) {
            novoItemPedido.setProduct(produtoSelecionado);
            novoItemPedido.setQuantity(quantidade);
            novoItemPedido.setPurchasePrice(preco);
        }

        return novoItemPedido;
    }

    @FXML
    private void handleSalvar() {

        if (validarFormPedido()){
            try {
                SendPedidoDto novoPedido = new SendPedidoDto();
                novoPedido.setCustomerId(clientesComboBox.getValue().getId());
                novoPedido.setDeliveryPersonId(entregadorComboBox.getValue().getId());
                novoPedido.setReceivedAmount(BigDecimal.ZERO);
                novoPedido.setTotalAmount(BigDecimal.ZERO);
                novoPedido.setPaymentMethod(metodoPagamento.getValue());
                novoPedido.setDescription("Sem descrição");

                List<ItemPedidoDto> itensDto = produtosAddList.stream()
                        .map(item -> {
                            ItemPedidoDto dto = new ItemPedidoDto();
                            dto.setProductId(item.getProduct().getId());
                            dto.setQuantity(item.getQuantity());
                            dto.setPurchasePrice(item.getPurchasePrice());
                            return dto;
                        })
                        .collect(Collectors.toList());

                novoPedido.setProductOrders(itensDto);

                pedidoService.criarPedido(novoPedido, token);

                clearAllForm();
                carregarTela("/views/Pedido/Pedido.fxml");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao cadastrar pedido" + e.getMessage());
            }

        }
    }


    @FXML
    private void handleCancelar() {
        carregarTela("/views/Pedidos/Pedido.fxml");
    }

    private void showMenuContext() {
        if (contextMenu.getItems().isEmpty()) {
            MenuItem editarLancamento = new MenuItem("Editar lançamento");
            MenuItem removerLancamento = new MenuItem("Remover lançamento");
            editarLancamento.setOnAction(event -> handleEditarLancamento());
            removerLancamento.setOnAction(event -> handleRemoverLancamento());

            contextMenu.getItems().addAll(editarLancamento, removerLancamento);
        }

        adicionadosTableView.setRowFactory(tv -> {
            TableRow<ItemPedido> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    private void handleEditarLancamento() {
        ItemPedido produtoSelecionado = adicionadosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            itemEditando = produtoSelecionado;
            preencherCampos(produtoSelecionado);
        }
    }

    private void preencherCampos(ItemPedido itemSelecionado) {
        produtoComboBox.setValue(itemSelecionado.getProduct());
        precoField.setText(itemSelecionado.getPurchasePrice().toString().replace(".",","));
        quantidadeField.setText(itemSelecionado.getQuantity().toString());
    }

    private void handleRemoverLancamento(){
        ItemPedido produtoSelecionado = adicionadosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            produtosAddList.remove(produtoSelecionado);
        }
        adicionadosTableView.refresh();
    }


    //------------------------- carregar itens -------------------------//

    private void carregarTela(String caminho) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent addProduto = loader.load();
            rootPane.getChildren().clear();
            rootPane.getChildren().add(addProduto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tela: " + e.getMessage());
        }
    }

    private void carregarListaProdutos(){

        try {
            List<Produto> produtos = estoqueService.buscarEstoque(token).stream()
                    .map(Estoque::getProduct)
                    .filter(produto -> produto != null && produto.isActive())
                    .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
                    .collect(Collectors.toList());

            ObservableList<Produto> observableProdutos = FXCollections.observableArrayList(produtos);

            produtoComboBox.setItems(observableProdutos);

            produtoComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Produto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });

            produtoComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Produto item, boolean empty) {
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

    private void carregarListaClientes(){

        try {
            List<Clientes> clientes = clientesService.buscarClientes(token).stream()
                    .filter(Clientes::getActive)
                    .sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
                    .collect(Collectors.toList());
            clientesComboBox.setItems(FXCollections.observableArrayList(clientes));

            clientesComboBox.setCellFactory(param -> new ListCell<Clientes>() {
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

            clientesComboBox.setButtonCell(new ListCell<Clientes>() {
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
            entregadorComboBox.setItems(FXCollections.observableArrayList(entregadores));

            entregadorComboBox.setCellFactory(param -> new ListCell<Entregador>() {
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

            entregadorComboBox.setButtonCell(new ListCell<Entregador>() {
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

    private void atualizarTotais() {
        BigDecimal total = produtosAddList.stream()
                .map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itens = produtosAddList.size();

        totalLabel.setText("R$ " + total.setScale(2, RoundingMode.HALF_UP));
        totalItensLabel.setText(String.valueOf(itens));

    }

    //--------------------------- validações -------------------------//

    private boolean validarFormItem() {
        boolean isValid = true;
        Produto produto = produtoComboBox.getValue();

        if (produto == null || produto.toString().isEmpty()) {
            produtoErrorLabel.setText("Produto é obrigatória.");
            produtoErrorLabel.setVisible(true);
            produtoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            produtoErrorLabel.setVisible(false);
            produtoErrorLabel.setManaged(false);
        }

        if (precoField.getText().trim().isEmpty()) {
            precoErrorLabel.setText("Preço é obrigatória.");
            precoErrorLabel.setVisible(true);
            precoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            precoErrorLabel.setVisible(false);
            precoErrorLabel.setManaged(false);
        }

        if (quantidadeField.getText().trim().isEmpty()) {
            quantidadeErrorLabel.setText("Quantidade é obrigatório.");
            quantidadeErrorLabel.setVisible(true);
            quantidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            quantidadeErrorLabel.setVisible(false);
            quantidadeErrorLabel.setManaged(false);
        }

        BigDecimal preco = new BigDecimal(precoField.getText().replace(",", "."));

        if (preco.compareTo(BigDecimal.ZERO) == 0) {
            precoErrorLabel.setText("Campo não pode ser zero.");
            precoErrorLabel.setVisible(true);
            precoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            precoErrorLabel.setVisible(false);
            precoErrorLabel.setManaged(false);
        }

        if (Integer.parseInt(quantidadeField.getText().trim()) == 0) {
            quantidadeErrorLabel.setText("Campo não pode ser zero.");
            quantidadeErrorLabel.setVisible(true);
            quantidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            quantidadeErrorLabel.setVisible(false);
            quantidadeErrorLabel.setManaged(false);
        }


        if (produtoExiste(produto)) {
            if (itemEditando == null || produto != itemEditando.getProduct()) {
                duplicadoErrorLabel.setVisible(true);
                duplicadoErrorLabel.setManaged(true);
                isValid = false;
            } else {
                duplicadoErrorLabel.setVisible(false);
                duplicadoErrorLabel.setManaged(false);
            }
        } else {
            duplicadoErrorLabel.setVisible(false);
            duplicadoErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private boolean produtoExiste(Produto produto) {
        return produtosAddList.stream()
                .anyMatch(item -> item.getProduct().getId().equals(produto.getId()));
    }

    private boolean validarFormPedido() {
        boolean isValid = true;

        if (clientesComboBox.getValue() == null) {
            clientesErrorLabel.setText("Cliente é obrigatório.");
            clientesErrorLabel.setVisible(true);
            clientesErrorLabel.setManaged(true);
            isValid = false;
        } else {
            clientesErrorLabel.setVisible(false);
            clientesErrorLabel.setManaged(false);
        }

        if (produtosAddList.isEmpty()){
            saveErrorLabel.setVisible(true);
            saveErrorLabel.setManaged(true);
            isValid = false;
        } else {
            saveErrorLabel.setVisible(false);
            saveErrorLabel.setManaged(false);
        }

        if (entregadorComboBox.getValue() == null || entregadorComboBox.getValue().toString().isEmpty()) {
            entregadorErrorLabel.setText("Entregador é obrigatório.");
            entregadorErrorLabel.setVisible(true);
            entregadorErrorLabel.setManaged(true);
            isValid = false;
        } else {
            entregadorErrorLabel.setVisible(false);
            entregadorErrorLabel.setManaged(false);
        }

        if (dataPedido.getValue() == null) {
            dataErrorLabel.setText("Data é obrigatória.");
            dataErrorLabel.setVisible(true);
            dataErrorLabel.setManaged(true);
            isValid = false;
        } else if (dataPedido.getValue().isAfter(LocalDate.now())) {
            dataErrorLabel.setText("Data do Pedido não pode ser uma data futura.");
            dataErrorLabel.setVisible(true);
            dataErrorLabel.setManaged(true);
            isValid = false;
        } else {
            dataErrorLabel.setVisible(false);
            dataErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void validarCampos() {
        // Validar números
        configurarValidacaoDinheiro(precoField, precoErrorLabel);
        configurarValidacaoNumerica(quantidadeField, quantidadeErrorLabel);
    }


    private void configurarValidacaoNumerica(TextField textField, Label errorLabel) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")){
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

    private void configurarValidacaoDinheiro(TextField textField, Label errorLabel) {
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
    private void clearProdutoForm(){
        produtoComboBox.getSelectionModel().clearSelection();
        precoField.clear();
        quantidadeField.clear();
    }

    private void clearAllForm(){
        clientesComboBox.getSelectionModel().clearSelection();
        produtoComboBox.getSelectionModel().clearSelection();
        entregadorComboBox.getSelectionModel().clearSelection();
        precoField.clear();
        quantidadeField.clear();
    }
}
