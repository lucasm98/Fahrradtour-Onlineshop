package biketour.tour;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface TourRepository extends CrudRepository<Tour, Long> {

	public Tour findByName(String name);

	public Streamable<Tour> findByCapacity(int capacity);

}
