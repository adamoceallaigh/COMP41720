
// Imports


/**
 * Implementation of the DodgyDrivers insurance quotation service.
 * @author Rem
 *
 */
public class DDQService extends AbstractQuotationService {
	
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";
	
	/**
	 * Quote generation:
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */

	/** 
	 * Generates Quotation for the Dodgy Drivers Service using the client's info
	 * @param client_info
	 * @return Quotation
	 */ 
	@Override
	public Quotation generateQuotation(ClientInfo client_info) {
		// Create an initial quotation between 600 and 1200
		double price = generatePrice(600, 600);
		
		// Automatic 30% discount for being male
		int discount = (client_info.gender == ClientInfo.MALE) ? 30:0;
		
		// Automatic 2% discount per year over 60...
		discount += (client_info.age > 60) ? (2*(client_info.age-60)) : 0;
		
		// Add a points discount
		discount += getPointsDiscount(client_info.points);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}


	private int getPointsDiscount(int penalty_points_received) {

		// 20% discount for less than 3 penalty points
		if (penalty_points_received < 3) return 20;

		// No Discount for 3 - 6 penalty points
		if (penalty_points_received <= 6) return 0;

		// 50% added on for anything over 
		return -50;
	}

}
