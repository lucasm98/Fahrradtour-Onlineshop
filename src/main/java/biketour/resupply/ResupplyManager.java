package biketour.resupply;


import biketour.user.Address;
import com.mysema.commons.lang.Assert;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ResupplyManager {

	private final ResupplyCatalog catalog;

	public ResupplyManager(ResupplyCatalog catalog){
		Assert.notNull(catalog, "Resupply Catalog must not be null");
		this.catalog = catalog;
	}

	/**
	 * Creates and saves a new Resupply Station in ResupplyCatalog
	 * @param resupplyForm holds the required name and address details
	 * @return saves the new Resupply Station in the catalog
	 */
	public Resupply createResupply(ResupplyForm resupplyForm){

		Assert.notNull(resupplyForm.getName(), "Name must not be null");
		Assert.notNull(resupplyForm.getStreet(), "Street must not be null");
		Assert.notNull(resupplyForm.getNumber(), "Number must not be null");
		Assert.notNull(resupplyForm.getCityCode(), "City Code must not be null");
		Assert.notNull(resupplyForm.getCity(), "City must not be null");

		String name = resupplyForm.getName();
		Address address = new Address(resupplyForm.getStreet(), resupplyForm.getNumber(),
				resupplyForm.getCityCode(), resupplyForm.getCity());
		return catalog.save(new Resupply(name, address, true));
	}

	/**
	 * Finds a specific Resupply Station
	 * @param id of the required Resupply Station
	 * @return the required Resupply Station
	 */
	public Optional<Resupply> findResupplyById(long id) {

		return catalog.findById(id);
	}

	/**
	 * Edits the details of an existing Resupply Station
	 * @param id of the required Resupply Station
	 * @param resupplyEditForm holds the new name and address details
	 */
	public void editResupply(long id, ResupplyForm resupplyEditForm){

		Assert.notNull(resupplyEditForm.getName(), "Name must not be null");
		Assert.notNull(resupplyEditForm.getStreet(), "Street must not be null");
		Assert.notNull(resupplyEditForm.getNumber(), "Number must not be null");
		Assert.notNull(resupplyEditForm.getCityCode(), "City Code must not be null");
		Assert.notNull(resupplyEditForm.getCity(), "City must not be null");

		Resupply resupply = findResupplyById(id).get();
		String newName = resupplyEditForm.getName();
		Address newAddress = new Address(resupplyEditForm.getStreet(), resupplyEditForm.getNumber(),
				resupplyEditForm.getCityCode(), resupplyEditForm.getCity());

		if (findResupplyById(id).isPresent()){
			if (!resupply.getName().equals(newName)){
				resupply.setName(newName);
			}
			if (!resupply.getAddress().equals(newAddress)){
				resupply.setAddress(newAddress);
			}
		}
	}

	/**
	 * Toggles a given Resupply Station's state between Active and Inactive
	 * @param id of the required Resupply Station
	 */
	public void toggleState(long id){

		Resupply resupply = findResupplyById(id).get();
		if (findResupplyById(id).isPresent()){
			if (resupply.getEnabled()) {
				resupply.setEnabled(false);
			} else {
				resupply.setEnabled(true);
			}
		}
	}

	/**
	 * Checks if a Resupply Station is enabled
	 * @param id of the required Resupply Station
	 * @return the corresponding boolean
	 */
	public boolean isActive(long id){

		Resupply resupply = findResupplyById(id).get();
		if (findResupplyById(id).isEmpty()){
			throw new IllegalArgumentException("Cannot be empty");
		}
		return resupply.getEnabled();
	}

	/**
	 * Finds all active Resupply Stations
	 * @return an array list of active Resupply Stations
	 */
	public Iterable<Resupply> findAllActive(){

		ArrayList<Resupply> arrayList = new ArrayList<>();
		for (Resupply resupply : findAllResupplyStations()){
			if (resupply.getEnabled()){
				arrayList.add(resupply);
			}
		}
		return arrayList;
	}

	public Iterable<Resupply> getAll(){return catalog.findAll();}
	public Iterable<Resupply> findAllResupplyStations (){
		return catalog.findAll();
	}

}
