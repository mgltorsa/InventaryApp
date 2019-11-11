package com.app.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import com.app.logic.InventaryManager;
import com.app.logic.Logger;
import com.app.model.Item;
import com.app.model.MoveTransaction;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

	private static String INDEX_VIEW = "index.fxml";
	private static String MANAGER_VIEW = "manager.fxml";

	private IndexView indexView;
	private ManagerView managerView;
	private InventaryManager manager;

	@Override
	public void start(Stage stage) throws Exception {

		initViews(stage);
		initData();

	}

	private void initViews(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		InputStream stream = getClass().getResourceAsStream(INDEX_VIEW);
		Pane pane = loader.load(stream);
		indexView = loader.getController();

		Scene scene = new Scene(pane);
		stage.setScene(scene);

		indexView.setPane(pane);
		indexView.setStage(stage);

		indexView.init(this);
		indexView.show(true);

	}

	private void initData() {
		manager = new InventaryManager();
		// TODO

		Exception ex = null;

		File movesDirectory = new File("data/csv/moves");
		ArrayList<String> filesSrc = new ArrayList<String>();
		for (File file : movesDirectory.listFiles()) {

			filesSrc.add(file.getAbsolutePath());
		}
		String[] sources = new String[filesSrc.size()];

		filesSrc.toArray(sources);

		try {
			manager.init("data/csv/items/items.csv", "data/csv/items/assigned.csv", sources);

		} catch (Exception e) {
			if (ex == null) {
				ex = new Exception("At manager.init APP in code line: 76");
			}
			ex.addSuppressed(e);
			e.printStackTrace();
		}

		File buyDirectory = new File("data/csv/compras");
		try {

			manager.loadBuyFiles(buyDirectory.listFiles());
		} catch (Exception e) {
			if (ex == null) {
				ex = new Exception("At manager.init APP in code line: 76");
			}
			ex.addSuppressed(e);
			e.printStackTrace();
		}
		
		if(ex!=null) {
			Logger.getLogger().log(ex);
		}

	}

	public static void main(String[] args) {

		launch(args);
	}

	public void initAction() {
		indexView.show(false);

		if (managerView == null) {
			InputStream stream = getClass().getResourceAsStream(MANAGER_VIEW);
			FXMLLoader loader = new FXMLLoader();
			Pane pane;
			try {
				pane = loader.load(stream);
				managerView = loader.getController();
				Scene scene = new Scene(pane);
				Stage stage = new Stage();
				stage.setScene(scene);

				managerView.setPane(pane);
				managerView.setStage(stage);
				managerView.init(this);
				searchAndUpdate("");
				managerView.show(true);

			} catch (IOException e) {
				Logger.getLogger().log(e);
				e.printStackTrace();
			}

		} else {
			managerView.show(true);
		}

	}

	public void searchAndUpdate(String text) {
		if (text != null) {
			ArrayList<Item> result = manager.search(text);

			Platform.runLater(() -> {
				managerView.setSearch(result);
			});
		}
	}

	public void selectItem(String id) {
		if (id != null && !id.isEmpty()) {
			Item item = manager.getItem(id);
			Platform.runLater(() -> {
				if (item != null) {
					managerView.selectItem(item);
				}
			});
		}

	}

	public void getTransactions(String itemId, ArrayList<Integer> years) {
		if (itemId != null && !itemId.isEmpty()) {
			ArrayList<MoveTransaction> transactions = manager.getTransactions(itemId, years);
			Platform.runLater(() -> {
				managerView.setTransactions(transactions);
			});
		}

	}

	public String getClassification(String itemId, int year) {

		return manager.getClassification(itemId, year);
	}

	public double getCvd(String itemId) {

		return manager.getCvd(itemId);
	}

	public void forecast(String itemId, int year, int month, String method) {

		try {
			manager.forecast(itemId, year, month, method);
			selectItem(itemId);
		} catch (Exception e) {
			Logger.getLogger().log(e);
			e.printStackTrace();
		}

	}

	public void showLineChart(String itemId) {
		Item item = manager.getItem(itemId);
		Platform.runLater(() -> {
			managerView.showChart(item);
		});

	}

	public boolean isAssignedItem(String itemId) {
		return manager.isAssignedItem(itemId);
	}

	public String getLevel(String id, int year, String classification) {

		return manager.getLevel(id, year, classification);
	}

	public void loadMoveFile(File file) {
		manager.loadMoveFile(file);
		managerView.refresh();

	}

	public ArrayList<Integer> getAvailableYears() {

		return manager.getAvailableYears();
	}

	public void addDemand(String itemId, double value, LocalDate date) {
		manager.addDemand(itemId, value, date);
		managerView.refresh();

	}

	public LocalDate getMinDate() {
		return manager.getMinDate();
	}

	public boolean dataSetChanged() {
		return manager.dataHasChanged();
	}

	public void persist() {
		manager.persist();

	}

	public boolean isAvailableDate(LocalDate date) {

		return manager.isAvailableDate(date);
	}

	public void loadBuyFile(File file) {
		try {
			manager.loadBuyFile(file);
		} catch (Exception e) {
			Logger.getLogger().log(e);
			e.printStackTrace();
		}

	}

	public Item getItem(String itemId) {
		// TODO Auto-generated method stub
		return manager.getItem(itemId);
	}

	public double getMinStock(Item item) {

		return manager.getMinStock(item);
	}

	public double getMaxStock(Item item) {

		return manager.getMaxStock(item);
	}

	public double getQ(Item item) {
		return manager.getQ(item);
	}

}
