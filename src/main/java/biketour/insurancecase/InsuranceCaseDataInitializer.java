package biketour.insurancecase;

import biketour.tour.ConcreteTourCatalog;
import biketour.tour.ConcreteTourDataInitializer;
import biketour.tour.TourRepository;
import biketour.user.UserManager;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Component
@Order(value = 30)
public class InsuranceCaseDataInitializer implements DataInitializer {

	private InsuranceCaseManager insuranceCaseManager;
	private ConcreteTourCatalog concreteTourCatalog;
	private UserManager userManager;

	public InsuranceCaseDataInitializer(InsuranceCaseManager insuranceCaseManager,
										ConcreteTourCatalog concreteTourCatalog, UserManager userManager) {
		this.insuranceCaseManager = insuranceCaseManager;
		this.concreteTourCatalog = concreteTourCatalog;
		this.userManager = userManager;
	}

	@Override
	public void initialize() {
		insuranceCaseManager.createInsuranceCase(
			concreteTourCatalog.findAll().iterator().next(),
			userManager.findAll().toList().get(0).getUserAccount(),
				LocalDate.now(),
			"Auf Grund noch ungeklärter Ursachen verletzte sich der Kunde beim Aufsteigen auf sein " +
					"Fahrrad, wobei die Person seitlich stürzte und einen Hang hinunter rollte, um dann" +
					" letztendlich in einem Rosenstrauch hängen zu bleiben. Nach der Erstversorgung wurde der " +
					"Verletzte auf Grund eines möglichen Bruches ins Krankenhaus eingeliefert.");
		insuranceCaseManager.createInsuranceCase(
			concreteTourCatalog.findAll().iterator().next(),
			userManager.findAll().toList().get(1).getUserAccount(),
				LocalDate.now(),
			"Beim Fahren entlang einer Straße wurde der Betroffene von einem anderen Verkehrsteilnehmer" +
			"erwischt. Dieser hatte laut Aussage der später eingetroffenen Polizei 1,7 Promille." +
			"Der Kunde wurde mit Prellungen und einer leichten Gehirnerschütterung ins Krankenhaus eingeliefert."
		);


	}
}
