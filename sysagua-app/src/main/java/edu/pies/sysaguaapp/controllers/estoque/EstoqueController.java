package edu.pies.sysaguaapp.controllers.estoque;

import edu.pies.sysaguaapp.models.Estoque;
import edu.pies.sysaguaapp.services.EstoqueService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.util.List;

public class EstoqueController {
    private final EstoqueService estoqueService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Estoque> tabelaEstoque;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnProximo;

    @FXML
    private Button btnAjustarEstoque;

    @FXML
    private Button btnMovimentacoes;

    @FXML
    private Button btnConfiguracoes;

    @FXML
    private TextField nomeField;

    private ObservableList<Estoque> estoqueObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public EstoqueController() {
        this.estoqueService = new EstoqueService();
        this.estoqueObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProdutos();

        handleMovimentacoes();
        handleConfiguracoes();
    }

    private void configurarTabela() {
        TableColumn<Estoque, String> colunaProduto = new TableColumn<>("Produto");
        colunaProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getName()));

        TableColumn<Estoque, BigDecimal> colunaCusto = new TableColumn<>("Custo");
        colunaCusto.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Estoque, Integer> colunaQuantidade = new TableColumn<>("Quantidade");
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tabelaEstoque.getColumns().addAll(colunaProduto, colunaCusto, colunaQuantidade);
        tabelaEstoque.setItems(estoqueObservable);
    }

    private void carregarProdutos() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Estoque> todosEstoque = estoqueService.buscarEstoque(token);

            totalPaginas = (int) Math.ceil((double) todosEstoque.size() / itensPorPagina);
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosEstoque.size());
            List<Estoque> estoquePagina = todosEstoque.subList(inicio, fim);

            estoqueObservable.setAll(estoquePagina);

            atualizarEstadoBotoes();
            atualizarPaginacao();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar estoque: " + e.getMessage());
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

    @FXML
    private void handleAjustarEstoque(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Estoque/AddProduto.fxml"));
            Parent addProduto = loader.load();
    
            rootPane.getChildren().clear();
            rootPane.getChildren().add(addProduto);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tela de adicionar produto: " + e.getMessage());
        }
    }

    @FXML
    private void handleMovimentacoes() {
        btnMovimentacoes.setOnAction(event -> {
            // Implementar
            System.out.println("Movimentações");
        });
    }

    @FXML
    private void handleConfiguracoes() {
        btnConfiguracoes.setOnAction(event -> {
            // Implementar
            System.out.println("Configurações");
        });
    }
}
