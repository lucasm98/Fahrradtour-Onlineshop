package biketour.booking;

import biketour.bike.Bike;
import org.apache.tomcat.util.digester.ArrayStack;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marcel Körner
 */
@Entity
public class Booking extends Order{

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Bike> bikes;
	private final LocalDate date;
	private boolean isBuyable;

	private Booking() {
		this.bikes = new ArrayList<>();
		this.date = LocalDate.now();
		this.isBuyable = false;
	}

	public Booking(UserAccount userAccount) {
		super(userAccount);
		this.bikes = new ArrayList<>();
		this.date = LocalDate.now();
		this.isBuyable = false;
	}

	public Booking(UserAccount userAccount, PaymentMethod paymentMethod) {
		super(userAccount, paymentMethod);
		this.bikes = new ArrayList<>();
		this.date = LocalDate.now();
		this.isBuyable = false;
	}

	public Collection<Bike> getBikes() {
		return bikes;
	}

	public Collection<Bike> getActivBikes() {
		ArrayList<Bike> returnBike = new ArrayStack<>();
		for(Bike bike:bikes){
			if(bike.getBikeStatus()!= Bike.BikeStatus.BOUGHT){
				returnBike.add(bike);
			}
		}
		return returnBike;
	}



	public void setBikes(ArrayList<Bike> bikes) {
		this.bikes = bikes;
	}

	public LocalDate getDate() {
		return date;
	}

	public OrderLine getOrderLine() {
		return this.getOrderLines().toList().get(0);
	}

	public boolean hasOrderLine() {
		return !getOrderLines().toList().isEmpty() ? true : false;
	}

	public boolean bookingSuccessfully(Product product, Quantity quantity) {
		if(!hasOrderLine()) {
			addOrderLine(product,quantity);
			return true;
		} else {
			return false;
		}
	}

	public boolean getBuyable() {
		return isBuyable;
	}

	public void setBuyable(boolean buyable) {
		isBuyable = buyable;
	}

	public String orderStatusOpen() {
		return OrderStatus.OPEN.toString();
	}

}
