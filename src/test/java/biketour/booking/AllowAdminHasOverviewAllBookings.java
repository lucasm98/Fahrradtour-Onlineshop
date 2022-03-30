package biketour.booking;

import biketour.AbstractIntegrationTests;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

public class AllowAdminHasOverviewAllBookings extends AbstractIntegrationTests {



	@Autowired
	private BookingController bookingController;
	@Autowired
	private UserAccountManager userAccountManager;
	private ExtendedModelMap extendedModelMap;

	/**
	 * This method simulates a user having role 'BOSS' and checks if this user has an overview with all bookings.
	 */
//	@Test
//	@WithMockUser(roles = "BOSS")
//	void allowAdminHasOverviewAllUser() {
//		extendedModelMap = new ExtendedModelMap();
//		bookingController.bookingManagerGet(extendedModelMap,
//				userAccountManager.findByUsername("boss"));
//		Assert.notNull(extendedModelMap.get("bookings"), "There is no overview");
//	}



}
