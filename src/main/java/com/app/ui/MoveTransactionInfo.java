package com.app.ui;

import java.text.NumberFormat;
import java.time.LocalDate;

import com.app.model.MoveTransaction;

public class MoveTransactionInfo {

	private double quantity;
	private LocalDate date;
	private double totalCost;
	
	public MoveTransactionInfo(MoveTransaction transaction) {
		super();
		this.quantity = transaction.getDemand();
		this.date = transaction.getDate();		
		this.totalCost=transaction.getDemand()*transaction.getItem().getCost();
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTotalCost() {
		return NumberFormat.getCurrencyInstance().format(totalCost);
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	
	
	
	
}
