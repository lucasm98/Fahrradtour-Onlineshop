package biketour.material;

import biketour.resupply.Resupply;
import biketour.resupply.ResupplyCatalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Order(30)
public class MaterialInitializer implements DataInitializer{

	@Autowired
	private ResupplyCatalog resupplyCatalog;
	private final MaterialCatalog catalog;
	private MaterialManager materialManager;

	public MaterialInitializer(MaterialCatalog catalog,MaterialManager materialManager){
	this.catalog = catalog;
	this.materialManager = materialManager;
	
	}

	/**
	 * Initializes test Materials
	 *
	 */
	@Override
	public void initialize() {
		
		for(Resupply resupply:resupplyCatalog.findAll()) {
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Lenker", "Lenkerstange zum auswechseln mit Bremsen und Klingel",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL, "ELenker",
					"Lenkerstange zum auswechseln mit Bremsen, Klingel und Ebikemonitor",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Pedal", "2 Pedale zum auswecheln",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"ERad", "Komplettes ERad",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Rad", "Komplettes Rad",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Sattel", "Sattel mit Sattelstange",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Bandagen", "5 Bandagen in einer Box",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Pflaster", "Packung Pflaster",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Wasser", "6 Wasserflaschen",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Wasserkanister", "5 Liter Wasserkanister",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Schokoriegel", "4 Schokoriegel",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.VERSORGUNG,
					"Bananenriegel", "4 Bananenriegel",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Tacho", "Tachenkomputer fürs Rad der Daten anzeigen kann",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Kette", "Kette zum auswechseln",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Flickzeug", "zum Ras flicken",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Karte", "zur Orientierung",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
			catalog.save(new Material(MaterialType.ERSATZTEIL,
					"Luftpumpe", "zum Rad aufpumpen",
					Quantity.of(new Random().nextInt(10)),resupply.getId(),resupply.getName()));
		}
	System.out.println("Material has been Initialized : "+ catalog.count());

	Material material1 = materialManager.findAllUnique().iterator().next();
	materialManager.createRequest(3,material1);


	Material material2 = materialManager.findAll().iterator().next();
	materialManager.createRequest(4,material2);
	Material material3 =materialManager.findErsatz().iterator().next();
	materialManager.createRequest(6,material3);
	}

}
