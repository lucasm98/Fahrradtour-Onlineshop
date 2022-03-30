package biketour.bike;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface BikeRepository extends CrudRepository<Bike, ProductIdentifier> {
	@Override
	Streamable<Bike> findAll();

	Streamable<Bike> findByBikeType(Bike.BikeType type);


}