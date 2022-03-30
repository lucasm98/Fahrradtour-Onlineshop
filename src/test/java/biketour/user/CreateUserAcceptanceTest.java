package biketour.user;

import biketour.AbstractIntegrationTests;
import biketour.register.Registration;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class CreateUserAcceptanceTest extends AbstractIntegrationTests {

	private final String USERNAME = "user92";
	private final String FIRSTNAME = "john";
	private final String LASTNAME = "reed";
	private final String EMAIL = "user92@gmx.de";
	private final String PASSWORT = "123";
	private final String STREET = "village way";
	private final String NUMBER = "12";
	private final String CITYCODE = "12345";
	private final String CITY = "phoenix";


	/**
	 * This method creates a new user and the associated user account using the registration form.
	 * If the process works without mistakes, a new user will be returned.
	 */
//	@Test
//	void createNewUser() {
//		UserRepository userRepository = mock(UserRepository.class);
//		when(userRepository.save(any())).then(x -> x.getArgument(0));
//
//		UserAccountManager userAccountManager = mock(UserAccountManager.class);
//		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());
//
//		UserManager userManager = new UserManager(userRepository, userAccountManager);
//
//		User newUser = userManager.createUser(new Registration(USERNAME, FIRSTNAME, LASTNAME, EMAIL, PASSWORT,
//				STREET, NUMBER, CITYCODE, CITY), Role.of("CUSTOMER"));
//
//		verify(userAccountManager, times(1))
//				.create(eq(USERNAME),
//						eq(Password.UnencryptedPassword.of(PASSWORT)),
//						eq(Role.of("CUSTOMER")));
//
//		Assert.notNull(newUser, "No new user created.");
//	}
}

