
// Imports
package service.core;


/**
 *  Class to define the state to be stored in ClientInfo objects
 * @author Rem
 */

public class ClientInfo implements java.io.Serializable {

	// Class variable declarations
	public static final char MALE = 'M';
	public static final char FEMALE = 'F';
	private String name;
	private char gender;
	private int age;
	private int points;
	private int noClaims;
	private String licenseNumber;
	
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

	public String getName() {
		return this.name;
	}

	public void setName(String name_temp){
		this.name = name_temp;
	}

	public char getGender(){
		return this.gender;
	}

	public void setGender(char gender_temp){
		this.gender = gender_temp;
	}

	public int getAge(){
		return this.age;
	}

	public void setAge(int age_temp){
		this.age = age_temp;
	}

	public int getPoints(){
		return this.points;
	}

	public void setPoints(int points_temp){
		this.points = points_temp;
	}

	public int getNoClaims(){
		return this.noClaims;
	}

	public void setNoClaims(int no_claims_temp){
		this.noClaims = no_claims_temp;
	}

	public String getLicenseNumber(){
		return this.licenseNumber;
	}

	public void setLicenseNumber(String license_number_temp){
		this.licenseNumber = license_number_temp;
	}






}
