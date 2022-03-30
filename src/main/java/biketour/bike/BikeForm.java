package biketour.bike;

public class BikeForm {

	private final String name;
	private final String amount;
	private final String currency;
	private final String bikeType;
	private final String image;

	public BikeForm(String name, String amount, String currency, String bikeType, String image) {
		this.name = name;
		this.amount = amount;
		this.currency = currency;
		this.bikeType = bikeType;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public String getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getBikeType() {
		return bikeType;
	}

	public String getImage() {
		return image;
	}
}
