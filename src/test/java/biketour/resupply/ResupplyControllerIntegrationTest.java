package biketour.resupply;

import biketour.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyIterable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResupplyControllerIntegrationTest extends AbstractIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired ResupplyCatalog catalog;

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void managerIsAccessibleForAdmin() throws Exception{
		mvc.perform(get("/management/resupplyManager"))
			.andExpect(status().isOk())
			.andExpect(view().name("resupplyManager"));
	}

	@Test
	void preventsPublicAccessForManagerOverview() throws Exception{
		mvc.perform(get("/management/resupplyManager"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void resupplyDetails() throws Exception{
		Resupply resupply = catalog.findAll().iterator().next();

		mvc.perform(get("/management/resupplyStation/{id}", resupply.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("resupplyDetails"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void getResupplyForm() throws Exception{

		mvc.perform(get("/management/resupplyManager/resupplyForm"))
			.andExpect(status().isOk())
			.andExpect(view().name("resupplyForm"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void getResupplyEditForm() throws Exception{
		Resupply resupply = catalog.findAll().iterator().next();

		mvc.perform(get("/management/resupplyManager/editResupply/{id}", resupply.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("resupplyEditForm"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void createResupply() throws Exception{

		mvc.perform(get("/management/resupplyManager/resupplyForm"))
			.andExpect(status().isOk())
			.andExpect(view().name("resupplyForm"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void toggleState() throws Exception{
		Resupply resupply = catalog.findAll().iterator().next();

		mvc.perform(post("/management/resupplyManager/toggleState/{id}", resupply.getId()))
			.andExpect(view().name("redirect:/management/resupplyManager"));
	}




}
