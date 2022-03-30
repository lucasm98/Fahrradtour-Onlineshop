package biketour.tour;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * Formular that will create the actual tour
 * @author julian
 */
public class ConcreteTourForm {

	@NotEmpty(message = "{TourForm.capacity.NotEmpty")
	private final String start;

	@NotEmpty(message = "{TourForm.end.NotEmpty}")
	private final String end;

	@NotEmpty(message = "{TourForm.price.NotEmpty}")
	private final String price;

	@NotEmpty(message = "{TourForm.priceCategory.NotEmpty}")
	private final String priceCategory;

	@NotEmpty(message = "{TourForm.status.NotEmpty}")
	private final String status;

	@NotEmpty(message = "{TourForm.tourGuide.NotEmpty")
	private final String tourGuide;

	public String getTourGuide() {
		return tourGuide;
	}

	public ConcreteTourForm(String tourGuide, String start, String end, String price, String priceCategory, String status){

		this.start = start;
		this.end = end;
		this.price = price;
		this.priceCategory = priceCategory;
		this.status = status;
		this.tourGuide = tourGuide;
	}

	public String getStart() {return start;}

	public String getEnd() {return end;}

	public String getPrice() {return price;}

	public String getPriceCategory() {return priceCategory;}

	public String getStatus() {return status;}
}
