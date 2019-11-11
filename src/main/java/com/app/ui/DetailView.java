package com.app.ui;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.app.model.Item;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DetailView extends View {
	
	private static final String LINE_CHART_VIEW = "line_chart.fxml";


	private static final String OPTION_VIEW = "options.fxml";


	private static final String INPUT_VIEW = "input.fxml";


	private static final String BUYES_VIEW = "buyTable.fxml";


	private static final String INFO_VIEW = "infoTable.fxml";


	@FXML
	private Color x2;

	@FXML
	private Font x1;

	@FXML
	private Label lbItemCode;

	@FXML
	private TextField txItemDescription;

	@FXML
	private Label lbItemWarehouse;

	@FXML
	private Label lbItemCost;

	@FXML
	private TableView<ItemYearInfo> tableItemYear;

	@FXML
	private TableColumn<ItemYearInfo, ?> yearColumn;

	@FXML
	private TableColumn<ItemYearInfo, ?> classificationColumn;
	

    @FXML
    private TableColumn<ItemYearInfo, ?> stockColumn;

	@FXML
	private Label lbCvd;

	@FXML
	private Button btForecast;
	
	@FXML
	private Button btViewBuy;

	private DecimalFormat formatter = new DecimalFormat("#0.00");

	@FXML
	private Label lbLeadTime;
	
	@FXML
	private Button btTypeDemand;	

	@FXML
	private Button btLineChart;

	private LineChartView lineChartView;
	
	private OptionView optionView;


	private InputDialog inputDialog;
	
	private InfoTableView infoTableView;


	private BuyTableView buyTableView;
	
    @FXML
    private Button btInventarySystem;
    
    @FXML
    private Button btIndicators;
    

    @FXML
    private Label lbBuyFrecuency;
    
    @FXML
    private Label lbServiceLevel;
    

	

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

		tableItemYear.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableItemYear.getSelectionModel().selectedItemProperty().addListener((observable) -> {

			if (tableItemYear.getSelectionModel().getSelectedItem() != null
					&& tableItemYear.getSelectionModel().getSelectedItems() != null) {

				ArrayList<Integer> years = new ArrayList<Integer>();
				for (ItemYearInfo info : tableItemYear.getSelectionModel().getSelectedItems()) {
					years.add(info.getYear());
				}
				getApp().getTransactions(lbItemCode.getText(), years);
			}
		});

		btLineChart.setOnAction((evt) -> {
			getApp().showLineChart(lbItemCode.getText());
		});

		btForecast.setOnAction((evt) -> {
			showOptionsView();
		});
		
		btTypeDemand.setOnAction((evt)->{
			if(inputDialog==null) {
				loadInputDialog();
			}

			inputDialog.setItemId(this.lbItemCode.getText());
			inputDialog.show(true);
		});
		
		btViewBuy.setOnAction((evt)->{
			if(buyTableView==null) {
				loadBuyTable();
			}
			buyTableView.setItemId(lbItemCode.getText());
			buyTableView.show(true);
			
		});
		
		btInventarySystem.setOnAction((evt)->{
			if(infoTableView==null) {
				loadInfoTable();
			}
			infoTableView.setMode(InfoTableView.STOCK);
			infoTableView.setItemId(lbItemCode.getText());
			infoTableView.show(true);
		});
		
		btIndicators.setOnAction((evt)->{
			if(infoTableView==null) {
				loadInfoTable();
			}
			infoTableView.setMode(InfoTableView.INDICATORS);
			infoTableView.setItemId(lbItemCode.getText());
			infoTableView.show(true);
		});


	}

	private void loadInfoTable() {
		InputStream stream = getClass().getResourceAsStream(INFO_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			infoTableView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			infoTableView.setPane(pane);
			infoTableView.setStage(stage);
			infoTableView.init(getApp());

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void loadBuyTable() {
		InputStream stream = getClass().getResourceAsStream(BUYES_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			buyTableView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			buyTableView.setPane(pane);
			buyTableView.setStage(stage);
			buyTableView.init(getApp());

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	private void showOptionsView() {
		if(optionView==null) {
			loadOptionView();
		}
		
		optionView.setItemId(lbItemCode.getText());
		optionView.show(true);
		
	}

	private void loadOptionView() {
		InputStream stream = getClass().getResourceAsStream(OPTION_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			optionView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			optionView.setPane(pane);
			optionView.setStage(stage);
			optionView.init(getApp());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void loadLineChartView() {
		InputStream stream = getClass().getResourceAsStream(LINE_CHART_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			lineChartView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			lineChartView.setPane(pane);
			lineChartView.setStage(stage);
			lineChartView.init(getApp());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setItem(Item item) {
		
		lbItemCode.setFont(Font.font("Verdana", FontWeight.BOLD, lbItemCode.getFont().getSize()));
		if(getApp().isAssignedItem(item.getId())) {
			lbItemCode.setTextFill(Color.CORAL);
		}else {
			lbItemCode.setTextFill(Color.BLACK);
		}
		lbItemCode.setText(item.getId());
		txItemDescription.setText(item.getDescription());
		lbItemWarehouse.setText(item.getInventaries().get(0).getWarehouse());
		lbItemCost.setText(NumberFormat.getCurrencyInstance().format(item.getCost()));

		ArrayList<ItemYearInfo> items = new ArrayList<ItemYearInfo>();

		
		for (int year : getApp().getAvailableYears()) {
			String classification = getApp().getClassification(item.getId(), year);
			String level = getApp().getLevel(item.getId(),year,classification);
			items.add(new ItemYearInfo(year, classification, level));

		}

		tableItemYear.setItems(FXCollections.observableArrayList(items));
		double cvd = getApp().getCvd(item.getId());
		String text = "";
		if (cvd == -1) {
			text = "N/A";
		} else {
			text = formatter.format(cvd) + "";
			if (cvd >= 1) {
				text += " ( DEMANDA ERRATICA ) ";
			}
		}

		lbCvd.setText(text);
		if(optionView!=null) {
			optionView.setItemId(item.getId());
		}
		
		double leadTime = item.getLeadTime();

		String leadTimeText = leadTime==-1 ?  "No existe historial de compras para este item" : formatter.format(leadTime) + " dias"; 
		
		lbLeadTime.setText(leadTimeText);
		
		double frecuency = item.getFrecuency();
		
		String frecuencyText = frecuency == -1 ? "N/A" : formatter.format(frecuency);
		
		lbBuyFrecuency.setText(frecuencyText);
		
		
		
		//TODO:
		lbServiceLevel.setText("95%");
		

	}

	public void showChart(Item item) {
		if (lineChartView == null) {
			loadLineChartView();
		}
		
		lineChartView.setChart(item);
		lineChartView.show(true);
		
	}
	
	public TableView<ItemYearInfo> getTableItemYear() {
		return tableItemYear;
	}

}
