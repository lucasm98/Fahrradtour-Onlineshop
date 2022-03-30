
package biketour.tour;

import javax.validation.constraints.NotEmpty;

/**
 * Formular that will be filled by an admin to create a new tour template
 * @author julian
 */
public class TourForm {

	@NotEmpty(message = "{TourForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{TourForm.description.NotEmpty}")
	private final String description;

	@NotEmpty(message = "{TourForm.capacity.NotEmpty}")
	private final String capacity;

	@NotEmpty(message = "{TourForm.image.NotEmpty}")
	private final String image;


	public TourForm(String name,  String description, String capacity, String image){
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.image = image;
	}

	String getName() {
		return name;
	}

	String getDescription() {
		return description;
	}

	String getCapacity() {
		return capacity;
	}

	String getImage(){return image;}
}