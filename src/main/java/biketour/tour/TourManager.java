package biketour.tour;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * manages all interaction involving a tour template
 * @author julian
 */
@Service
@Transactional
public class TourManager {

	private TourRepository repository;

	public TourManager(TourRepository repository){
		this.repository = repository;
	}

	/**
	 * creates a new Tour-Template
	 * @param form input informations from the admin
	 */
	public void createTour(TourForm form){
		repository.save(validateForm(form));
	}

	/**
	 * checks the input information and casts them
	 * @param form input information as strings
	 * @return a Tour template
	 */
	public Tour validateForm(TourForm form){
		Assert.notNull(form.getName(), "Name must not be null!");
		Assert.notNull(form.getDescription(), "Description must not be null!");
		Assert.notNull(form.getCapacity(), "Capacity must not be null!");
		Assert.notNull(form.getImage(), "Image must not be null.");
		return new Tour(form.getName(), form.getDescription(), form.getImage(), Integer.parseInt(form.getCapacity()),null);
	}

	/**
	 * @return the repositories of all tour-templates
	 */
	public TourRepository getRepository(){return this.repository;}

	/**
	 * @param form all updated data to edit the tour
	 * @param id of the tour template
	 */
	public void editTour(TourForm form, long id){
		Tour oldTour = repository.findById(id).get();
		if (oldTour.getCapacity() != Integer.parseInt(form.getCapacity())){
			oldTour.setCapacity(Integer.parseInt(form.getCapacity()));
		}
		if(!oldTour.getName().equals(form.getName())){
			oldTour.setName(form.getName());
		}
		if (!oldTour.getDescriptionText().equals(form.getDescription())){
			oldTour.setDescriptionText(form.getDescription());
		}
		if (!oldTour.getImage().equals(form.getImage())){
			oldTour.setImage(form.getImage());
		}
	}

	public Tour findById(long id){
		return repository.findById(id).get();
	}
}
