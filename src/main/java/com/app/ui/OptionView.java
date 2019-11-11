package com.app.ui;

import java.io.IOException;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OptionView extends View {

	private static final String INPUT_VIEW = "forecast_input.fxml";

	@FXML
	private Button btIntuitive;

	@FXML
	private Button btSimpleMean;

	@FXML
	private Button btPonderateMean;

	@FXML
	private Button btExpSuavization;

	@FXML
	private Button btExpDouble;

	@FXML
	private Button btExpForecast;

	@FXML
	private Button btCroston;

	@FXML
	private TextArea txInfo;

	private String itemId;

	private ForecastInputDialog inputDialog;

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		EventHandler<ActionEvent> handler = getHandler();
		btIntuitive.setOnAction(handler);
		btSimpleMean.setOnAction(handler);
		btPonderateMean.setOnAction(handler);
		btExpSuavization.setOnAction(handler);
		btExpDouble.setOnAction(handler);
		btExpForecast.setOnAction(handler);
		btCroston.setOnAction(handler);

	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	private EventHandler<ActionEvent> getHandler() {
		return (evt) -> {
			Button node = (Button) evt.getSource();
			String id = node.getId();
			String text = "";
			if (id.equals(btCroston.getId())) {
				text="Para demandas erraticas";
				if(getApp().isAssignedItem(this.getItemId())) {
					
					crostonForecast();
					this.show(false);
					
				}else {
					text+="Nota: No se realizo el pronostico dado que este item no hace parte de los asignados";
				}
			}else if(id.equals(btSimpleMean.getId())) {
				text="Este pronostico es para demandas unifomes y  no erraticas, usa datos historicos reales para generar un pronostico. Este metodo es util si la demanda del item permanece estable en el tiempo ";
			}else if(id.equals(btPonderateMean.getId())) {
				text="Es para demandas uniformes, no tiene en cuenta la tendencia y utiliza ponderaciones para hacer mas enfasis a las valoraciones recientes, es efectivo para suvizar fluctuaciones repentinas en el patron de la demanda";
			}else if(id.equals(btIntuitive.getId())) {
				text=" Es un metodo cualitativo que  asume que la demanda en el siguiente periodo, es la misma que el anterior.";
			}else if(id.equals(btExpSuavization.getId())) {
				text=" Este metodo es  para demandas uniformes y no considera tendencia, este metodo cuantitativo implica mantener muy pocos registros de datos historicos; Para realizar el nuevo pronostico solo tiene encuenta el pronostico del periodo anterior y la demanda en el periodo anterior, ademas de  una constante de suavizamiento ";
			}else if(id.equals(btExpDouble.getId())) {
				text=" Este metodo es para demenda con tendencia y se debe tener en cuenta un alfa y beta.(para demanda no erraticas)";
			}else if(id.equals(btExpForecast.getId())) {
				text="Este metodo es para demanda con tendencia  y no erraticas, realiza su pronostico a partir de minimos cuadrados";
			}
			
			
			txInfo.setText(text);


		};
	}

	private void crostonForecast() {
		
		if(inputDialog==null) {
			loadInputDialog();
		}
	
		inputDialog.setForecastMethod("croston");
		inputDialog.setItem(this.itemId);
		inputDialog.show(true);
		
	}

	private void loadInputDialog() {
		InputStream stream = getClass().getResourceAsStream(INPUT_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			inputDialog = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			inputDialog.setPane(pane);
			inputDialog.setStage(stage);
			inputDialog.init(getApp());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String getItemId() {
		
		return this.itemId;
	}
}
