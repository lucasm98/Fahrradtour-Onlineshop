package biketour.tour;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * a concrete Tour with a special Date and a tour guide
 * @author julian
 */
@Entity
public class ConcreteTour extends Product {

	public enum PriceCategory {
		ECONOMY, STANDARD, LUXURY
	}

	public enum Status {
		ACTIVE, INACTIVE, CANCELED
	}

	@Lob
	private String descriptionText;
	@OneToOne(cascade = CascadeType.ALL)
	private Tour tour;
	@OneToOne(cascade = CascadeType.ALL)
	private UserAccount tourGuide;
	private LocalDate start;
	private LocalDate end;
	private PriceCategory priceCategory;
	private Status status;
	private int free;

	@SuppressWarnings("unused")
	private ConcreteTour(){};

	public ConcreteTour(Tour tour, UserAccount tourGuide, LocalDate start, LocalDate end,
						MonetaryAmount price, PriceCategory priceCategory, Status status) {
		super(tour.getName(), price);
		this.descriptionText = tour.getDescriptionText();
		this.tour = tour;
		this.tourGuide = tourGuide;
		this.start = start;
		this.end = end;
		this.priceCategory = priceCategory;
		this.status = status;
		this.free = tour.getCapacity();
	}

	public String getDescriptionText() {return descriptionText;}
	public String getShortDescriptionText() {
		return descriptionText.substring(0,
			Integer.min(240,descriptionText.length()))+"...";
	}

	public void setDescriptionText(String descrition_text) {this.descriptionText = descrition_text;}

	public Tour getTour() {return tour;}

	public void setTour(Tour tour) {this.tour = tour;}

	public UserAccount getTourGuide() {return tourGuide;}

	public void setTourGuide(UserAccount tourGuide) {this.tourGuide = tourGuide;}

	public LocalDate getEnd() {return end;}

	public String getGermanEnd() {return end.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
			.withLocale(Locale.GERMAN));
	}

	public void setEnd(LocalDate end) {this.end = end;}

	public LocalDate getStart() {return start;}

	public String getGermanStart() {return start.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
			.withLocale(Locale.GERMAN));
	}

	public void setStart(LocalDate start) {this.start = start;}

	public void setPriceCategory(PriceCategory priceCategory) {this.priceCategory = priceCategory;}

	public PriceCategory getPriceCategory() {return priceCategory;}


	public Status getStatus() {return status;}

	public void setStatus(Status status) {this.status = status;}

	public int getLeftDays(){
		return start.getDayOfYear()-LocalDate.now().getDayOfYear();
	}
	public boolean in5(){
		return getLeftDays()<=5 && getLeftDays()>0?true:false;
	}
	public boolean in10(){
		return getLeftDays()<=10 && getLeftDays()>5?true:false;
	}

	public boolean subFromFree(int number){
		if (number <= getFree()){
			setFree(getFree() - number);
			return true;
		}
		return false;
	}

	public int getFree() {return free;}

	public String getStatusString(){
		return status.name();
	}


	public boolean getStatusBool(){
		if (status.equals(Status.ACTIVE)){
			return true;
		}
		return false;
	}

	public void setFree(int booked) {
		this.free = booked;
	}

	public void addToFree(int added) {
		setFree(
				getFree() + added);
	}

	public boolean concreteTourCancelable() {
		return this.getStart().isAfter(LocalDate.now()) && isCanceled() == false ? true : false;
	}

	public void setCanceled() {
		this.setStatus(Status.CANCELED);
	}

	public boolean isCanceled() {
		return getStatus().equals(Status.CANCELED) ? true : false;
	}
}
