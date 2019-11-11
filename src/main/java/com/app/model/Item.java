package com.app.model;

import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Item {

	private static final double SERVICE_LEVEL = 0.95;
	private static int FORMAT_1 = 0;
	private static int FORMAT_2 = 1;
	private static int FORMAT_3 = 2;

	private static int ID_INDEX_F1 = 2;
	private static int REF_INDEX_F1 = 3;
	private static int DESCRIPTION_INDEX_F1 = 4;
	private static int MEASUREMENT_INDEX_F1 = 6;
	private static int COST_INDEX_F1 = 8;
	private static int STATE_INDEX_F1 = 9;
	private static int GROUP_INDEX_F1 = 10;

	private static int ID_INDEX_F2 = 1;

	private static int ID_INDEX_F3 = 0;

	private NormalDistribution normal = new NormalDistribution();

	private String id;

	private String ref;

	private String description;

	private String measurementUnit;

	private double cost;

	private String state;

	private String group;

	private ArrayList<Inventary> inventaries = new ArrayList<Inventary>();

	private ArrayList<MoveTransaction> moveTransactions = new ArrayList<MoveTransaction>();

	private ArrayList<BuyTransaction> buyTransactions = new ArrayList<BuyTransaction>();

	public Comparator<MoveTransaction> comparator = new Comparator<MoveTransaction>() {

		@Override
		public int compare(MoveTransaction o1, MoveTransaction o2) {

			return o1.getDate().compareTo(o2.getDate());
		}
	};

	public Item(String id, String ref, String description, String measurementUnit, double cost, String state,
			String group) {
		super();
		this.id = id;
		this.ref = ref;
		this.description = description;
		this.measurementUnit = measurementUnit;
		this.cost = cost;
		this.state = state;
		this.group = group;
	}

	public Item(String id) {
		this.id = id;
	}

	// TODO:
	public static Item parseItem(String[] info, int format) throws Exception {
		Item item = null;
		if (format == FORMAT_1) {
			item = parseFromFormat1(info);
		} else if (format == FORMAT_2) {
			item = parseFromFormat2(info);
		} else if (format == FORMAT_3) {
			item = parseFromFormat3(info);
		}

		return item;

	}

	private static Item parseFromFormat3(String[] info) throws Exception {
		String id = info[ID_INDEX_F3];
		Item item = new Item(id);
		if (id.isEmpty()) {
			throw new Exception("id was empty");
		}
		return item;
	}

	private static Item parseFromFormat2(String[] info) throws Exception {
		String id = info[ID_INDEX_F2];
		Item item = new Item(id);
		if (id.isEmpty()) {
			throw new Exception("id was empty");
		}
		return item;
	}

	private static Item parseFromFormat1(String[] info) throws Exception {
		String id = info[ID_INDEX_F1];
		if (id.isEmpty()) {
			throw new Exception("id was empty");
		}
		String ref = info[REF_INDEX_F1];
		String description = info[DESCRIPTION_INDEX_F1];
		String measurementUnit = info[MEASUREMENT_INDEX_F1];
		double cost = Double.parseDouble(info[COST_INDEX_F1]);
		String state = info[STATE_INDEX_F1];
		String group = info[GROUP_INDEX_F1];

		Item item = new Item(id, ref, description, measurementUnit, cost, state, group);
		return item;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public ArrayList<Inventary> getInventaries() {
		return inventaries;
	}

	public void setInventaries(ArrayList<Inventary> inventary) {
		this.inventaries = inventary;
	}

	public ArrayList<MoveTransaction> getMoveTransactions() {
		return moveTransactions;
	}

	public void setMoveTransactions(ArrayList<MoveTransaction> transactions) {
		this.moveTransactions = transactions;
	}

	public ArrayList<BuyTransaction> getBuyTransactions() {
		return buyTransactions;
	}

	public void setBuyTransactions(ArrayList<BuyTransaction> buyTransactions) {
		this.buyTransactions = buyTransactions;
	}

	public void addInventaries(ArrayList<Inventary> inventaries) {
		this.inventaries.addAll(inventaries);
	}

	public void addMoveTransactions(MoveTransaction... transactions) {
		for (MoveTransaction transaction : transactions) {
			this.moveTransactions.add(transaction);
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id + " : " + description;
	}

	public ArrayList<Integer> getAvailableYears() {
		HashMap<Integer, Integer> dates = new HashMap<Integer, Integer>();
		ArrayList<Integer> datesArray = new ArrayList<Integer>();
		for (MoveTransaction transaction : moveTransactions) {
			int year = transaction.getDate().getYear();
			if (!dates.containsKey(year)) {
				dates.put(year, year);
				datesArray.add(year);
			}
		}
		return datesArray;
	}

	public double getTotalInYear(int year) {

		double total = 0.0;
		for (MoveTransaction transaction : moveTransactions) {
			if (transaction.getDate().getYear() == year) {
				total += transaction.getDemand() * this.cost;
			}
		}

		return total;

	}

	public HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> getMoveTransactionsByYearAndMonth() {

		HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> byYearAndMonth = new HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>>();

		ArrayList<Integer> range = getRangeOfYears();

		if (range.size() > 0) {
			for (int i = 0; i < range.size() - 1; i++) {
				int year = range.get(i);
				byYearAndMonth.put(year, new HashMap<Integer, ArrayList<MoveTransaction>>());
				for (int j = 1; j <= 12; j++) {
					byYearAndMonth.get(year).put(j, new ArrayList<MoveTransaction>());
				}
			}

			int lastYear = range.get(range.size() - 1);
			byYearAndMonth.put(lastYear, new HashMap<Integer, ArrayList<MoveTransaction>>());

			for (int i = 1; i <= getLastMonth(lastYear); i++) {
				byYearAndMonth.get(lastYear).put(i, new ArrayList<MoveTransaction>());
			}

			for (MoveTransaction transaction : moveTransactions) {

				HashMap<Integer, ArrayList<MoveTransaction>> byMonth = byYearAndMonth
						.get(transaction.getDate().getYear());
				byMonth.get(transaction.getDate().getMonthValue()).add(transaction);

			}
		}

		return byYearAndMonth;
	}

	private ArrayList<Integer> getRangeOfYears() {

		moveTransactions.sort(comparator);

		ArrayList<Integer> range = new ArrayList<>();
		int minYear = -1;
		int maxYear = -1;
		if (moveTransactions.size() > 0) {
			minYear = moveTransactions.get(0).getDate().getYear();

			maxYear = moveTransactions.get(moveTransactions.size() - 1).getDate().getYear();
		}
		if (minYear != -1) {
			for (int i = minYear; i <= maxYear; i++) {
				range.add(i);
			}
		}

		return range;
	}

	private int getLastMonth(int year) {
		int month = -1;
		for (MoveTransaction transaction : moveTransactions) {
			if (transaction.getDate().getYear() == year) {
				int auxMonth = transaction.getDate().getMonthValue();
				month = month > auxMonth ? month : auxMonth;
			}
		}
		// TODO: REVIEW:
		return month == -1 ? 1 : month;
	}

	public double getLeadTime() {

		int total = buyTransactions.size();

		if (total == 0) {
			return -1;
		}

		int totalDays = 0;

//		getMoveTransactionsByYearAndMonth();

		for (int i = 0; i < buyTransactions.size(); i++) {
			BuyTransaction buyTransaction = buyTransactions.get(i);
			Period period = Period.between(buyTransaction.getDateDemand(), buyTransaction.getDateInput());
			totalDays += period.getDays();
		}

		double leadTime = (double) totalDays / (double) total;

		return leadTime;
	}

	public void addBuyTransactions(BuyTransaction transaction) {
		this.buyTransactions.add(transaction);

	}

	public double getReorderPoint() {

		double securityStock = getSecurityStock();
		if (securityStock == -1) {
			return -1;
		}

		double reorderPoint = (securityStock + getTotalAndPonderatedMonthlyDemand()) * getLeadTime();
		System.out.println("reorderPoint-> " + reorderPoint);

		return reorderPoint;
	}

	private double getTotalAndPonderatedMonthlyDemand() {
		int totalMonths = 0;
		double monthAmount = 0.0;

		HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> data = getMoveTransactionsByYearAndMonth();

		for (HashMap<Integer, ArrayList<MoveTransaction>> dataByMonth : data.values()) {
			for (ArrayList<MoveTransaction> transactions : dataByMonth.values()) {
				for (MoveTransaction transaction : transactions) {
					monthAmount += transaction.getDemand();
				}
				totalMonths++;

			}
		}

		System.out.println("totalMonths-> " + totalMonths);

		double average = monthAmount / totalMonths;
		return average;
	}

	public double getTotalDemand() {
		double demand = 0.0;
		for (MoveTransaction transaction : moveTransactions) {
			demand += transaction.getDemand();
		}
		return demand;
	}

	public double varDemand(double average) {
		double var = 0.0;
		int totalMonths = 0;
		HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> data = getMoveTransactionsByYearAndMonth();

		for (HashMap<Integer, ArrayList<MoveTransaction>> dataByMonth : data.values()) {
			for (ArrayList<MoveTransaction> transactions : dataByMonth.values()) {
				double monthAmount = 0.0;
				for (MoveTransaction transaction : transactions) {
					monthAmount += transaction.getDemand();
				}
				var += Math.pow(monthAmount - average, 2.0);
				totalMonths++;
			}
		}

		var = var / totalMonths - 1;
		return var;
	}

	public double getSecurityStock() {

		double securityStock = 0;

		if (moveTransactions.size() == 0) {
			return -1;
		}

		double average = getTotalAndPonderatedMonthlyDemand();

		System.out.println("average-> " + average);

		double leadTime = getLeadTime();

		if (leadTime == -1) {
			return -1;
		}

		double z = normal.inverseCumulativeProbability(SERVICE_LEVEL);

		System.out.println("z-> " + z);

		securityStock = z * (desvLeadTimeR(new double[] { 1, 1, 1, 1, 1 }));

		System.out.println("secu-> " + securityStock);
		return securityStock;
	}

	private double desvLeadTimeR(double[] is) {

		double[] array = new double[buyTransactions.size()];
		for (int i = 0; i < buyTransactions.size(); i++) {
			BuyTransaction buyTransaction = buyTransactions.get(i);
			Period period = Period.between(buyTransaction.getDateDemand(), buyTransaction.getDateInput());
			int days = period.getDays();
			array[i] = days;
		}

		array = vectorSum(array, is, true);

		double mean = mean(array);
		double desv = Math.sqrt(var(array, mean));
		return desv;
	}

	private double var(double[] array, double mean) {
		int n = array.length;

		double sum = 0;

		for (int i = 0; i < array.length; i++) {
			sum += Math.pow((array[i] - mean), 2.0);
		}

		double var = sum / (n - 1);
		return var;
	}

	private double mean(double[] array) {
		int total = 0;
		int sum = 0;
		for (double data : array) {
			sum += data;
			total++;
		}
		return (double) sum / (double) total;
	}

	private double[] vectorSum(double[] array, double[] is, boolean fill) {
		double[] aux = null;
		double[] vectorSum = null;
		double[] auxArray = array;
		if (array.length > is.length) {
			if (fill) {
				vectorSum = new double[array.length];
				aux = Arrays.copyOf(is, array.length);
			} else {
				System.out.println("Array length is greater than is array at vector sum");
			}
		} else if (is.length > array.length) {
			auxArray = Arrays.copyOf(array, is.length);
			vectorSum = new double[is.length];
			aux = is;
		} else {
			vectorSum = new double[is.length];
			aux = is;
		}

		for (int i = 0; i < auxArray.length; i++) {
			vectorSum[i] = auxArray[i] + aux[i];
		}

		return vectorSum;

	}

	public double getMinStock(int r) {

		double demand = getTotalAndPonderatedMonthlyDemand();

		double leadTime = getLeadTime();

		double securityStock = getSecurityStock();

		if (leadTime == -1 || securityStock == -1) {
			return -1;
		}

		double minStock = demand * (leadTime + r + securityStock);

		return minStock;
	}

	public double getMaxStock(int r) {

		double demand = getTotalAndPonderatedMonthlyDemand();

		double minStock = getMinStock(r);

		if (minStock == -1) {
			return -1;
		}
		double maxStock = (demand * r) + minStock;

		return maxStock;
	}

	public double getQ(int r) {
		double finalInv = getNetInv();

		double maxStock = getMaxStock(r);

		if (maxStock == -1) {
			return -1;
		}

		double q = maxStock - finalInv;
		return q;
	}

	private double getNetInv() {

		double finalInv = 0.0;
		for (MoveTransaction transaction : moveTransactions) {
			finalInv += transaction.getNeto();
		}
		return finalInv;
	}

	private double getCover(double meanInv, double demand) {

		if (demand <= 0) {
			return -1;
		}
		double cover = meanInv / demand;
		return cover;
	}

	private double getInventaryDays(double rotation, HashMap<Integer, ArrayList<MoveTransaction>> byMonth) {

		int totalMonths = byMonth.keySet().size();

		int days = totalMonths * 30;

		if (rotation <= 0) {
			return -1;
		}

		double inventaryDays = days / rotation;

		return inventaryDays;
	}

	private double getRotation(double meanInv, double demand) {

		if (meanInv <= 0) {
			return 0;
		}

		double rotation = demand / meanInv;

		return rotation;
	}

	public HashMap<Integer, HashMap<String, Double>> getIndicatorsByYear() {

		HashMap<Integer, HashMap<String, Double>> indicatorsByYear = new HashMap<Integer, HashMap<String, Double>>();
		HashMap<Integer, HashMap<Integer, ArrayList<MoveTransaction>>> byYear = getMoveTransactionsByYearAndMonth();
		for (Integer year : byYear.keySet()) {
			HashMap<Integer, ArrayList<MoveTransaction>> byMonth = byYear.get(year);
			HashMap<String, Double> indicators = new HashMap<String, Double>();

			double meanInv = getMeanInv(byMonth);
			double demand = getDemand(byMonth);
			double rotation = getRotation(meanInv, demand);
			double inventary = getInventaryDays(rotation, byMonth);
			double cover = getCover(meanInv, demand);

			double totalFault = getTotalFaults(year);

			double faultsPercent = getFaultsPercent(totalFault,demand);
			indicators.put(Indicators.ROTATION.toString(), rotation);
			indicators.put(Indicators.INVENVTARY_DAYS.toString(), inventary);
			indicators.put(Indicators.COVER.toString(), cover);
			indicators.put(Indicators.FAULTS.toString(), faultsPercent);
			indicatorsByYear.put(year, indicators);
		}

		return indicatorsByYear;
	}

	private double getTotalFaults(Integer year) {
		double totalFaults = 0;
		for (BuyTransaction transaction : buyTransactions) {
			if (transaction.getDateDemand().getYear() == year) {
				totalFaults += transaction.getFaults();
			}
		}
		return totalFaults;
	}

	private double getFaultsPercent(double totalFaults, double demand) {

		// Total faltantes (x anio) / demand
		
		if (demand == 0) {
			return -1;
		}
		double faults = totalFaults / demand;
		return faults;
	}

	private double getDemand(HashMap<Integer, ArrayList<MoveTransaction>> byMonth) {
		double demand = 0.0;

		for (ArrayList<MoveTransaction> transactions : byMonth.values()) {
			for (MoveTransaction transaction : transactions) {
				demand += transaction.getDemand();
			}
		}
		return demand;
	}

	private double getMeanInv(HashMap<Integer, ArrayList<MoveTransaction>> byMonth) {
		int totalMonths = byMonth.keySet().size();
		double neto = 0;
		for (ArrayList<MoveTransaction> transactions : byMonth.values()) {
			for (MoveTransaction transaction : transactions) {
				neto += transaction.getNeto();

			}
		}
		if (totalMonths <= 0) {
			return -1;
		}
		double meanInv = neto / totalMonths;
		return meanInv;
	}

	public double getFrecuency() {
		double totalOrder = 0.0;
		for (BuyTransaction transaction : buyTransactions) {
			totalOrder += transaction.getOrder();
		}
		double demand = getTotalDemand();
		if (demand == 0) {
			return -1;
		}
		double frecuency = totalOrder / demand;
		return frecuency;
	}

}
