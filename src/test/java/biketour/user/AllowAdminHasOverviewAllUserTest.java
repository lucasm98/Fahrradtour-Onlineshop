package biketour.user;

import biketour.AbstractIntegrationTests;
import biketour.user.UserController;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

public class AllowAdminHasOverviewAllUserTest extends AbstractIntegrationTests {

	@Autowired
	private UserController userController;
	private ExtendedModelMap extendedModelMap;

	/**
	 * This method simulates a user having role 'BOSS' and checks if this user has an overview with all users.
	 */

//	@Test
//	@WithMockUser(roles = "BOSS")
//	void allowAdminHasOverviewAllUser() {
//		extendedModelMap = new ExtendedModelMap();
//		userController.userManagerGet(extendedModelMap);
//		Assert.notNull(extendedModelMap.get("userList"), "There is no overview");
//	}
}

