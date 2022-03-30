package biketour.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Marcel Körner
 */
@Entity
public class Address {

	private @Id @GeneratedValue long id;
	private String street, city, number, cityCode;

	private Address() {};

	public Address(String street, String number, String cityCode, String city) {
		this.street = street;
		this.number = number;
		this.cityCode = cityCode;
		this.city = city;
	}

	public long getId() {
		return id;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getNumber() {
		return number;
	}

	public String getCityCode() {
		return cityCode;
	}

	public String toString() {
		return this.getStreet() + " " + this.getNumber() + ", " + this.getCityCode() +
			", " + this.getCity();
	}
}
