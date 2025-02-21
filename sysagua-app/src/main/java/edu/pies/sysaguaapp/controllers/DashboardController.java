package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Pedido.ItemPedido;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.produto.Produto;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.services.compra.CompraService;
import edu.pies.sysaguaapp.services.pedido.PedidoService;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.utils.ChartUtilsPie;
import edu.pies.sysaguaapp.utils.ChartUtilsXY;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class DashboardController{
    private final CompraService compraService;
    private final PedidoService pedidoService;
    private final String token;

    @FXML
    private StackPane rootPane;

    private StackPane overlayPane;

    @FXML
    private VBox graphicArea, firstGraph;

    @FXML
    private LineChart<String, Number> chartComprasDia;

    @FXML
    private PieChart chartComprasBubble, chartVendasLinha;

    @FXML
    private BarChart<String, Number> chartVendasDia;
    
    @FXML
    private ListView<String> listPrincipaisClientes;

    @FXML
    private DatePicker dateInicio, dateFim;

    @FXML
    private Label totalReceitasMes, percentualReceitasMes;

    @FXML
    private Button btnAmpliarFirstGraph, btnAmpliarSecondGraph,btnAmpliarThirdGraph, btnAmpliarFifthGraph;

    @FXML
    private HBox firstLineGraph;

    public DashboardController() {
        compraService = new CompraService();
        pedidoService = new PedidoService();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDiaMes = hoje.withDayOfMonth(1);
        dateInicio.setValue(primeiroDiaMes);
        dateFim.setValue(hoje);

        dateInicio.setOnAction(e -> filtrarGraficos());
        dateFim.setOnAction(e -> filtrarGraficos());

        carregarChartCompras();
        carregarChartComprasBubble();
        carregarChartVendasLinha();
        atualizarListaPrincipaisClientes();
        configurarLista();
        carregarChartPedidoDia();
        carregarValores();

        firstLineGraph.prefHeightProperty().bind(graphicArea.heightProperty().multiply(0.5));
        firstGraph.prefWidthProperty().bind(firstLineGraph.widthProperty().multiply(0.5));
        btnAmpliarFirstGraph.setCursor(Cursor.HAND);
        btnAmpliarSecondGraph.setCursor(Cursor.HAND);
        btnAmpliarThirdGraph.setCursor(Cursor.HAND);
        btnAmpliarFifthGraph.setCursor(Cursor.HAND);

    }


    private void configurarLista() {
        listPrincipaisClientes.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        FadeTransition ft = new FadeTransition(Duration.millis(500), this);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.playFromStart();
                    }
                }
            };
            return cell;
        });
    }

    private void filtrarGraficos() {
        LocalDate inicio = dateInicio.getValue();
        LocalDate fim = dateFim.getValue();
        if (inicio != null && fim != null) {
            if (inicio.isAfter(fim)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Intervalo de Datas Inválido");
                alert.setHeaderText(null);
                alert.setContentText("A data de início não pode ser maior que a data de fim.");
                alert.showAndWait();
                return;
            }
            // Recarrega os gráficos filtrados
            carregarChartCompras();
            carregarChartComprasBubble();
            carregarChartVendasLinha();
            carregarChartPedidoDia();
        }
    }

    private void carregarChartPedidoDia() {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidos(token);
            LocalDate inicio = dateInicio.getValue();
            LocalDate fim = dateFim.getValue();
    
            // Calcula o período do mês anterior com base no filtro
            LocalDate inicioAnterior = inicio.minusMonths(1);
            LocalDate fimAnterior = fim.minusMonths(1);
    
            // Inicializa os mapas para armazenar os valores para cada dia dos intervalos
            // Usa os mesmos dias (números) do filtro para ambas as séries
            Map<Integer, BigDecimal> somaPorDiaAtual = new TreeMap<>();
            Map<Integer, BigDecimal> somaPorDiaAnterior = new TreeMap<>();
    
            // Preenche os mapas com os dias do intervalo (dia do mês) com valor zero
            for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
                int dia = data.getDayOfMonth();
                somaPorDiaAtual.put(dia, BigDecimal.ZERO);
                somaPorDiaAnterior.put(dia, BigDecimal.ZERO);
            }
    
            // Agrega os valores das compras no período atual
            for (Pedido pedido: pedidos) {
                if (pedido.getCreatedAt() == null) continue;
                LocalDate dataPedido = pedido.getCreatedAt().toLocalDate();
                // Mês atual: data da pedido deve estar entre inicio e fim
                if (!dataPedido.isBefore(inicio) && !dataPedido.isAfter(fim)) {
                    int dia = dataPedido.getDayOfMonth();
                    if (somaPorDiaAtual.containsKey(dia)) {
                        BigDecimal somaAtual = somaPorDiaAtual.get(dia);
                        somaPorDiaAtual.put(dia, somaAtual.add(pedido.getTotalAmount()));
                    }
                }
                // Mês anterior: data da pedido deve estar entre inicioAnterior e fimAnterior
                if (!dataPedido.isBefore(inicioAnterior) && !dataPedido.isAfter(fimAnterior)) {
                    int dia = dataPedido.getDayOfMonth();
                    // Apenas acrescente se o dia também estiver dentro do intervalo filtrado
                    if (somaPorDiaAnterior.containsKey(dia)) {
                        BigDecimal somaAnterior = somaPorDiaAnterior.get(dia);
                        somaPorDiaAnterior.put(dia, somaAnterior.add(pedido.getTotalAmount()));
                    }
                }
            }
    
            // Cria as séries para cada período, garantindo a ordem crescente dos dias
            XYChart.Series<String, Number> seriesAtual = new XYChart.Series<>();
            seriesAtual.setName("Mês Atual");
            XYChart.Series<String, Number> seriesAnterior = new XYChart.Series<>();
            seriesAnterior.setName("Mês Anterior");
    
            for (int dia : somaPorDiaAtual.keySet()) {
                seriesAtual.getData().add(new XYChart.Data<>(String.valueOf(dia), somaPorDiaAtual.get(dia)));
                seriesAnterior.getData().add(new XYChart.Data<>(String.valueOf(dia), somaPorDiaAnterior.get(dia)));
            }
    
            chartVendasDia.getData().clear();
            chartVendasDia.getData().addAll(seriesAnterior, seriesAtual);
            Platform.runLater(() -> {
                chartVendasDia.layout();
                ChartUtilsXY.refreshCustomNodes(chartVendasDia);
            });
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o gráfico de pedidos por dia: " + e.getMessage());
        }
    }

    private void carregarChartVendasLinha() {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidos(token);
            LocalDate inicio = dateInicio.getValue();
            LocalDate fim = dateFim.getValue();
            // Mapa para acumular a quantidade vendida para cada linha de produto filtrando pela data de entrada
            Map<String, Integer> vendasPorLinha = new TreeMap<>();
            for (Pedido pedido : pedidos) {
                if (pedido.getProductOrders() == null || pedido.getCreatedAt() == null) continue;
                LocalDate dataPedido = pedido.getCreatedAt().toLocalDate();
                if (dataPedido.isBefore(inicio) || dataPedido.isAfter(fim)) continue;
                for (ItemPedido item : pedido.getProductOrders()) {
                    Produto produto = item.getProduct();
                    if (produto == null || produto.getLine() == null) continue;
                    String nomeLinha = produto.getLine().getName();
                    int quantidade = item.getQuantity();
                    vendasPorLinha.put(nomeLinha, vendasPorLinha.getOrDefault(nomeLinha, 0) + quantidade);
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : vendasPorLinha.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            chartVendasLinha.setData(pieChartData);
            Platform.runLater(() -> {
                updatePieChartLegend(chartVendasLinha);
                ChartUtilsXY.refreshCustomNodes(chartVendasLinha);
                chartVendasLinha.layout();
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o gráfico de vendas por linha: " + e.getMessage());
        }
    }

    private void carregarChartComprasBubble() {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidos(token);
            LocalDate inicio = dateInicio.getValue();
            LocalDate fim = dateFim.getValue();
            // Mapa para acumular a quantidade vendida para cada produto filtrando pela data de entrada
            Map<String, Integer> produtosVendas = new TreeMap<>();
            for (Pedido pedido : pedidos) {
                if (pedido.getProductOrders() == null || pedido.getCreatedAt() == null)
                    continue;
                LocalDate dataCompra = pedido.getCreatedAt().toLocalDate();
                if (dataCompra.isBefore(inicio) || dataCompra.isAfter(fim))
                    continue;
                for (ItemPedido item : pedido.getProductOrders()) {
                    if (item.getProduct() == null) continue;
                    String nomeProduto = item.getProduct().getName();
                    int quantidade = item.getQuantity();
                    produtosVendas.put(nomeProduto, produtosVendas.getOrDefault(nomeProduto, 0) + quantidade);
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : produtosVendas.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            chartComprasBubble.setData(pieChartData);
            Platform.runLater(() -> {
                updatePieChartLegend(chartComprasBubble);
                ChartUtilsXY.refreshCustomNodes(chartComprasBubble);
                chartComprasBubble.layout();
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o gráfico de pizza: " + e.getMessage());
        }
    }

    private void updatePieChartLegend(PieChart pieChart) {
        double total = 0;
        for (PieChart.Data data : pieChart.getData()) {
            total += data.getPieValue();
        }

        for (PieChart.Data data : pieChart.getData()) {
            double percent = (data.getPieValue() / total) * 100;

            String originalName = data.getName().split("\n")[0];
            String newName = originalName + "\n" + ((int)data.getPieValue()) + " (" + String.format("%.2f", percent) + "%)";
            data.setName(newName);
        }
    }

    private void atualizarListaPrincipaisClientes() {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidos(token);
            LocalDate inicio = dateInicio.getValue();
            LocalDate fim = dateFim.getValue();
            // Agrupa os totais de compras por fornecedor filtrando pela data de entrada
            Map<String, BigDecimal> fornecedorTotals = new HashMap<>();
            for (Pedido pedido : pedidos) {
                if (pedido.getCustomer() == null || pedido.getCreatedAt() == null) continue;
                LocalDate dataCompra = pedido.getCreatedAt().toLocalDate();
                if (dataCompra.isBefore(inicio) || dataCompra.isAfter(fim)) continue;
                String supplierName = pedido.getCustomer().getName();
                BigDecimal totalCompra = pedido.getTotalAmount();
                BigDecimal totalAtual = fornecedorTotals.getOrDefault(supplierName, BigDecimal.ZERO);
                fornecedorTotals.put(supplierName, totalAtual.add(totalCompra));
            }
            
            // Ordena os fornecedores pelo total decrescente
            List<Map.Entry<String, BigDecimal>> sortedList = new ArrayList<>(fornecedorTotals.entrySet());
            sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
            
            int topN = Math.min(10, sortedList.size());
            ObservableList<String> items = FXCollections.observableArrayList();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            for (int i = 0; i < topN; i++) {
                Map.Entry<String, BigDecimal> entry = sortedList.get(i);
                String formattedValue = formatter.format(entry.getValue());
                String emoji = (i == 0) ? " 🏆" : "";
                items.add((i + 1) + ". " + entry.getKey() + " - " + formattedValue + emoji);
            }
            listPrincipaisClientes.setItems(items);
            listPrincipaisClientes.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar lista dos principais fornecedores: " + e.getMessage());
        }
    }

    private void carregarChartCompras() {
        try {
            List<Compra> compras = compraService.buscarCompras(token);
            LocalDate inicio = dateInicio.getValue();
            LocalDate fim = dateFim.getValue();
            
            // Calcula o intervalo do período anterior com base no filtro
            LocalDate inicioAnterior = inicio.minusMonths(1);
            LocalDate fimAnterior = fim.minusMonths(1);
            
            // Inicializa os mapas para armazenar as somas diárias para os dois períodos
            // A chave é o dia do mês (número inteiro) e inicializamos com valor zero
            Map<Integer, BigDecimal> somaDiariaAtual = new TreeMap<>();
            Map<Integer, BigDecimal> somaDiariaAnterior = new TreeMap<>();
            
            for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
                int dia = data.getDayOfMonth();
                somaDiariaAtual.put(dia, BigDecimal.ZERO);
                somaDiariaAnterior.put(dia, BigDecimal.ZERO);
            }
            
            // Agrega os valores das compras nos respectivos períodos
            for (Compra compra : compras) {
                if (compra.getEntryAt() == null) continue;
                LocalDate dataCompra = compra.getEntryAt().toLocalDate();
                int dia = dataCompra.getDayOfMonth();
                
                // Verifica se a data da compra está dentro do período atual
                if (!dataCompra.isBefore(inicio) && !dataCompra.isAfter(fim)) {
                    if (somaDiariaAtual.containsKey(dia)) {
                        BigDecimal somaAtual = somaDiariaAtual.get(dia);
                        somaDiariaAtual.put(dia, somaAtual.add(compra.getTotalAmount()));
                    }
                }
                // Para o período anterior, verifica se a compra ocorreu no mesmo dia (número) dentro do período anterior
                else if (!dataCompra.isBefore(inicioAnterior) && !dataCompra.isAfter(fimAnterior)) {
                    if (somaDiariaAnterior.containsKey(dia)) {
                        BigDecimal somaAnterior = somaDiariaAnterior.get(dia);
                        somaDiariaAnterior.put(dia, somaAnterior.add(compra.getTotalAmount()));
                    }
                }
            }
            
            // Cria as séries para o gráfico de linhas
            XYChart.Series<String, Number> seriesAtual = new XYChart.Series<>();
            seriesAtual.setName("Mês Atual");
            XYChart.Series<String, Number> seriesAnterior = new XYChart.Series<>();
            seriesAnterior.setName("Mês Anterior");
            
            for (Integer dia : somaDiariaAtual.keySet()) {
                seriesAtual.getData().add(new XYChart.Data<>(String.valueOf(dia), somaDiariaAtual.get(dia)));
                seriesAnterior.getData().add(new XYChart.Data<>(String.valueOf(dia), somaDiariaAnterior.get(dia)));
            }
            
            chartComprasDia.getData().clear();
            chartComprasDia.getData().addAll(seriesAnterior, seriesAtual);
            ChartUtilsXY.refreshCustomNodes(chartComprasDia);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o gráfico de compras: " + e.getMessage());
        }
    }

    @FXML
    private void handleProdutosChartMaximization() {
        ChartUtilsPie.maximizeChart(chartComprasBubble, "Produtos mais vendidos", rootPane);
    }

    @FXML
    private void handleLineChartMaximization() {
        ChartUtilsPie.maximizeChart(chartVendasLinha, "Linha de produtos mais vendidos", rootPane);
    }

    @FXML
    private void handlePedidosDiaChartMax() {
        ChartUtilsXY.maximizeChart(chartVendasDia, "Vendas por dia", rootPane);
    }

    @FXML
    private void handleInitialize(){
        initialize();
    }

    @FXML
    private void handleComprasChartMaximization() {
        ChartUtilsXY.maximizeChart(chartComprasDia, "Compras do Mês", rootPane);
    }

    private void carregarValores() {
        BigDecimal totalReceitasMesAtual = BigDecimal.ZERO;
        BigDecimal totalReceitasMesAnterior = BigDecimal.ZERO;

        try {
            List<Pedido> pedidos = pedidoService.buscarPedidos(token);
            LocalDate primeiroDiaMesAtual = LocalDate.now().withDayOfMonth(1);
            LocalDate primeiroDiaMesAnterior = primeiroDiaMesAtual.minusMonths(1);

            for (Pedido pedido : pedidos) {
                LocalDate dataPedido = pedido.getCreatedAt().toLocalDate();
                BigDecimal totalPedido = pedido.getTotalAmount();

                if (!pedido.getDeliveryStatus().equals(PedidoStatus.CANCELED)) {
                    if (dataPedido.isAfter(primeiroDiaMesAtual.minusDays(1))) {
                        totalReceitasMesAtual = totalReceitasMesAtual.add(totalPedido);
                    } else if (dataPedido.isAfter(primeiroDiaMesAnterior.minusDays(1)) && dataPedido.isBefore(primeiroDiaMesAtual)) {
                        totalReceitasMesAnterior = totalReceitasMesAnterior.add(totalPedido);
                    }
                }
            }

            totalReceitasMes.setText("R$ " + totalReceitasMesAtual.setScale(2, RoundingMode.HALF_UP));

            // Calcula a variação percentual
            BigDecimal percentual = BigDecimal.ZERO;
            if (totalReceitasMesAnterior.compareTo(BigDecimal.ZERO) > 0) {
                percentual = totalReceitasMesAtual.subtract(totalReceitasMesAnterior)
                        .divide(totalReceitasMesAnterior, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // Define cor e emoji da variação
            String cor = (percentual.compareTo(BigDecimal.ZERO) >= 0) ? "green" : "red";
            String emoji = (percentual.compareTo(BigDecimal.ZERO) >= 0) ? "🔺" : "🔻";

            percentualReceitasMes.setStyle("-fx-text-fill: " + cor);
            percentualReceitasMes.setText(String.format("%.2f%% %s", percentual.setScale(2, RoundingMode.HALF_UP), emoji));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
