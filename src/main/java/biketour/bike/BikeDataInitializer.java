package biketour.bike;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class BikeDataInitializer implements DataInitializer {

	private BikeManager bikeManager;
	private final String bikeName = "Standart_Bike";
	private final String bikeAmount = "300";
	private final String bikeCurrency = "EUR";
	private final String biketype = "STANDARD";
	private final String bikeImage = "standard";
	private final String ebikeName = "E_Bike";
	private final String ebikeAmount = "700";
	private final String ebikeCurrency = "EUR";
	private final String ebikeType = "EBIKE";
	private final String ebikeImage = "ebike";

	BikeDataInitializer(BikeManager bikeManager){
		this.bikeManager = bikeManager;
	}

	@Override
	public void initialize() {
		for (int i = 0; i <= 200; i++) {
			bikeManager.createBike(new BikeForm(bikeName + i, bikeAmount, bikeCurrency, biketype, bikeImage));
			bikeManager.createBike(new BikeForm(ebikeName + i, ebikeAmount, ebikeCurrency, ebikeType, ebikeImage));

		}
	}
}
