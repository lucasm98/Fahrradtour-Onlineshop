package biketour.purchase;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseContollerIntegrationTest extends AbstractIntegrationTests {

	@Autowired
	MockMvc mvc;

//	@Test
//	@WithMockUser(username = "boss", roles = "BOSS")
//	void managerIsAccessibleForAdmin() throws Exception{
//		mvc.perform(get("/purchaseManager"))
//			.andExpect(status().isOk())
//			.andExpect(model().attribute("purchases", is(not(emptyIterable()))));
//	}

	@Test
	void preventsPublicAccessForManagerOverview() throws Exception{
		mvc.perform(get("/management/PurchaseManager"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}
}