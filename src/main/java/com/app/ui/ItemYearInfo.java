package com.app.ui;

public class ItemYearInfo {

	private int year;
	private String classification;
	private String level;
	public ItemYearInfo(int year, String classification, String level) {
		this.year = year;
		this.classification = classification;
		this.level=level;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getClassification() {
		return classification;
	}
	
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
}
