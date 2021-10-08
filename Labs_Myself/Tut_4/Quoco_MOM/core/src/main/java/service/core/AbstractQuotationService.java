
// Imports

import java.util.Random;

public abstract class AbstractQuotationService implements QuotationService {

	// Class variable declarations
	private int counter = 1000;
	private Random random = new Random();
	
	/**
	 * Generate Random Prefix for the client
	 * @param prefix
	 * @return String representation of the prefix
	 */
	protected String generateReference(String prefix) {
		String ref = prefix;
		int length = 100000;
		while (length > 1000) {
			if (counter / length == 0) ref += "0";
			length = length / 10;
		}
		return ref + counter++;
	}

	/**
	 * Generate random price as default price of quotation for each service
	 * @param min
	 * @param range
	 * @return random number representing default price of quotation for each service
	 */
	protected double generatePrice(double min, int range) {
		return min + (double) random.nextInt(range);
	}
}
