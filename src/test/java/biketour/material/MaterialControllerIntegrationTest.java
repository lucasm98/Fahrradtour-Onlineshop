package biketour.material;

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
public class MaterialControllerIntegrationTest {

	@Autowired MockMvc mvc;
	@Autowired MaterialCatalog catalog;
	@Autowired MaterialManager manager;

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void managerIsAccessibleForAdmin() throws Exception{
		mvc.perform(get("/management/materialManager"))
			.andExpect(status().isOk())
			.andExpect(view().name("materialManager"));
	}

	@Test
	void preventsPublicAccessForManagerOverview() throws Exception{
		mvc.perform(get("/management/materialManager"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void materialDetail() throws Exception{
		Material material = catalog.findAll().iterator().next();
		mvc.perform(get("/management/material/{material}", material.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("materialDetail"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void materialVersorgung() throws Exception{
		mvc.perform(get("/management/material/versorgung"))
			.andExpect(status().isOk())
			.andExpect(view().name("materialManager"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void materialErsatz() throws Exception{
		mvc.perform(get("/management/material/ersatz"))
			.andExpect(status().isOk())
			.andExpect(view().name("materialManager"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void editMaterial() throws Exception{
		Material material = catalog.findAll().iterator().next();
		mvc.perform(get("/management/materialedit/{material}", material.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("materialEdit"));
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void stock() throws Exception{

		mvc.perform(get("/management/materialStock"))
			.andExpect(status().isOk())
			.andExpect(view().name("materialStock"));
	}


}
