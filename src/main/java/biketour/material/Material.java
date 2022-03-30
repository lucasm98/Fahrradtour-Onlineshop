package biketour.material;

import org.salespointframework.catalog.Product;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Quantity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
public class Material extends Product implements Serializable {

	private MaterialType type;
	private String description;
	private long locid;
	private Quantity quantity;
	private String loc;


	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	private Material(){super();};

	Material(MaterialType type, String name, String description, Quantity quantity, long locId,String loc){

		super(name, Money.of(new BigDecimal("99.66"), "EUR"));
		super.addCategory(type.toString());
		this.type=type;
		this.description=description;
		this.quantity=quantity;
		this.locid =locId;
		this.loc=loc;
		}

	public String getDescription(){return description;}
	public String getType(){return type.name();}
	public long getLocid() {
		return locid;
	}
	public String getLoc() {
		return loc;
	}
	public Quantity getQuantity() {
		return quantity;
	}
	public Boolean isErsatz(){
		if(type==MaterialType.ERSATZTEIL){
			return true;
		}else{
			return false;
		}
	}
}
