package biketour.resupply;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

public class ResupplyForm {

	@NotEmpty(message = "{ResupplyForm.name.NotEmpty}")
	private String name;

	@NotEmpty(message = "{ResupplyForm.street.NotEmpty}")
	private String street;

	@NotEmpty(message = "{ResupplyForm.number.NotEmpty}")
	private String number;

	@NotEmpty(message = "{ResupplyForm.cityCode.NotEmpty}")
	private String cityCode;

	@NotEmpty(message = "{ResupplyForm.city.NotEmpty}")
	private String city;

	public ResupplyForm(String name, String street, String number, String cityCode, String city) {
		this.name = name;
		this.street = street;
		this.number = number;
		this.cityCode = cityCode;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCityCode() { return cityCode; }

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}