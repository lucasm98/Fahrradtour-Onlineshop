package biketour.booking;

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
public class BookingControllerIntegrationTest extends AbstractIntegrationTests {
	@Autowired MockMvc mvc;

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void managerIsAccessibleForAdmin() throws Exception{
		mvc.perform(get("/management/bookingManager"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("repository", is(not(emptyIterable()))));
	}

	@Test
	void preventsPublicAccessForManagerOverview() throws Exception{
		mvc.perform(get("/management/bookingManager"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(username = "Gandalf93", roles = "CUSTOMER")
	void myBookingGet() throws Exception{
		mvc.perform(get("/myBooking"))
			.andExpect(view().name("bookingList"));
	}

	@Test
	void myBookingGetWithoutUser() throws Exception{
		mvc.perform(get("/myBooking"))
			.andExpect(view().name("redirect:/"));
	}
}
