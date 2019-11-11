package com.app.logic;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.model.Item;
import com.app.model.MoveTransaction;

public class InventaryManager {

	private Data data;

	public void init(String itemsSource, String assignedSrc, String... dataSources) throws Exception {
		data = new Data();
		Exception ex = null;
		try {
			data.loadInventary(itemsSource);

		} catch (Exception e) {
			if (ex == null) {
				ex = new Exception();
			}
			ex.addSuppressed(e);
		}

		try {

			data.loadMoves(dataSources);
		} catch (Exception e) {
			if (ex == null) {
				ex = new Exception();
			}
			ex.addSuppressed(e);
		}

		try {

			data.loadAssigned(assignedSrc);
		} catch (Exception e) {
			if (ex == null) {
				ex = new Exception();
			}
			ex.addSuppressed(e);
		}

		if (ex != null) {
			throw ex;
		}

	}

	public ArrayList<Item> search(String text) {
		if (text.isEmpty()) {
			return getItems();
		}
		return data.search(text);

	}

	public Item getItem(String id) {
		return data.getItem(id);
	}

	public ArrayList<Item> getItems() {
		return data.getItems();
	}

	public ArrayList<MoveTransaction> getTransactions(String itemId, ArrayList<Integer> years) {
		Item item = data.getItem(itemId);
		ArrayList<MoveTransaction> transactions = new ArrayList<MoveTransaction>();
		HashMap<Integer, Integer> hYears = new HashMap<Integer, Integer>();
		for (Integer year : years) {
			hYears.put(year, year);
		}
		for (MoveTransaction transaction : item.getMoveTransactions()) {
			if (hYears.containsKey(transaction.getDate().getYear())) {
				transactions.add(transaction);
			}
		}

		transactions.sort(MoveTransaction.comparator);

		return transactions;

	}

	public String getClassification(String itemId, int year) {
		return data.getClassification(itemId, year);
	}

	public double getCvd(String itemId) {

		return data.getCvd(itemId);
	}

	public boolean isAssignedItem(String itemId) {
		return data.isAssignedItem(itemId);
	}

	public MoveTransaction forecast(String itemId, int year, int month, String method) throws Exception {

		if (method.equals("croston")) {
			return data.forecast(itemId, year, month, method);
		} else {
			throw new Exception("Non valid method");
		}
	}

	public String getLevel(String itemId, int year, String classification) {

		return data.getLevel(itemId, year, classification);
	}

	public void loadMoveFile(File file) {

		data.loadMoveFile(file);
	}

	public ArrayList<Integer> getAvailableYears() {

		return data.getAvailableYears();
	}

	public void addDemand(String itemId, double value, LocalDate date) {
		data.addDemand(itemId, value, date);

	}

	public LocalDate getMinDate() {

		return data.getMinDate();
	}

	public boolean dataHasChanged() {

		return data.dataHasChanged();
	}

	public void persist() {
		data.persist();

	}

	public boolean isAvailableDate(LocalDate date) {

		return data.isAvailableDate(date);
	}

	public void loadBuyFile(File file) throws Exception {
		data.loadBuyFile(file);

	}

	public void loadBuyFiles(File[] listFiles) throws Exception {

		System.out.println("Loading buy files");
		Exception ex = null;
		for (File file : listFiles) {

			System.out.println("Loading file: "+file.getAbsolutePath());

			try {
				data.loadBuyFile(file);
				
			} catch (Exception e) {
				if(ex==null) {
					ex=new Exception("error loading buy files");
				}
				ex.addSuppressed(e);
				e.printStackTrace();
			}
		}
		
		if(ex!=null) {
			throw ex;
		}

	}

	public double getMinStock(Item item) {
		
		return data.getMinStock(item);
	}

	public double getMaxStock(Item item) {
		return data.getMaxStock(item);
	}

	public double getQ(Item item) {
		return data.getQ(item);
	}

}
