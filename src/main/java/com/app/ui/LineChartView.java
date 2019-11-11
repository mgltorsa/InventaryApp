package com.app.ui;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.Locale;

import com.app.model.Item;
import com.app.model.MoveTransaction;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class LineChartView extends View {

	@FXML
	private LineChart<String, Double> chart;

	public void setChart(Item item) {

		chart.setTitle(item.getId() + " - Demanda/meses");
		HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> transactions = item.getMoveTransactionsByYearAndMonth();
		chart.getData().clear();
		for (Integer year : transactions.keySet()) {

			XYChart.Series<String, Double> serie = new XYChart.Series<String, Double>();
			serie.setName(year + "");
			HashMap<Integer, ArrayList<MoveTransaction>> byMonth = transactions.get(year);
			for (Integer month : byMonth.keySet()) {
				double total = 0.0;
				String monthStr = LocalDate.of(1999, month, 1).getMonth().getDisplayName(TextStyle.SHORT,
						Locale.getDefault());
				for (MoveTransaction transaction : byMonth.get(month)) {
					total += transaction.getDemand();
				}

				XYChart.Data<String, Double> data = new XYChart.Data<String, Double>(monthStr, total);
				
				data.setNode(new HoveredThresholdNode(0,total));
				serie.getData().add(data);
			}

			chart.getData().add(serie);

		}

	}

	class HoveredThresholdNode extends StackPane {
		HoveredThresholdNode(int priorValue, double value) {
			setPrefSize(10, 10);

			final Label label = createDataThresholdLabel(priorValue, value);

			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					getChildren().setAll(label);
					setCursor(Cursor.HAND);
					toFront();
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					getChildren().clear();
					setCursor(Cursor.CROSSHAIR);
				}
			});
		}

		private Label createDataThresholdLabel(int priorValue, double value) {
			final Label label = new Label(value + "");
			label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
			label.setStyle("-fx-font-size: 10; -fx-font-weight: bold; -fx-background-radius:20px");

			if (priorValue == 0) {
				label.setTextFill(Color.DARKGRAY);
			} else if (value > priorValue) {
				label.setTextFill(Color.FORESTGREEN);
			} else {
				label.setTextFill(Color.FIREBRICK);
			}

			label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			return label;
		}
	}

}
