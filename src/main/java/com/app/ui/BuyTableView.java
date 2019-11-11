package com.app.ui;

import java.util.ArrayList;

import com.app.model.BuyTransaction;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BuyTableView extends View {

	
    @FXML
    private TableView<BuyTransactionInfo> tableItemBuy;

    @FXML
    private TableColumn<?, ?> orderColumn;

    @FXML
    private TableColumn<?, ?> inputColumn;
	
	private String itemId;
	
	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		orderColumn.setCellValueFactory(new PropertyValueFactory<>("demandDate"));
		inputColumn.setCellValueFactory(new PropertyValueFactory<>("inputDate"));
	}
	
	public void setItemId(String itemId) {
		this.itemId=itemId;
		refreshTable();
		
	}

	private void refreshTable() {
		tableItemBuy.getItems().clear();
		
		ArrayList<BuyTransactionInfo> transactionInfos = new ArrayList<BuyTransactionInfo>();
		for (BuyTransaction transaction : getApp().getItem(itemId).getBuyTransactions()) {
			transactionInfos.add(new BuyTransactionInfo(transaction));
		}

		tableItemBuy.setItems(FXCollections.observableArrayList(transactionInfos));
		
	}

}
