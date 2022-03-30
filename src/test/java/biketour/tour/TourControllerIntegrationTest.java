package biketour.tour;

import biketour.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyIterable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TourControllerIntegrationTest extends AbstractIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired ConcreteTourController concreteTourController;
	@Autowired TourManager tourManager;
	@Autowired TourRepository tourRepository;

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void managerIsAccessibleForAdmin() throws Exception{
		mvc.perform(get("/management/tourManager"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("repository", is(not(emptyIterable()))));
	}

	@Test
	void preventsPublicAccessForManagerOverview() throws Exception{
		mvc.perform(get("/management/tourManager"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	/*
	@Test
	public void concreteTourControllerIntegrationTest() {

		Model model = new ExtendedModelMap();
		String returnedView = concreteTourController.getCatalog(model);
		assertThat(returnedView).isEqualTo("tourCatalog");
		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");
		assertThat(object).hasSize(7);
	}

	@Test
	public void concreteTourManagerIntegrationTest(){
		Model model = new ExtendedModelMap();
		String returnedView = concreteTourController.getConcreteTourManager(model);
		assertThat(returnedView).isEqualTo("tourConcreteManager");
		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");
		assertThat(object).hasSize(7);
	}*/

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void getTourForm() throws Exception{

		mvc.perform(get("/management/tourForm"))
			.andExpect(status().isOk())
			.andExpect(view().name("tourForm"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void updateTour() throws Exception{
		Tour tour = tourRepository.findAll().iterator().next();
		mvc.perform(get("/management/{id}/update", tour.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("tourEdit"));
	}
}

