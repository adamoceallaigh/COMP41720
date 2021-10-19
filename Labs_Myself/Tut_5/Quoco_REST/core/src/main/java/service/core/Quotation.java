// Imports
package service.core;

import java.io.Serializable;

/**
 * Class to store the quotations returned by the quotation services
 * @author Rem
 */

public class Quotation implements Serializable {

	// Class variable declarations
	private String company;
	private String reference;
	private double price;

	// Constructor
	public Quotation(String company, String reference, double price) {
		this.company = company;
		this.reference = reference;
		this.price = price;
	}

	public Quotation(){}


	// Getters and Setters

	public String getCompany(){
		return this.company;
	}

	public void setCompany(String company_temp){
		this.company = company_temp;
	}

	public String getReference(){
		return this.reference;
	}

	public void setReference(String reference_temp){
		this.reference = reference_temp;
	}

	public double getPrice(){
		return this.price;
	}

	public void setPrice(double price_temp){
		this.price = price_temp;
	}
	
}
