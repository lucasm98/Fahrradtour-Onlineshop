package biketour.booking;

import org.apache.logging.log4j.message.StringFormattedMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

/**
 * @author Marcel Körner
 */
public class BookingView {

	private String bookingId;
	private String tourName;
	private String userName;
	private String start;
	private String end;
	private String quantity;
	private String status;
	private String price;
	private String pay;
	private boolean isBuyable;
	private String state;


	public BookingView(String bookingId, String tourName, String userName, String start, String end,
					   String quantity, String status, boolean isBuyable,String price,String pay,String state) {
		this.bookingId = bookingId;
		this.tourName = tourName;
		this.userName = userName;
		this.start = start;
		this.end = end;
		this.quantity = quantity;
		this.status = status;
		this.isBuyable = isBuyable;
		this.price=price;
		this.pay=pay;
		this.start=start;
	}

	public String getState() {
		return state;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getTourName() { return tourName; }

	public String getUserName() {
		return userName;
	}

	public String getStart() {
		return start;
	}

	public String getGermanStart(){
		return getLocalDate(start).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(Locale.GERMAN));
	}

	public String getEnd() {
		return end;
	}

	public String getGermanEnd(){
		return getLocalDate(end).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(Locale.GERMAN));
	}

	public String getPrice() {
		return price;
	}

	public String getPay() {
		return pay;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getStatus() {
		return status;
	}

	public boolean isBuyable() {
		return isBuyable;
	}

	/**
	 * casts the date string to a local date
	 * @param formInput date string
	 * @return LocalDate
	 */
	protected LocalDate getLocalDate(String formInput){
		//	"2019-12-16" --> LocalDate.of(2019, 12, 16
		String dates[] = formInput.split("-");
		return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
	}
}
