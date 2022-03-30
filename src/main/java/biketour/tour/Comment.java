package biketour.tour;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENTS")
public class Comment implements Serializable {

	private static final long serialVersionUID = -7114101035786254953L;

	private @Id
	@GeneratedValue
	long id;

	private String text;
	private int rating;

	private LocalDateTime date;

	@SuppressWarnings("unused")
	private Comment() {}

	public Comment(String text, int rating, LocalDateTime dateTime) {

		this.text = text;
		this.rating = rating;
		this.date = dateTime;
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getRating() {
		return rating;
	}
}