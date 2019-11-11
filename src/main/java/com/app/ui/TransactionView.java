package com.app.ui;

import java.time.LocalDate;
import java.util.ArrayList;

import com.app.model.MoveTransaction;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TransactionView extends View {

	@FXML
	private Color x2;

	@FXML
	private Font x1;

	@FXML
	private TableView<MoveTransactionInfo> simpleTableTransaction;

	@FXML
	private TableColumn<MoveTransactionInfo, Double> quantityColumn;

	@FXML
	private TableColumn<MoveTransactionInfo, LocalDate> dateColumn;

	@FXML
	private TableColumn<MoveTransactionInfo, Double> totalCostColumn;

	@FXML
	private Button btViewTable;

	@FXML
	private Label lbTotalQuantity;

	@FXML
	private Label lbTotalInput;

	@FXML
	private Label lbTotalPendant;

	private ArrayList<MoveTransaction> transactions;

	@FXML
	private Label lbUnity;

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
		simpleTableTransaction.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		btViewTable.setVisible(false);

	}

	public void setTransactions(ArrayList<MoveTransaction> transactions) {
		this.transactions = transactions;
		updateTransactions();

	}

	private void updateTransactions() {
		ArrayList<MoveTransactionInfo> transactionInfos = new ArrayList<MoveTransactionInfo>();
		for (MoveTransaction transaction : transactions) {
			transactionInfos.add(new MoveTransactionInfo(transaction));
		}

		simpleTableTransaction.setItems(FXCollections.observableArrayList(transactionInfos));

		double totalDemand = 0.0;
		String unity = "N/A";
		for (MoveTransaction transaction : transactions) {
			totalDemand += transaction.getDemand();
			unity = transaction.getItem().getMeasurementUnit();
		}

		lbTotalQuantity.setText(totalDemand + "");
		lbUnity.setText(unity);
	}

	public void clear() {
		if (simpleTableTransaction != null && simpleTableTransaction.getItems() != null) {
			simpleTableTransaction.getItems().clear();
			
		}
		lbTotalQuantity.setText("");
		lbUnity.setText("");

	}

}
