package com.app.model;

import java.time.LocalDate;

public class BuyTransaction extends Transaction {
	
	private static final int DATE_DEMAND_INDEX = 9;
	private static final int DATE_INPUT_INDEX = 10;
	private static final int FAULTS_INDEX = 6;
	private static final int ORDER_INDEX = 4;
	private static final int FORMAT = 2;
	
	
	private LocalDate dateDemand;
	private LocalDate dateInput;
	private double faults;
	private double order;

	public BuyTransaction(Item item, LocalDate dateDemand, LocalDate dateInput) {
		this.item=item;
		this.dateDemand=dateDemand;
		this.dateInput=dateInput;
	}
	
	public BuyTransaction(Item item, LocalDate dateDemand, LocalDate dateInput, double faults, double order) {
		this.item=item;
		this.dateDemand=dateDemand;
		this.dateInput=dateInput;
		this.faults=faults;
		this.order=order;
	}

	public LocalDate getDateDemand() {
		return dateDemand;
	}

	public void setDateDemand(LocalDate dateDemand) {
		this.dateDemand = dateDemand;
	}

	public LocalDate getDateInput() {
		return dateInput;
	}

	public void setDateInput(LocalDate dateInput) {
		this.dateInput = dateInput;
	}	

	public double getFaults() {
		return faults;
	}

	public void setFaults(double faults) {
		this.faults = faults;
	}
	
	

	public double getOrder() {
		return order;
	}

	public void setOrder(double order) {
		this.order = order;
	}

	public static BuyTransaction parseTransaction(String line) throws Exception {
		String[] info = line.split(";");

		
		String dateInfo[] = info[DATE_DEMAND_INDEX].split("/");
		int dayOfMonth = Integer.parseInt(dateInfo[0]);
		int month = Integer.parseInt(dateInfo[1]);
		int year = Integer.parseInt(dateInfo[2]);

		LocalDate dateDemand = LocalDate.of(year, month, dayOfMonth);
		
		dateInfo = info[DATE_INPUT_INDEX].split("/");
		dayOfMonth = Integer.parseInt(dateInfo[0]);
		month = Integer.parseInt(dateInfo[1]);
		year = Integer.parseInt(dateInfo[2]);
		
		LocalDate dateInput = LocalDate.of(year, month, dayOfMonth);
		
		Item item = Item.parseItem(info, FORMAT);		
		
		String faultsStr = info[FAULTS_INDEX].isEmpty() ? "0" : info[FAULTS_INDEX];
		double faults = Double.parseDouble(faultsStr);
		
		String orderStr = info[ORDER_INDEX].isEmpty() ? "0" : info[ORDER_INDEX];
		double order = Double.parseDouble(orderStr);

		BuyTransaction transaction = new BuyTransaction(item, dateDemand, dateInput, faults, order);
		
		return transaction;
	}



	
	
}
