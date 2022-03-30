package biketour.tour;

import biketour.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConcreteTourControllerIntegrationTest extends AbstractIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired ConcreteTourCatalog catalog;
	@Autowired TourRepository tourRepository;

	@Test
	void getCatalog() throws Exception{
		mvc.perform(get("/tours"))
			.andExpect(status().isOk())
			.andExpect(view().name("tourCatalog"));
	}

	@Test
	void getDetail() throws Exception{
		ConcreteTour tour = catalog.findAll().iterator().next();
		mvc.perform(get("/tour/{tour}", tour.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("tourDetail"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void getConcreteTourManager() throws Exception{
		mvc.perform(get("/management/concreteTourManager"))
			.andExpect(status().isOk())
			.andExpect(view().name("tourConcreteManager"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void getConcreteTourForm() throws Exception{
		Tour tour = tourRepository.findAll().iterator().next();
		mvc.perform(get("/management/{id}/concreteTourForm", tour.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("tourConcreteForm"));
	}

	@Test
	@WithMockUser(username = "Frodo95", roles = "TOURGUIDE")
	void coachIsAccessibleForTourGuide() throws Exception{
		mvc.perform(get("/tourCoach"))
			.andExpect(status().isOk())
			.andExpect(view().name("tourCoach"));
	}

	@Test
	void preventsPublicAccessForTourCoach() throws Exception{
		mvc.perform(get("/tourCoach"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	void tourResupply() throws Exception{
		ConcreteTour tour = catalog.findAll().iterator().next();
		mvc.perform(get("/tourResupply/{tour}", tour.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("tourResupply"));
	}



}
