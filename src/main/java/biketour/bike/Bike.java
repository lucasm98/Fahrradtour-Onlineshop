package biketour.bike;

import com.mysema.commons.lang.Assert;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Bike extends Product {

	public enum BikeStatus {
		AVAILABLE,
		RESERVED,
		BOUGHT

	}

	public enum BikeType {
		STANDARD("standard"),
		EBIKE("ebike");

		private String name;

		BikeType(String name) {
			this.name = name;
		}
	}

	private BikeType bikeType;
	private String image;
	private BikeStatus bikeStatus;

	public Bike(String name, MonetaryAmount price, BikeType bikeType, String image, BikeStatus bikeStatus) {
		super(name, price);
		Assert.notNull(bikeType, "BikeType must not be null!");
		Assert.notNull(image, "Image must not be null!");
		Assert.notNull(bikeStatus, "BikeStatus must not be null!");
		this.bikeType = bikeType;
		this.image = image;
		this.bikeStatus = BikeStatus.AVAILABLE;
	}

	private Bike() {
	}

	public void setBikeType(BikeType bikeType) {
		this.bikeType = bikeType;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setBikeStatus(BikeStatus bikeStatus) {
		this.bikeStatus = bikeStatus;
	}

	public BikeType getBikeType() {
		return this.bikeType;
	}

	public BikeStatus getBikeStatus() {
		return this.bikeStatus;
	}

	public String getImage() {
		return this.image;
	}
}
