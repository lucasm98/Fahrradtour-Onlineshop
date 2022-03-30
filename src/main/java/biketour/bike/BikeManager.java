package biketour.bike;

import biketour.booking.Booking;
import biketour.tour.ConcreteTour;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.money.Monetary;
import java.util.*;

@Service
@Transactional
public class BikeManager {

	private BikeRepository bikeRepository;

	public BikeManager(BikeRepository bikeRepository) {
		this.bikeRepository = bikeRepository;
	}

	/**
	 * @param bikeForm all informtion needed to create a bike
	 * @return a created bike
	 */
	public Bike createBike(BikeForm bikeForm) {
		return bikeRepository.save
				(new Bike(bikeForm.getName(),
						Money.of(Integer.parseInt(bikeForm.getAmount()), Monetary.getCurrency(bikeForm.getCurrency())),
						Bike.BikeType.valueOf(bikeForm.getBikeType()),
						bikeForm.getImage(),
						Bike.BikeStatus.AVAILABLE));
	}

	public void createBikeByQuantityAndType(int quantity, String type){

		for (int i = 0; i < quantity; i++){
			String name, image;
			Money price;
			int sizeOfBikesByType = bikeRepository.findByBikeType(Bike.BikeType.valueOf(type)).toList().size() - 1;
			if (Bike.BikeType.valueOf(type) == Bike.BikeType.EBIKE){
				name = "E_Bike";
				price = Money.of(700, Monetary.getCurrency("EUR"));
				image = "ebike";
			}else{
				name = "Standart_Bike";
				price = Money.of(300, Monetary.getCurrency("EUR"));
				image = "standard";
			}

			bikeRepository.save(new Bike(name + (sizeOfBikesByType + 1), price,
					Bike.BikeType.valueOf(type), image, Bike.BikeStatus.AVAILABLE));
		}
	}

	/**
	 * @param bikeType the desired type
	 * @return all bikes with Type
	 */
	public Streamable<Bike> findBikesByType(Bike.BikeType bikeType) {
		return this.bikeRepository.findAll().filter(bike -> bike.getBikeType().equals(bikeType));
	}

	/**
	 * @return all available bikes
	 */
	public Streamable<Bike> findAll() {
		return bikeRepository.findAll();
	}

	/**
	 *
	 * @param bikeType to get the bikes of desired type
	 * @param quantity capacity of a concrete tour to fill tour with bikes
	 * @return a list of bikes with type
	 */
	public ArrayList<Bike> getBikesForOrder(Bike.BikeType bikeType, int quantity) {
		ArrayList<Bike> bikesArrayList =  new ArrayList<>();
		List<Bike> bikes = this.findBikesByType(bikeType).filter(bike
				-> bike.getBikeStatus() == Bike.BikeStatus.AVAILABLE).toList().subList(0, quantity);
		for(Bike bike : bikes){
			bikesArrayList.add(bike);
		}
		return bikesArrayList;
	}

	/**
	 *
	 * @param productIdentifier id of bike
	 * @return bike
	 */
	public Optional<Bike> findBikeById(ProductIdentifier productIdentifier) {
		return bikeRepository.findById(productIdentifier);
	}

	/**
	 * @param priceCategory of concreteTour
	 * @return status of bike
	 */
	public Bike.BikeType getBikeTypeFromPriceCategory(ConcreteTour.PriceCategory priceCategory) {
		return priceCategory.equals(ConcreteTour.PriceCategory.LUXURY) ?
				Bike.BikeType.EBIKE : Bike.BikeType.STANDARD;
	}

	/**
	 * @param id id of a bike but as a string
	 * @return the bike that has id
	 */
	public Bike findBikeByStringId(String id) {
		return findAll().filter(bike -> bike.getId().toString().equals(id)).toList().get(0);
	}

	public boolean setBikesFree(Booking booking) {
		Iterator<Bike> iterator = booking.getBikes().iterator();
		while(iterator.hasNext()) {
			iterator.next().setBikeStatus(Bike.BikeStatus.AVAILABLE);
		}
		booking.setBikes(new ArrayList<>());
		return true;
	}
}
