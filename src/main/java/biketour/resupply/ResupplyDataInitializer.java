package biketour.resupply;

import biketour.user.Address;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class ResupplyDataInitializer implements DataInitializer {

	private ResupplyCatalog catalog;

	ResupplyDataInitializer(ResupplyCatalog catalog){

		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		catalog.save(new Resupply("Some Station",new Address("Somestr.","1","234","Dresden"), true));
		catalog.save(new Resupply("Another Station",new Address("Anotherstr.","2","345","Karachi"), false));
		catalog.save(new Resupply("BackBone Station" ,new Address("Backbonestr.","42","404","SiliconWalley"), true));
		catalog.save(new Resupply("Plymouth Station",new Address("Plymouthstr.","18","420","Plymouth"), false));
		catalog.save(new Resupply("Cloudstore Station",new Address("100K Street","999999","101","Cloudium"), true));
		catalog.save(new Resupply("Sheep Station",new Address("Sheeperstr.","2","555","Wales"), true));
		catalog.save(new Resupply("Zoo Station",new Address("Animalstr.","808","238","Whales"), false));
		catalog.save(new Resupply("SimsonsHome Station",new Address("Evergreen Terrace","742","742","Springfield"), true));
	}
}
