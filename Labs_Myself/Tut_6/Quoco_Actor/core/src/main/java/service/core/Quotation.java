
// Imports
package service.core;
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

	// Getters and Setters 
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	

	
	
}
