package biketour.purchase;

import biketour.bike.Bike;
import biketour.booking.Booking;
import biketour.tour.ConcreteTour;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Purchase extends Order {

	@OneToOne(cascade = CascadeType.ALL)
	private Booking booking;

	public Booking getBooking() {
		return booking;
	}


	public int getPoints() {
		return points;
	}

	private int points;



	public Purchase(UserAccount userAccount,int points,Booking booking,ArrayList<Bike> bikes) {
		super(userAccount);
		this.booking=booking;
		this.points=points;
		for (Bike b : bikes){
			addOrderLine(b, Quantity.of(1));
		}
	}

	public Purchase(UserAccount userAccount, PaymentMethod paymentMethod, int points,
					Booking booking, ArrayList<Bike> bikes) {
		super(userAccount, paymentMethod);
		this.booking=booking;
		this.points=points;
		for (Bike b : bikes){
			addOrderLine(b, Quantity.of(1));
		}
	}

	private Purchase() {
	}

	public List<OrderLine> getOrderLine() {
		return this.getOrderLines().toList();
	}

}
