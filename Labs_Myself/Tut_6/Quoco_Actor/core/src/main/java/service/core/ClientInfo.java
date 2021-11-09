
// Imports
package service.core;


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
	

	// Getters and Setters

	public static char getMale() {
		return MALE;
	}

	public static char getFemale() {
		return FEMALE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getNoClaims() {
		return noClaims;
	}

	public void setNoClaims(int noClaims) {
		this.noClaims = noClaims;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}



}
