package service.core;
// Imports


import java.io.Serializable;

/**
 * Class to store the quotations returned by the quotation services
 * @author Rem
 */

public class Quotation implements Serializable {

	// Class variable declarations
	public String company;
	public String reference;
	public double price;

	// Constructor
	public Quotation(String company, String reference, double price) {
		this.company = company;
		this.reference = reference;
		this.price = price;
	}
	
}
