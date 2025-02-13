package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.services.CompraService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Node;

public class DashboardController{
    private final CompraService compraService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox graphicArea;

    @FXML
    private LineChart<String, Number> chartComprasDia;

    @FXML
    private Button btnMaximizeComprasChart;

    public DashboardController() {
        compraService = new CompraService();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        carregarChartCompras();

    }

    private void carregarChartCompras(){
        try {
            List<Compra> compras = compraService.buscarCompras(token);
            Map<LocalDate, BigDecimal> somaDiaria = new TreeMap<>();
            for(Compra compra: compras){
                LocalDate date;

                if( compra.getEntryAt() != null){
                    date = compra.getEntryAt().toLocalDate();  
                } else{
                    continue;
                }

                BigDecimal somaAtual = somaDiaria.getOrDefault(date, BigDecimal.ZERO);
                somaDiaria.put(date, somaAtual.add(compra.getTotalAmount()));
                System.out.println("soma diaria" + somaDiaria);
            }

            //Série de dados para o gráfico
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Compras por dia");

            //adiciona os dados agrupados à série
            for (Map.Entry<LocalDate, BigDecimal> entry : somaDiaria.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            //atualiza o gráfico
            chartComprasDia.getData().clear();
            chartComprasDia.getData().add(series);

            // Para cada ponto da série, cria um nó customizado que exibe o valor acima do marcador
            Platform.runLater(() -> refreshCustomNodes(chartComprasDia));
            Platform.runLater(() -> chartComprasDia.layout());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o gráfico de compras: " + e.getMessage());
        }
    }

    private void maximizeChart(Chart chart, String chartTitle) {
        final VBox graphicAreaFinal = (VBox) rootPane.lookup("#graphicArea");
        // Guarda o conteúdo original para restaurar depois
        List<javafx.scene.Node> originalContent = new ArrayList<>(graphicAreaFinal.getChildren());
        
        // Limpa e configura a área maximizada
        graphicAreaFinal.getChildren().clear();
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label(chartTitle);
        Button restoreButton = new Button("Restaurar");
        restoreButton.setOnAction(e -> {
            graphicAreaFinal.getChildren().clear();
            graphicAreaFinal.getChildren().addAll(originalContent);
        });
        header.getChildren().addAll(title, restoreButton);
        
        // Faz o gráfico ocupar todo o espaço disponível da área
        chart.prefWidthProperty().bind(graphicAreaFinal.widthProperty());
        chart.prefHeightProperty().bind(graphicAreaFinal.heightProperty());
        
        // Adiciona o cabeçalho e o gráfico à área
        graphicAreaFinal.getChildren().addAll(header, chart);
        
        // Aplica as regras de CSS e força o layout para atualizar o redimensionamento
        chart.applyCss();
        chart.layout();
        
        // Reaplica os nós customizados (rótulos e marcadores) se o gráfico for um XYChart
        Platform.runLater(() -> refreshCustomNodes(chart));
    }

    @SuppressWarnings("unchecked")
    private void refreshCustomNodes(Chart chart) {
        if (chart instanceof javafx.scene.chart.XYChart) {
            javafx.scene.chart.XYChart<String, Number> xyChart = (javafx.scene.chart.XYChart<String, Number>) chart;

            for (XYChart.Series<String, Number> series : xyChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {

                    // Cria um rótulo com o valor do ponto
                    Label valueLabel = new Label(data.getYValue().toString());
                    valueLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: black;");

                    // Cria o marcador do ponto
                    Circle marker = new Circle(5, Color.web("#2a9fd6"));

                    // Empilha o marcador e o rótulo no mesmo local
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(marker, valueLabel);
                    stack.setAlignment(Pos.TOP_CENTER);

                    // Adiciona o nó ao ponto do gráfico
                    data.setNode(stack);

                    // Atualiza o rótulo se o valor mudar
                    data.YValueProperty().addListener((obs, oldVal, newVal) -> {
                        valueLabel.setText(newVal.toString());
                    });
                }
            }
        }
    }


    @FXML
    private void handleComprasChartMaximization() {
        maximizeChart(chartComprasDia, "Compras do Mês");
    }

//    @FXML
//    private void handleVendasChartMaximization() {
//        maximizeChart(chartVendas, "Vendas do Mês");
//    }



}
