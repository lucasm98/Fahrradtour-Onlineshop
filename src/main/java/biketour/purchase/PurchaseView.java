package biketour.purchase;

import biketour.bike.Bike;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.Totalable;

import java.util.ArrayList;

public class PurchaseView {

	private String purchaseId;
	private String purchaseDate;
	private String userName;
	private String price;
	private String status;
	private String pay;

	private String points;

	private ArrayList<Bike> bikes;


	public PurchaseView(String purchaseId, ArrayList<Bike> bikes, String purchaseDate, String userName,
						String points,String price, String pay, String status) {
		this.purchaseId = purchaseId;
		this.bikes=bikes;
		this.purchaseDate = purchaseDate;
		this.userName = userName;
		this.pay=pay;
		this.points=points;
		this.price = price;
		this.status = status;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public String getPay() {
		return pay;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public String getUserName() {
		return userName;
	}

	public String getPrice() {
		return price;
	}

	public String getPoints() {
		return points;
	}

	public String getStatus() {
		return status;
	}

	public ArrayList<Bike> getBikes() {
		return bikes;
	}



}
