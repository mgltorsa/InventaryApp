package com.app.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import com.app.model.BuyTransaction;
import com.app.model.Inventary;
import com.app.model.Item;
import com.app.model.MoveTransaction;

public class Data {

	private static final String MOVES_DIR = "./data/csv/moves";
	private static final String BUY_DIR = "./data/csv/compras";
	private HashMap<String, Item> items;
	private HashMap<String, Item> assignedItems;

	private int minYear = Integer.MAX_VALUE;
	private int maxYear = Integer.MIN_VALUE;
	private int maxMonth = Integer.MIN_VALUE;
	private boolean hasOcurredChanges;

	public Data() {
		items = new HashMap<String, Item>();
		assignedItems = new HashMap<String, Item>();
	}

	public void loadInventary(String path) throws Exception {
		File file = new File(path);
		FileInputStream fi = new FileInputStream(file);
		InputStreamReader ir = new InputStreamReader(fi, "UTF-8");
		BufferedReader br = new BufferedReader(ir);
		String line;
		// Readed Titles
		br.readLine();
		int lineCount = 1;
		Exception ex = null;
		while ((line = br.readLine()) != null && !line.isEmpty()) {

			try {

				Inventary inventary = Inventary.parse(line);
				Item item = inventary.getItem();
				item.getInventaries().add(inventary);
				if (items.containsKey(item.getId())) {
					items.get(item.getId()).addInventaries(item.getInventaries());
				} else {
					items.put(item.getId(), item);
				}
				lineCount++;

			} catch (Exception e) {
				if (ex == null) {
					ex = new Exception("supressed errors");
				}
				String errorMessage = "Transaction error parse in register: " + lineCount + " from file: " + path + "\n"
						+ "Error message was: " + e.getMessage() + "\n";
				Exception aux = new Exception(errorMessage);
				aux.addSuppressed(e);
				ex.addSuppressed(aux);
			}
		}

		br.close();
		if (ex != null) {
			throw ex;
		}

	}

	public void loadMove(String path) throws Exception {
		File file = new File(path);
		FileInputStream fi = new FileInputStream(file);
		InputStreamReader ir = new InputStreamReader(fi, "UTF-8");
		BufferedReader br = new BufferedReader(ir);
		String line;
		int lineCount = 1;
		Exception ex = null;
		br.readLine();
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			try {

				MoveTransaction transaction = MoveTransaction.parseTransaction(line);
				String id = transaction.getItem().getId();
				int year = transaction.getDate().getYear();
				minYear = Math.min(minYear, year);

				maxYear = Math.max(maxYear, year);

				Item item = getItem(id);
				if (item == null) {
					throw new Exception("Item doesn't exist: " + id);
				} else {
					item.addMoveTransactions(transaction);
					transaction.setItem(item);
				}

				lineCount++;

			} catch (Exception e) {
				if (ex == null) {
					ex = new Exception("supressed errors");
				}
				String errorMessage = "Transaction error parse in register: " + lineCount + " from file: " + path + "\n"
						+ "Error message was: " + e.getMessage() + "\n";
				Exception aux = new Exception(errorMessage);
				aux.addSuppressed(e);
				ex.addSuppressed(aux);
			}
		}
		br.close();

		findLastMonth();

