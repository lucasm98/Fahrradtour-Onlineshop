package biketour.register;

import javax.validation.constraints.NotEmpty;

public class Registration {

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private String userName;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private String firstName;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private String lastName;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private String email;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}") //
	private String password;

	@NotEmpty(message = "{RegistrationForm.street.NotEmpty}") //
	private String street;

	@NotEmpty(message = "{RegistrationForm.number.NotEmpty}") //
	private String number;

	@NotEmpty(message = "{RegistrationForm.city.NotEmpty}") //
	private String city;

	@NotEmpty(message = "{RegistrationForm.cityCode.NotEmpty}") //
	private String cityCode;

	public Registration(String userName, String firstName, String lastName, String email, String password,
						String street, String number, String city, String cityCode) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.street = street;
		this.number = number;
		this.city = city;
		this.cityCode = cityCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String toString() {
		return this.getUserName() + "\n" +
			this.getFirstName() + "\n" +
			this.getLastName() + "\n" +
			this.getEmail() + "\n" +
			this.getPassword() + "\n" +
			this.getStreet() + "\n" +
			this.getNumber() + "\n" +
			this.getCityCode() + "\n" +
			this.getCity();
	}
}