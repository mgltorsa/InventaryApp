package com.app.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.model.Item;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InfoTableView extends View {

	public static final String INDICATORS = "indicators";

	public static final String STOCK = "stock";

	private String itemId;

	private DecimalFormat formatter = new DecimalFormat("#0.00");

	@FXML
	private TableView<InfoRegister> tableInfo;

	@FXML
	private TableColumn<?, ?> dataColumn;

	@FXML
	private TableColumn<?, ?> valueColumn;

	private String mode;

	@Override
	public void init(App app) {

		super.init(app);
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("field"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		getStage().setTitle("Inventario Periodico-Sistema (S,S)");
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
		updateInfo();
	}

	private void updateInfo() {

		tableInfo.getItems().clear();

		Item item = getApp().getItem(itemId);

		if(mode.equals(STOCK)) {
			updateInfoStock(item );
		}else if(mode.equals(INDICATORS)) {
			updateInfoIndicators(item);
		}
		

	}

	private void updateInfoIndicators(Item item) {
		HashMap<Integer, HashMap<String, Double>>  indicators = item.getIndicatorsByYear();
		
		ArrayList<InfoRegister> registers = new ArrayList<InfoRegister>();
		
		for(Integer year : indicators.keySet()) {
			HashMap<String, Double> byYear = indicators.get(year);
			
			for(String field : byYear.keySet()) {
				
				String fieldRegister = field+" - "+year;
				double value = byYear.get(field);
				String valueRegister = value < 0 ? "N/A" : formatter.format(value);
				registers.add(new InfoRegister(fieldRegister,valueRegister));

			}
			
		}
		
		tableInfo.setItems(FXCollections.observableArrayList(registers));
		
	}

	private void updateInfoStock(Item item) {
		ArrayList<InfoRegister> registers = new ArrayList<InfoRegister>();
		

		double reorderPoint = item.getReorderPoint();

		String reorderPointText = reorderPoint == -1 ? "N/A" : formatter.format(reorderPoint);

		registers.add(new InfoRegister("Punto de reorden", reorderPointText));

		double securityStock = item.getSecurityStock();

		String securityStockText = securityStock == -1 ? "N/A" : formatter.format(securityStock);

		registers.add(new InfoRegister("Inventario de seguridad", securityStockText));

		double minStockValue = getApp().getMinStock(item);

		String minStockValueText = minStockValue == -1 ? "N/A" : formatter.format(minStockValue);

		registers.add(new InfoRegister("Valor minimo del inventario", minStockValueText));

		double maxStockValue = getApp().getMaxStock(item);

		String maxStockValueText = maxStockValue == -1 ? "N/A" : formatter.format(maxStockValue);

		registers.add(new InfoRegister("Valor maximo del inventario", maxStockValueText));
		
		double q = getApp().getQ(item);

		String qText = q == -1 ? "N/A" : formatter.format(q);

		registers.add(new InfoRegister("Q*", qText));

		tableInfo.setItems(FXCollections.observableArrayList(registers));		
	}

	public void setMode(String mode) {
		this.mode=mode;
		
	}

}
