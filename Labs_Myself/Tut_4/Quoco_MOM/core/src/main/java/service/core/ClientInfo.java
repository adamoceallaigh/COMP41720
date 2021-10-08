
// Imports



/**
 * Interface to define the state to be stored in ClientInfo objects
 * @author Rem
 */

public class ClientInfo implements java.io.Serializable {

	// Class variable declarations
	public static final char MALE				= 'M';
	public static final char FEMALE				= 'F';
	public String name;
	public char gender;
	public int age;
	public int points;
	public int noClaims;
	public String licenseNumber;
	
	// Constructors
	public ClientInfo(String name, char sex, int age, int points, int noClaims, String licenseNumber) {
		this.name = name;
		this.gender = sex;
		this.age = age;
		this.points = points;
		this.noClaims = noClaims;
		this.licenseNumber = licenseNumber;
	}
	
	public ClientInfo() {}


}
