package biketour.resupply;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ResupplyCatalog extends CrudRepository<Resupply, Long> {

	@Override
	Streamable<Resupply> findAll();
}
