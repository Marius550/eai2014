package de.java.ws;

import de.java.domain.Drug;

/**
 * Message Drug is an object representing a drug and used for exchange via rest webservices
 * Depending on the usage, only pzn, name and description are filled (for create and update)
 * or all attributes are filled (for get -> displaying a drug)
 */
public class MessageDrug implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Master data
	private int pzn;
	private String name;
	private double price;
	private long drugMinimumAgeYears;
	private String description;
	// Replenishment Configuration
	private long minimumInventoryLevel;
	private long optimalInventoryLevel;
	// Current inventory and prescription quantities
	private long stock;
	private long pendingQuantity;
	private long unfulfilledQuantity;
	
	public MessageDrug(){
		
	}
	/**
	 * Creates a simplified drug based on the master data of a drug. Uses all attributes in a drug, i.e.
	 * pzn, name, price, drugMinimumAgeYears, description, stock, minimumInventoryLevel, optimalInventoryLevel
	 * @param drug
	 */
	public MessageDrug(Drug drug){
		if (drug == null){
			return;
		}
		this.pzn = drug.getPzn();
		this.name = drug.getName();
		this.price = drug.getPrice();
		this.drugMinimumAgeYears = drug.getDrugMinimumAgeYears();
		this.description = drug.getDescription();
		this.stock = drug.getStock();
		this.minimumInventoryLevel = drug.getMinimumInventoryLevel();
		this.optimalInventoryLevel = drug.getOptimalInventoryLevel();
	}
	
	// Lots of getters and setters
	public int getPzn() {
		return pzn;
	}
	public void setPzn(int pzn) {
		this.pzn = pzn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getDrugMinimumAgeYears() {
		return drugMinimumAgeYears;
	}
	public void setDrugMinimumAgeYears(long drugMinimumAgeYears) {
		this.drugMinimumAgeYears = drugMinimumAgeYears;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getStock() {
		return stock;
	}
	public void setStock(long stock) {
		this.stock = stock;
	}
	public long getPendingQuantity() {
		return pendingQuantity;
	}
	public void setPendingQuantity(long pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	public long getUnfulfilledQuantity() {
		return unfulfilledQuantity;
	}
	public void setUnfulfilledQuantity(long unfulfilledQuantity) {
		this.unfulfilledQuantity = unfulfilledQuantity;
	}
	public long getMinimumInventoryLevel() {
		return minimumInventoryLevel;
	}
	public void setMinimumInventoryLevel(long minimumInventoryLevel) {
		this.minimumInventoryLevel = minimumInventoryLevel;
	}
	public long getOptimalInventoryLevel() {
		return optimalInventoryLevel;
	}
	public void setOptimalInventoryLevel(long optimalInventoryLevel) {
		this.optimalInventoryLevel = optimalInventoryLevel;
	}
	
	/**
	 * Converts a Message drug to a real drug object ONLY USING pzn, name and description
	 * @return new Drug, based on pzn, name and description of the message drug
	 */
	public Drug convertToDrug (){
		Drug realDrug = new Drug();
		realDrug.setName(name);
		realDrug.setPrice(price);
		realDrug.setDrugMinimumAgeYears(drugMinimumAgeYears);
		realDrug.setDescription(description);
		realDrug.setPzn(pzn);
		return realDrug;
	}

}