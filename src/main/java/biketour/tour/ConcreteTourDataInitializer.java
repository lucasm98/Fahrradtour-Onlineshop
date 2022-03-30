package biketour.tour;

import biketour.resupply.Resupply;
import biketour.resupply.ResupplyManager;
import biketour.user.UserManager;
import com.google.common.collect.Lists;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.money.Monetary;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.StreamSupport;

@Component
@Order(value = 25)
public class ConcreteTourDataInitializer implements DataInitializer {

	@Autowired
	private ResupplyManager resupplyManager;
	private TourRepository repository;
	private UserManager userManager;
	private ConcreteTourCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;

	//example sentence for description
	private final String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
			"sed diam nonumy eirmod " +
			"tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
			"At vero eos et accusam et justo duo " +
			"dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est " +
			"Lorem ipsum dolor sit amet. Lorem ipsum " +
			"dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
			"invidunt ut labore et dolore magna aliquyam erat, " +
			"sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
			"Stet clita kasd gubergren, no sea takimata sanctus " +
			"est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
			"sed diam nonumy eirmod tempor invidunt " +
			"ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
			"justo duo dolores et ea rebum. Stet clita " +
			"kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. " +
			"\n" +
			"Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, " +
			"vel illum dolore eu feugiat " +
			"nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent " +
			"luptatum zzril delenit augue " +
			"duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet,";

	ConcreteTourDataInitializer(ConcreteTourCatalog catalog, UserManager userManager,
								TourRepository repository, UniqueInventory<UniqueInventoryItem> inventory) {
		Assert.notNull(catalog, "catalog must not be null");
		Assert.notNull(userManager, "Account Manager must not be null");
		Assert.notNull(repository, "repository must not be null");
		Assert.notNull(inventory, "inventory must not be null");
		this.catalog = catalog;
		this.userManager = userManager;
		this.repository = repository;
		this.inventory = inventory;
	}

