package edu.pies.sysaguaapp.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChartUtilsPie {

    public static PieChart clonePieChart(PieChart original) {
        PieChart cloned = new PieChart();
        cloned.setTitle(original.getTitle());
        cloned.setClockwise(original.isClockwise());
        cloned.setLabelsVisible(original.getLabelsVisible());
        cloned.setLegendVisible(original.isLegendVisible());

        // Clona os dados de forma profunda
        ObservableList<PieChart.Data> dataClone = FXCollections.observableArrayList();
        for (PieChart.Data d : original.getData()) {
            dataClone.add(new PieChart.Data(d.getName(), d.getPieValue()));
        }
        cloned.setData(dataClone);

        return cloned;
    }

    public static void maximizeChart(PieChart originalChart, String chartTitle, StackPane rootPane) {
        PieChart displayChart = clonePieChart(originalChart);

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
    }
}
