package edu.pies.sysaguaapp.utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.util.Locale;

public class ChartUtilsXY {

    @SuppressWarnings("unchecked")
    public static <T extends XYChart<String, Number>> T cloneXYChart(T original) {
        if (original instanceof LineChart) {
            // Cria novos eixos para o gráfico de linhas
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            LineChart<String, Number> cloned = new LineChart<>(xAxis, yAxis);
            cloned.setTitle(original.getTitle());

            // Clona as séries de dados
            for (XYChart.Series<String, Number> series : original.getData()) {
                XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
                newSeries.setName(series.getName());
                for (XYChart.Data<String, Number> data : series.getData()) {
                    newSeries.getData().add(new XYChart.Data<>(data.getXValue(), data.getYValue()));
                }
                cloned.getData().add(newSeries);
            }
            return (T) cloned;
        } else if (original instanceof BarChart) {
            // Cria novos eixos para o gráfico de barras
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> cloned = new BarChart<>(xAxis, yAxis);
            cloned.setTitle(original.getTitle());

            // Clona as séries de dados
            for (XYChart.Series<String, Number> series : original.getData()) {
                XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
                newSeries.setName(series.getName());
                for (XYChart.Data<String, Number> data : series.getData()) {
                    newSeries.getData().add(new XYChart.Data<>(data.getXValue(), data.getYValue()));
                }
                cloned.getData().add(newSeries);
            }
            return (T) cloned;
        } else {
            throw new UnsupportedOperationException("Clone não implementado para este tipo de gráfico.");
        }
    }


    @SuppressWarnings("unchecked")
    public static void maximizeChart(Chart originalChart, String chartTitle, StackPane rootPane) {
        Chart displayChart;
        if (originalChart instanceof XYChart) {
            displayChart = cloneXYChart((XYChart<String, Number>) originalChart);
        } else {
            throw new UnsupportedOperationException("Maximização está implementada apenas para gráficos do tipo XYChart.");
        }

        StackPane overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        overlayPane.prefWidthProperty().bind(rootPane.widthProperty());
        overlayPane.prefHeightProperty().bind(rootPane.heightProperty());

        VBox maximizedContent = new VBox(10);
        maximizedContent.setStyle("-fx-background-color: white; -fx-padding: 10;");
        maximizedContent.prefWidthProperty().bind(overlayPane.widthProperty().subtract(40));
        maximizedContent.prefHeightProperty().bind(overlayPane.heightProperty().subtract(40));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(chartTitle);
        Button restoreButton = new Button("Restaurar");
        restoreButton.setOnAction(e -> rootPane.getChildren().remove(overlayPane));
        header.getChildren().addAll(titleLabel, restoreButton);

        displayChart.prefWidthProperty().bind(maximizedContent.widthProperty());
        displayChart.prefHeightProperty().bind(maximizedContent.heightProperty().subtract(header.heightProperty()));

        maximizedContent.getChildren().addAll(header, displayChart);
        overlayPane.getChildren().add(maximizedContent);
        rootPane.getChildren().add(overlayPane);


        Platform.runLater(() -> {
            displayChart.applyCss();
            displayChart.layout();
        });

        Platform.runLater(() -> refreshCustomNodes(displayChart));
    }

    public static void refreshCustomNodes(Chart chart) {
        if (chart instanceof XYChart) {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            XYChart<?, ?> xyChart = (XYChart<?, ?>) chart;
            for (Object seriesObj : xyChart.getData()) {
                if (seriesObj instanceof XYChart.Series<?, ?>) {
                    XYChart.Series<?, ?> series = (XYChart.Series<?, ?>) seriesObj;
                    for (Object dataObj : series.getData()) {
                        if (dataObj instanceof XYChart.Data<?, ?>) {
                            XYChart.Data<?, ?> data = (XYChart.Data<?, ?>) dataObj;

                            // Cria o Tooltip com o valor formatado
                            Tooltip tooltip = new Tooltip(currencyFormatter.format(data.getYValue()));
//                            tooltip.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

                            // Instala o tooltip no nó do ponto assim que ele estiver disponível
                            if (data.getNode() != null) {
                                Tooltip.install(data.getNode(), tooltip);
                                data.getNode().setCursor(Cursor.HAND);

                                // Se desejar também mostrar o valor ao clicar, pode adicionar um listener:
                                data.getNode().setOnMouseClicked(e -> {
                                    // Exibe o tooltip manualmente
                                    tooltip.show(data.getNode(), e.getScreenX(), e.getScreenY());
                                    // Registra um listener para ocultar o tooltip quando a janela perder o foco
                                    data.getNode().getScene().getWindow().focusedProperty().addListener((obs, oldVal, newVal) -> {
                                        if (!newVal) {
                                            tooltip.hide();
                                        }
                                    });
                                });
                                
                            } else {
                                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                                    if (newNode != null) {
                                        Tooltip.install(newNode, tooltip);
                                        newNode.setCursor(Cursor.HAND);
                                        newNode.setOnMouseClicked(e -> {
                                            tooltip.show(newNode, e.getScreenX(), e.getScreenY());
                                        });
                                    }
                                });
                            }

                            // Atualiza o texto do tooltip se o valor mudar
                            data.YValueProperty().addListener((obs, oldVal, newVal) -> {
                                tooltip.setText(currencyFormatter.format(newVal));
                            });
                        }
                    }
                }
            }
        }
    }
}