		if (ex != null) {
			throw ex;
		}

	}

	private void findLastMonth() {
		for (Item item : items.values()) {
			for (MoveTransaction transaction : item.getMoveTransactions()) {
				if (transaction.getDate().getYear() == maxYear) {
					maxMonth = Math.max(transaction.getDate().getMonthValue(), maxMonth);
				}
			}
		}
	}

	private void loadBuy(String path) throws Exception {
		File file = new File(path);
		FileInputStream fi = new FileInputStream(file);
		InputStreamReader ir = new InputStreamReader(fi, "UTF-8");
		BufferedReader br = new BufferedReader(ir);
		String line;
		int lineCount = 1;
		Exception ex = null;
		br.readLine();
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			try {

				BuyTransaction transaction = BuyTransaction.parseTransaction(line);
				String id = transaction.getItem().getId();

				Item item = getItem(id);
				if (item == null) {
					throw new Exception("Item doesn't exist: " + id);
				} else {
					item.addBuyTransactions(transaction);
					transaction.setItem(item);
				}

				lineCount++;

			} catch (Exception e) {
				if (ex == null) {
					ex = new Exception("supressed errors");
				}
				String errorMessage = "Transaction error parse in register: " + lineCount + " from file: " + path + "\n"
						+ "Error message was: " + e.getMessage() + "\n";
				Exception aux = new Exception(errorMessage);
				aux.addSuppressed(e);
				ex.addSuppressed(aux);
			}
		}
		br.close();

		if (ex != null) {
			throw ex;
		}

	}

	public Item getItem(String itemCode) {
		return items.get(itemCode);
	}

	public void loadMoves(String... dataSources) throws Exception {
		Exception ex = null;
		for (String dataSource : dataSources) {

			try {
				loadMove(dataSource);
			} catch (Exception e) {
				if (ex == null) {
					ex = new Exception("loadBuyError");
				}
				ex.addSuppressed(e);
			}
		}

		if (ex != null) {
			throw ex;
		}
	}

	public ArrayList<Item> search(String text) {
		ArrayList<Item> result = new ArrayList<Item>();
		Item item = null;

		Iterator<Item> ite = items.values().iterator();
		while (ite.hasNext()) {
			item = ite.next();
			String lowerText = text.toLowerCase();
			String upperText = text.toUpperCase();
			if (item.getDescription().contains(text) || item.getDescription().contains(lowerText)
					|| item.getDescription().contains(upperText) || item.getId().startsWith(text)
					|| item.getId().equals(text) || item.getId().startsWith(lowerText) || item.getId().equals(lowerText)
					|| item.getId().startsWith(upperText) || item.getId().equals(upperText)) {
				result.add(item);
			}
		}

		return result;
	}

	public ArrayList<Item> getItems() {
		ArrayList<Item> result = new ArrayList<Item>(items.values());

		return result;
	}

	public String getClassification(String itemId, int year) {
		double totalGeneral = 0.0;

		ArrayList<Item> itemsList = new ArrayList<Item>();

		for (Item item : getItemsForClassification(itemId)) {
			itemsList.add(item);
			totalGeneral += item.getTotalInYear(year);
		}

		itemsList.sort(new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				// TODO Auto-generated method stub
				return -Double.compare(o1.getTotalInYear(year), o2.getTotalInYear(year));
			}
		});

		double totalAccumulated = 0.0;
		boolean found = false;
		for (int i = 0; i < itemsList.size() && !found; i++) {
			Item item = itemsList.get(i);
			double acumulated = item.getTotalInYear(year) / totalGeneral;
			totalAccumulated += acumulated;

			if (item.getId().equals(itemId)) {
				found = true;
			}

		}

		String classification = totalAccumulated < 0.8 ? "A" : totalAccumulated < 0.95 ? "B" : "C";

		return classification;
	}

	private Collection<Item> getItemsForClassification(String itemId) {
		if (assignedItems.containsKey(itemId)) {
			return assignedItems.values();
		} else {
			return items.values();
		}
	}

	public void loadAssigned(String path) throws Exception {
		File file = new File(path);
		FileInputStream fi = new FileInputStream(file);
		InputStreamReader ir = new InputStreamReader(fi, "UTF-8");
		BufferedReader br = new BufferedReader(ir);
		String line;
		int lineCount = 1;
		Exception ex = null;
		br.readLine();
		while ((line = br.readLine()) != null && !line.isEmpty()) {

			try {
				lineCount++;
				String[] info = line.split(";");
				String id = info[0];
				Item item = getItem(id);
				if (item == null) {
					throw new Exception("Item with id: " + id + " doesn't exists");
				}
				assignedItems.put(id, item);

			} catch (Exception e) {
				if (ex == null) {
					ex = new Exception("supressed errors");
				}
				String errorMessage = "Transaction error parse in register: " + lineCount + " from file: " + path + "\n"
						+ "Error message was: " + e.getMessage() + "\n";
				Exception aux = new Exception(errorMessage);
				aux.addSuppressed(e);
				ex.addSuppressed(aux);
			}
			lineCount++;
		}

		br.close();

		if (ex != null) {
			throw ex;
		}

	}

	public double getCvd(String itemId) {
		double cvd = -1;
		if (assignedItems.containsKey(itemId)) {
			Item item = getItem(itemId);
			double mean = 0.0;
			double totalTransactions = 0.0;
			double desv = 0.0;

			int months = 0;
			ArrayList<Double> monthsInfo = new ArrayList<Double>();
			HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> transactionsByYear = item
					.getMoveTransactionsByYearAndMonth();

			for (Integer key : transactionsByYear.keySet()) {
				HashMap<Integer, ArrayList<MoveTransaction>> transactionsByMonth = transactionsByYear.get(key);
				for (Integer month : transactionsByMonth.keySet()) {

					double totalDemand = getTotalDemand(transactionsByMonth.get(month));
					totalTransactions += totalDemand;
					monthsInfo.add(totalDemand);
					months++;
				}
			}

			mean = totalTransactions / months;

			if (mean == 0) {
				return -1;
			}

			double totalDiffsDesv = 0.0;

			for (Double demand : monthsInfo) {
				totalDiffsDesv += Math.pow(demand - mean, 2.0);
			}

			desv = Math.sqrt(totalDiffsDesv / (months - 1));

			cvd = desv / mean;
		}
		return cvd;
	}

	private double getTotalDemand(ArrayList<MoveTransaction> list) {
		double demand = 0.0;
		for (MoveTransaction transaction : list) {
			demand += transaction.getDemand();
		}
		return demand;
	}

	public HashMap<String, Item> getAssignedItems() {
		return assignedItems;

	}

	public boolean isAssignedItem(String itemId) {
		return assignedItems.containsKey(itemId);
	}

	public MoveTransaction forecast(String itemId, int year, int month, String method) {
		MoveTransaction transaction = null;
		if (assignedItems.containsKey(itemId)) {

			Item item = getItem(itemId);
			double totalTransactions = 0.0;

			int hasDemandCounter = 0;
			ArrayList<Boolean> periods = new ArrayList<Boolean>();
			HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> transactionsByYear = item
					.getMoveTransactionsByYearAndMonth();

			if (!(transactionsByYear.containsKey(year) && transactionsByYear.get(year).containsKey(month))) {

				for (Integer key : transactionsByYear.keySet()) {
					HashMap<Integer, ArrayList<MoveTransaction>> transactionsByMonth = transactionsByYear.get(key);

					for (Integer monthKey : transactionsByMonth.keySet()) {

						double totalDemand = getTotalDemand(transactionsByMonth.get(monthKey));
						if (totalDemand > 0) {
							hasDemandCounter++;
							periods.add(true);
						} else {
							periods.add(false);
						}

						totalTransactions += totalDemand;

					}

					int n = 0;
					double totalPeriodsInDemand = 0.0;
					int lastDemand = 0;
					for (int i = 1; i < periods.size(); i++) {

						if (periods.get(i)) {
							if (periods.get(lastDemand)) {
								totalPeriodsInDemand += (i - lastDemand);
							}
							lastDemand = i;
							n++;
						}
					}

					double forecast = 0.0;

					if (hasDemandCounter == 1) {
						forecast = totalTransactions;
					} else {

						double zforecast = totalTransactions / hasDemandCounter;
						double nforecast = totalPeriodsInDemand / n;

						forecast = Math.ceil(zforecast / nforecast);
					}

					transaction = new MoveTransaction(item, forecast, LocalDate.of(year, month, 1));
				}
			}
			if (transaction != null) {
				hasOcurredChanges = true;
				minYear = Math.min(transaction.getDate().getYear(), minYear);
				maxYear = Math.max(transaction.getDate().getYear(), maxYear);
				
				//TODO:
				maxMonth = Math.max(transaction.getDate().getMonthValue(),maxMonth);
				item.addMoveTransactions(transaction);
			}

		}

		return transaction;
	}

	public String getLevel(String itemId, int year, String classification) {
		
		
		String lower = classification.toLowerCase();
		String response = "STOCK";
		if(lower.equals("a") || lower.equals("b") ) {
			response = "BAJO PEDIDO";
		}
		return response;
		
//		double totalDemand = 0.0;
//		double totalIn=0.0;
//		double totalOut=0.0;

//		Item item = getItem(itemId);
//
//		
//		for (MoveTransaction transaction : item.getMoveTransactions()) {
//			if (transaction.getDate().getYear() == year) {
//				totalDemand += transaction.getDemand();
//				totalIn += transaction.getIn();
//				totalOut += transaction.getOut();
//			}
//		}
//
//		double average = 0.0;
//
//		if (assignedItems.containsKey(item.getId())) {
//			average = getDemandAverageOfSelectedItems(year);
//		} else {
//			average = getDemandAverageOfItems(year);
//		}
//
//		String response = "";
//		if (totalDemand <= average) {
//			response = "BAJO";
//		} else {
//			response = "ALTO";
//		}

//		return response;
	}

