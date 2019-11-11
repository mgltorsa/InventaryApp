package com.app.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import com.app.logic.Logger;
import com.app.model.Item;
import com.app.model.MoveTransaction;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ManagerView extends View {

	private static final String DETAIL_VIEW = "details.fxml";

	private static final String TRANSACTION_VIEW = "transactions.fxml";

	private static String SEARCH_VIEW = "search_list.fxml";

	@FXML
	private SplitPane splitPane;

	@FXML
	private Font x3;

	@FXML
	private Color x4;

	private SearchView searchView;

	private DetailView detailView;

	private TransactionView transactionView;

	@FXML
	private MenuItem openBuyOption;
	
	@FXML
	private MenuItem openMovesOption;

	@FXML
	private MenuItem aboutOption;

	private final FileChooser chooser = new FileChooser();

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		loadSearchView();
		openMovesOption.setOnAction((evt) -> {
			File file = chooser.showOpenDialog(this.getStage());
			if (file != null) {
				getApp().loadMoveFile(file);
			}
		});
		
		openBuyOption.setOnAction((evt) -> {
			File file = chooser.showOpenDialog(this.getStage());
			if (file != null) {
				getApp().loadBuyFile(file);
			}
		});
		getStage().getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

	}

	private void closeWindowEvent(WindowEvent event) {
		System.out.println("Window close request ...");

		if (getApp().dataSetChanged()) { // if the dataset has changed, alert the user with a popup
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Cerrando app");
			alert.setContentText(String.format("¿Desea guardar los cambios?"));
			alert.initOwner(getStage().getOwner());
			Optional<ButtonType> res = alert.showAndWait();

			if (res.isPresent()) {
				if(res.get().equals(ButtonType.YES)){
					getApp().persist();
				}
			}
		}
		
		Logger.getLogger().close();
	}

	private void loadSearchView() {
		InputStream stream = getClass().getResourceAsStream(SEARCH_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			searchView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			searchView.setPane(pane);
			searchView.setStage(stage);
			searchView.init(getApp());

			Platform.runLater(() -> {
				splitPane.getItems().add(searchView.getPane());
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setSearch(ArrayList<Item> result) {
		searchView.setSearch(result);

	}

	public void selectItem(Item item) {
		if (detailView == null) {
			loadDetailView();
		}

		detailView.setItem(item);

		if (detailView.getTableItemYear().getItems().size() > 0) {
			detailView.getTableItemYear().getSelectionModel().selectFirst();
		} else {
			if (transactionView != null) {
				transactionView.clear();
			}
		}

	}

	private void loadDetailView() {
		InputStream stream = getClass().getResourceAsStream(DETAIL_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			detailView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			detailView.setPane(pane);
			detailView.setStage(stage);
			detailView.init(getApp());

			Platform.runLater(() -> {
				splitPane.getItems().add(detailView.getPane());
				splitPane.setDividerPosition(0, searchView.getPane().getBoundsInParent().getMinX());

			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setTransactions(ArrayList<MoveTransaction> transactions) {
		if (transactionView == null) {
			loadTransactionView();
		}

		transactionView.setTransactions(transactions);

	}

	private void loadTransactionView() {
		InputStream stream = getClass().getResourceAsStream(TRANSACTION_VIEW);
		FXMLLoader loader = new FXMLLoader();
		Pane pane;
		try {
			pane = loader.load(stream);
			transactionView = loader.getController();
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);

			transactionView.setPane(pane);
			transactionView.setStage(stage);
			transactionView.init(getApp());

			Platform.runLater(() -> {
				splitPane.getItems().add(transactionView.getPane());
				splitPane.setDividerPosition(1, detailView.getPane().getBoundsInParent().getMinX());
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showChart(Item item) {
		detailView.showChart(item);

	}

	public void refresh() {

		this.searchView.refresh();

	}
}
