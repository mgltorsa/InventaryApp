package com.app.ui;

import java.util.ArrayList;

import com.app.model.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SearchView extends View {

	@FXML
	private Font x1;

	@FXML
	private Color x2;

	@FXML
	private TextField txSearch;

	@FXML
	private ListView<Item> listView;
	
	private String seectedId;

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);

		txSearch.setOnKeyReleased((evt) -> {
			
			String searchText = txSearch.getText();
		
			getApp().searchAndUpdate(searchText);
		});
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, item) -> {
			if (item != null) {
				this.setSelectedId(item.getId());
				getApp().selectItem(this.getSelectedId());
			}
		});
	}
	
	public void setSelectedId(String id) {
		this.seectedId=id;
	}
	
	public String getSelectedId() {
		return this.seectedId;
	}

	public void setSearch(ArrayList<Item> result) {
		ObservableList<Item> items = FXCollections.observableArrayList(result);
		listView.setItems(items);

	}

	public void refresh() {
		getApp().selectItem(getSelectedId());
		
	}
}