//	private double getDemandAverageOfItems(int year) {
//
//		return getDemandAverageOfItems(this.items.values(), year);
//	}
//
//	private double getDemandAverageOfSelectedItems(int year) {
//
//		return getDemandAverageOfItems(this.assignedItems.values(), year);
//	}

//	private double getDemandAverageOfItems(Collection<Item> values, int year) {
//		double average = 0.0;
//		int size = values.size();
//		double total = 0;
//		for (Item item : values) {
//
//			for (MoveTransaction transaction : item.getMoveTransactions()) {
//				if (transaction.getDate().getYear() == year) {
//					total += transaction.getDemand();
//				}
//			}
//
//		}
//		average = total / size;
//		return average;
//	}

	public void loadMoveFile(File file) {

		Path out = Paths.get(MOVES_DIR, file.getName());
		Path source = Paths.get(file.getAbsolutePath());
		try {
			Files.copy(source, out);
			System.out.println(out.toString());
			loadMove(out.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<Integer> getAvailableYears() {

		ArrayList<Integer> range = new ArrayList<Integer>();
		for (int i = minYear; i <= maxYear; i++) {
			range.add(i);
		}
		return range;
	}

	public void addDemand(String itemId, double demand, LocalDate date) {
		Item item = items.get(itemId);
		hasOcurredChanges = true;

		MoveTransaction transaction = new MoveTransaction(item, demand, date);
		item.addMoveTransactions(transaction);

	}

	public LocalDate getMinDate() {

		return LocalDate.of(minYear, 1, 1);
	}

	public boolean dataHasChanged() {
		return hasOcurredChanges;
	}

	public void persist() {

	}

	public boolean isAvailableDate(LocalDate date) {

		return true;
	}

	public void loadBuyFile(File file) throws Exception {

		Path out = Paths.get(BUY_DIR, file.getName());
		Path source = Paths.get(file.getAbsolutePath());
		try {
			Files.copy(source, out);
			System.out.println(out.toString());
			loadBuy(out.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}

	public double getMinStock(Item item) {
		int r = getYearsQuantity();
		System.out.println("r-> "+r);
		return item.getMinStock(r);
	}	

	public double getMaxStock(Item item) {
		int r = getYearsQuantity();
		return item.getMaxStock(r);
	}

	public double getQ(Item item) {
		int r = getYearsQuantity();
		return item.getQ(r);
	}
	
	private int getYearsQuantity() {
//		HashMap<Integer, Integer> yearsMemory = new HashMap<Integer, Integer>();
//		for(Item item : items.values()) {
//			for(MoveTransaction transaction: item.getMoveTransactions()) {
//				yearsMemory.put(transaction.getDate().getYear(),0);
//			}
//		}
//		return yearsMemory.keySet().size();
		return 1;
	}

}
