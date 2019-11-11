package com.app.ui;

import java.time.LocalDate;

import com.app.model.BuyTransaction;

public class BuyTransactionInfo {

	private LocalDate demandDate;
	private LocalDate inputDate;
	
	public BuyTransactionInfo(BuyTransaction transaction) {
		super();
		this.demandDate=transaction.getDateDemand();
		this.inputDate=transaction.getDateInput();
	}

	public LocalDate getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(LocalDate demandDate) {
		this.demandDate = demandDate;
	}

	public LocalDate getInputDate() {
		return inputDate;
	}

	public void setInputDate(LocalDate inputDate) {
		this.inputDate = inputDate;
	}

	
	
	
	
	
}
