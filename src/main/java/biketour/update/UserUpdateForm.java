package biketour.update;

public class UserUpdateForm {

	private String firstName;
	private String lastName;
	private String email;
	private String street;
	private String number;
	private String cityCode;
	private String city;

	public UserUpdateForm(String firstName, String lastName, String email, String street, String number,
						  String cityCode, String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.street = street;
		this.number = number;
		this.cityCode = cityCode;
		this.city = city;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String getCityCode() {
		return cityCode;
	}

	public String getCity() {
		return city;
	}
}
