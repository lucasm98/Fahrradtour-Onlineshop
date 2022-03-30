package biketour.insurancecase;

import biketour.tour.ConcreteTour;
import biketour.tour.Tour;
import biketour.user.User;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Service
@Transactional
public class InsuranceCaseManager {

	private InsuranceCaseRepository insuranceCaseRepository;

	public InsuranceCaseManager(InsuranceCaseRepository insuranceCaseRepository) {
		this.insuranceCaseRepository = insuranceCaseRepository;
	}

	/**
	 * @return the insurance case repository
	 */
	public InsuranceCaseRepository getInsuranceCaseRepository() {
		return insuranceCaseRepository;
	}

	/**
	 * @param insuranceCaseRepository sets the insurance case repository
	 */
	public void setInsuranceCaseRepository(InsuranceCaseRepository insuranceCaseRepository) {
		this.insuranceCaseRepository = insuranceCaseRepository;
	}

	/**
	 * creates a new insurance case
	 * @param tour concrete tour needed to create a new case
	 * @param userAccount that belongs to a insurance case
	 * @param date when the insurance case happened
	 * @param text that describes the case
	 * @return a new created insurance case
	 */
	public InsuranceCase createInsuranceCase(ConcreteTour tour, UserAccount userAccount, LocalDate date, String text) {
		return insuranceCaseRepository.save(new InsuranceCase(tour, userAccount, date, text));
	}

	/**
	 * @return all insurance cases
	 */
	public Streamable<InsuranceCase> findAll() {
		return insuranceCaseRepository.findAll();
	}

	/**
	 * @param tour concrete tour
	 * @return all insurance cases that belong to the same tour
	 */
	public Streamable<InsuranceCase> findByTour(ProductIdentifier tour){
		return findAll().filter(element -> element.getTour().getId()==tour);
	}

}
