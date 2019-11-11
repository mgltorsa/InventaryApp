package com.app.model;

import java.time.LocalDate;
import java.util.Comparator;

public class MoveTransaction extends Transaction {

	private static final int FORMAT = 1;

	private static final int DEMAND_INDEX = 7;

	private static final int IN_INDEX = 5;

	private static final int NETO_INDEX = 8;

	private static final int WAREHOUSE_INDEX = 4;

	private static final int DATE_INDEX = 6;

	public static final Comparator<? super MoveTransaction> comparator = new Comparator<MoveTransaction>() {

		@Override
		public int compare(MoveTransaction o1, MoveTransaction o2) {

			return o1.getDate().compareTo(o2.getDate());
		}
	};

	private double demand;
	// fecha de entrega
	private LocalDate date;
	private String warehouse;
	private double neto;
	private double in;

	public MoveTransaction(Item item, double demand, LocalDate date, String warehouse, double neto, double in) {
		super();
		this.item = item;
		this.demand = demand;
		this.date = date;
		this.warehouse = warehouse;
		this.neto = neto;
		this.in = in;
	}

	public MoveTransaction(Item item, double demand, LocalDate date) {
		this.item = item;
		this.demand = demand;
		this.date = date;
		this.warehouse = "N/A";
		this.neto = 0;
		this.in = 0;
	}

	

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public double getNeto() {
		return neto;
	}

	public void setNeto(double out) {
		this.neto = out;
	}

	public double getIn() {
		return in;
	}

	public void setIn(double in) {
		this.in = in;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public static MoveTransaction parseTransaction(String line) throws Exception {

		String[] info = line.split(";");

		String demandStr = info[DEMAND_INDEX].isEmpty() ? "0" : info[DEMAND_INDEX];
		double demand = Double.parseDouble(demandStr);
		String warehouse = info[WAREHOUSE_INDEX];
		String dateInfo[] = info[DATE_INDEX].split("/");
		int dayOfMonth = Integer.parseInt(dateInfo[0]);
		int month = Integer.parseInt(dateInfo[1]);
		int year = Integer.parseInt(dateInfo[2]);

		LocalDate date = LocalDate.of(year, month, dayOfMonth);
		Item item = Item.parseItem(info, FORMAT);

		String inStr = info[IN_INDEX].isEmpty() ? "0" : info[IN_INDEX];
		String outStr = info[NETO_INDEX].isEmpty() ? "0" : info[NETO_INDEX];

		double in = Double.parseDouble(inStr);
		double out = Double.parseDouble(outStr);

		MoveTransaction transaction = new MoveTransaction(item, demand, date, warehouse, out, in);
		return transaction;
	}

}
