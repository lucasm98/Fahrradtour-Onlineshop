package biketour.tour;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

/**
 * Catalog of all Tours
 * @author julian
 */
public interface ConcreteTourCatalog extends Catalog<ConcreteTour> {

	Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	Iterable<ConcreteTour> findByPriceCategory(ConcreteTour.PriceCategory priceCategory, Sort sort);

	Iterable<ConcreteTour> findByStatus(ConcreteTour.Status status, Sort sort);

	Streamable<ConcreteTour> findByTourGuide(UserAccount userAccount);

	@Override
	Streamable<ConcreteTour> findAll();

	/**
	 * @param priceCategory is an Enum to describe the category of a tour
	 * @return an Iterable of tours with different price categories
	 */
	default Iterable<ConcreteTour> findByPriceCategory(ConcreteTour.PriceCategory priceCategory){
		return findByPriceCategory(priceCategory, DEFAULT_SORT);
	}

	default Iterable<ConcreteTour> findByStatus(ConcreteTour.Status status){
		return findByStatus(status, DEFAULT_SORT);
	}
}
