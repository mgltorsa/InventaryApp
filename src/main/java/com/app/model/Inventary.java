package com.app.model;


public class Inventary {
	
	private static final int FORMAT=0;
	
	private static final int DESC_INDEX = 0;
	private static final int TYPE_INDEX=1;
	private static final int WAREHOUSE_INDEX=5;
	private static final int QUANTITY_INDEX=7;

	private Item item;
	private String inventaryDescription;
	private String inventaryType;
	private String warehouse;
	private double quantity;
	
	
	public Inventary(Item item, String inventaryDescription, String inventaryType, String warehouse, double quantity) {
		super();
		this.item = item;
		this.inventaryDescription = inventaryDescription;
		this.inventaryType = inventaryType;
		this.warehouse = warehouse;
		this.quantity = quantity;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public String getInventaryDescription() {
		return inventaryDescription;
	}


	public void setInventaryDescription(String inventaryDescription) {
		this.inventaryDescription = inventaryDescription;
	}


	public String getInventaryType() {
		return inventaryType;
	}


	public void setInventaryType(String inventaryType) {
		this.inventaryType = inventaryType;
	}


	public String getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}


	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}



	public static Inventary parse(String line) throws Exception {
		String info[] = line.split(";");
		String inventaryDescription = info[DESC_INDEX];
		String inventaryType = info[TYPE_INDEX];
		String warehouse = info[WAREHOUSE_INDEX];
		double quantity = Double.parseDouble(info[QUANTITY_INDEX]);
		Item item = Item.parseItem(info, FORMAT);
		Inventary inventary = new Inventary(item, inventaryDescription, inventaryType, warehouse, quantity);

		return inventary;
	}



	
	
	
	
	
}
