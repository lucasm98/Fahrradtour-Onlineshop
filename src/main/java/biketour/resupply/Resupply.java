package biketour.resupply;

import biketour.user.Address;
import org.hibernate.annotations.GenericGenerator;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Resupply {

	@Id
	@GenericGenerator(name="increment", strategy = "increment")
	@GeneratedValue(generator="increment")
	private long id;
	private String name;
	private Boolean materialCheck;
	private Boolean isEnabled;
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	private Resupply (){}

	Resupply(String name, Address address, boolean isEnabled){

		this.name = name;
		this.address = address;
		materialCheck=false;
		this.isEnabled = isEnabled;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public Boolean getMaterialCheck() {
		return materialCheck;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setEnabled(Boolean enabled) { isEnabled = enabled; }

	public void setMaterialCheck(Boolean materialCheck) {
		this.materialCheck = materialCheck;
	}
}