	@Override
	public void initialize() {
		ArrayList<Long> longList = new ArrayList<>();
		for(Resupply resupply : resupplyManager.getAll()){
			longList.add(resupply.getId());
		}

		if (repository.findAll().iterator().hasNext()) {
			return;
		}
		repository.save(new Tour("London Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000) + 100), "loTo", 60, longList));
		repository.save(new Tour("Edinburgh Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "edTo", 100,longList));
		repository.save(new Tour("Wales Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "waTo", 40,longList));
		repository.save(new Tour("Dublin Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "duTo", 50,longList));
		repository.save(new Tour("Brighton Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "brTo", 70,longList));
		repository.save(new Tour("Liverpool Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "liTo", 45,longList));
		repository.save(new Tour("Manchester Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "maTo", 55,longList));
		repository.save(new Tour("Chelsea Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "chTo", 35,longList));
		repository.save(new Tour("New Castle Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "ncTo", 45,longList));
		repository.save(new Tour("Country Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "coTo", 30,longList));
		repository.save(new Tour("Forest Tour", description.substring(new Random().nextInt(100),
				new Random().nextInt(1000)+100), "foTo", 40,longList));

		LocalDate start = LocalDate.now().minusDays(7);
		LocalDate end = LocalDate.now();
		UserAccount tourGuide = userManager.findUserAccountByUserName("Frodo95").get();

		if (catalog.findAll().iterator().hasNext()) {
			return;
		}

		ConcreteTour londonTour = new ConcreteTour(repository.findByName("London Tour"), tourGuide,
				LocalDate.now(),
				LocalDate.now().plusDays(5), Money.of(800, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.ECONOMY, ConcreteTour.Status.ACTIVE);
		catalog.save(londonTour);
		inventory.save( new UniqueInventoryItem(londonTour, Quantity.of(londonTour.getTour().getCapacity())));

		ConcreteTour edinburghTour = new ConcreteTour(repository.findByName("Edinburgh Tour"), tourGuide,
				LocalDate.now().plusDays(5),
				LocalDate.now().plusDays(12), Money.of(800, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.STANDARD, ConcreteTour.Status.ACTIVE);
		catalog.save(edinburghTour);
		inventory.save( new UniqueInventoryItem(edinburghTour, Quantity.of(edinburghTour.getTour().getCapacity())));

		ConcreteTour walesTour = new ConcreteTour(repository.findByName("Wales Tour"), tourGuide, LocalDate.now().
				plusDays(3), LocalDate.now().plusDays(10),
				Money.of(1000, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.ACTIVE);
		catalog.save(walesTour);
		inventory.save( new UniqueInventoryItem(walesTour, Quantity.of(walesTour.getTour().getCapacity())));

		ConcreteTour dublinTour = new ConcreteTour( repository.findByName("Dublin Tour"), tourGuide,LocalDate.now().
				plusMonths(1), LocalDate.now().plusWeeks(6),
				Money.of(1000, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.STANDARD, ConcreteTour.Status.ACTIVE);
		catalog.save(dublinTour);
		inventory.save( new UniqueInventoryItem(dublinTour, Quantity.of(dublinTour.getTour().getCapacity())));

		ConcreteTour liverpoolTour = new ConcreteTour(repository.findByName("Liverpool Tour"), tourGuide, start.
				plusDays(2), end.plusDays(2),
				Money.of(1200, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.ACTIVE);
		catalog.save(liverpoolTour);
		inventory.save( new UniqueInventoryItem(liverpoolTour, Quantity.of(liverpoolTour.getTour().getCapacity())));

		ConcreteTour brightonTour = new ConcreteTour(repository.findByName("Brighton Tour"), tourGuide, start.
				plusDays(1), end,
				Money.of(1200, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.ECONOMY, ConcreteTour.Status.ACTIVE);
		catalog.save(brightonTour);
		inventory.save( new UniqueInventoryItem(brightonTour, Quantity.of(brightonTour.getTour().getCapacity())));

		ConcreteTour manchesterTour = new ConcreteTour(repository.findByName("Manchester Tour"), tourGuide, start, end,
				Money.of(1400, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.ECONOMY, ConcreteTour.Status.ACTIVE);
		catalog.save(manchesterTour);
		inventory.save( new UniqueInventoryItem(manchesterTour, Quantity.of(manchesterTour.getTour().getCapacity())));

		ConcreteTour forestTour = new ConcreteTour(repository.findByName("Forest Tour"), tourGuide, start.plusWeeks(1),
				start.plusWeeks(2), Money.of(1200, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.ACTIVE);
		catalog.save(forestTour);
		inventory.save( new UniqueInventoryItem(forestTour, Quantity.of(forestTour.getTour().getCapacity())));

		ConcreteTour countryTour = new ConcreteTour(repository.findByName("Country Tour"), tourGuide, start.
				plusWeeks(2),
				start.plusWeeks(3), Money.of(1350, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.ACTIVE);
		catalog.save(countryTour);
		inventory.save( new UniqueInventoryItem(countryTour, Quantity.of(countryTour.getTour().getCapacity())));

		ConcreteTour newCastleTour = new ConcreteTour(repository.findByName("New Castle Tour"), tourGuide, start.
				plusWeeks(2),
				start.plusWeeks(3), Money.of(990, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.ACTIVE);
		catalog.save(newCastleTour);
		inventory.save( new UniqueInventoryItem(newCastleTour, Quantity.of(newCastleTour.getTour().getCapacity())));

		ConcreteTour chelseaTour = new ConcreteTour(repository.findByName("Chelsea Tour"), tourGuide, start.
				plusWeeks(3),
				start.plusWeeks(4), Money.of(1100, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.INACTIVE);
		catalog.save(chelseaTour);
		inventory.save( new UniqueInventoryItem(chelseaTour, Quantity.of(chelseaTour.getTour().getCapacity())));

		// concrete tours for scenario
		// tour end is longer than 14 days ago


		ConcreteTour concreteTour_older_than_14_days = new ConcreteTour(repository.findByName("Manchester Tour"),
				tourGuide,
				LocalDate.now().minusDays(22),
				LocalDate.now().minusDays(15),
				Money.of(1400, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.ECONOMY, ConcreteTour.Status.INACTIVE);
		concreteTour_older_than_14_days.setName("Manchester Tour (more than 14 days ago)");
		inventory.save(new UniqueInventoryItem(concreteTour_older_than_14_days, Quantity.
				of(concreteTour_older_than_14_days.getTour().getCapacity())));
		catalog.save(concreteTour_older_than_14_days);

		// tour end is less than 14 days ago
		ConcreteTour concreteTour_less_than_14_days = new ConcreteTour(repository.findByName("Dublin Tour"),
				tourGuide,
				LocalDate.now().minusDays(15),
				LocalDate.now().minusDays(8),
				Money.of(1200, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.LUXURY, ConcreteTour.Status.INACTIVE);
		concreteTour_less_than_14_days.setName("Dublin Tour (less than 14 days ago)");
		inventory.save(new UniqueInventoryItem(concreteTour_less_than_14_days, Quantity.
				of(concreteTour_less_than_14_days.getTour().getCapacity())));
		catalog.save(concreteTour_less_than_14_days);

		ConcreteTour concreteTour_active_atm = new ConcreteTour(repository.findByName("Wales Tour"),
				tourGuide,
				LocalDate.now().minusDays(2),
				LocalDate.now().plusDays(4),
				Money.of(1800, Monetary.getCurrency("EUR")),
				ConcreteTour.PriceCategory.STANDARD, ConcreteTour.Status.INACTIVE);
		concreteTour_active_atm.setName("Wales Tour (active)");
		inventory.save(new UniqueInventoryItem(concreteTour_active_atm, Quantity.
				of(concreteTour_active_atm.getTour().getCapacity())));
		catalog.save(concreteTour_active_atm);

	}
}
