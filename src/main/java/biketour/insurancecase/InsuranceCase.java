package biketour.insurancecase;

import biketour.tour.ConcreteTour;
import biketour.tour.Tour;
import biketour.user.User;
import com.mysema.commons.lang.Assert;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class InsuranceCase {

	private @Id @GeneratedValue long id;
	@OneToOne
	private ConcreteTour tour;
	@OneToOne
	private UserAccount userAccount;
	private LocalDate date;
	@Lob
	private String text;

	private InsuranceCase() {};

	public InsuranceCase(ConcreteTour tour, UserAccount userAccount, LocalDate date, String text) {

		Assert.notNull(tour, "Tour must not be null");
		Assert.notNull(userAccount, "User must not be null");
		Assert.notNull(date, "Date must not be null");
		Assert.notNull(text, "Text must not be null");

		this.tour = tour;
		this.userAccount = userAccount;
		this.date = date;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public ConcreteTour getTour() {
		return tour;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
