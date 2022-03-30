package biketour.user;

import biketour.register.Registration;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Marcel Körner
 */
@Component
@Order(value = 1)
public class UserDataInitializer implements DataInitializer {

	private final String userNameBoss = "boss";
	private final String defaultUserName = "Frodo95";
	private final String defaultUserName2 = "Gandalf93";
	private final String defaultUserName3 = "Pedda";
	private final String defaultFirstName = "Frederick";
	private final String defaultFirstName2 = "Fridolin";
	private final String defaultFirstName3 = "Saftsack";
	private final String defaultLastName = "Winkler";
	private final String defaultLastName2 = "Der Graue";
	private final String defaultLastName3 = "Von Oben";
	private final String defaultEmail = "Frodo95@hotmail.de";
	private final String defaultEmail2 = "gandalf93@hotmail.de";
	private final String defaultEmail3 = "ganjalf@hotmail.de";
	private final String defaultPassword = "123";
	private final String defaultStreet = "Avenue";
	private final String defaultNumber = "1";
	private final String defaultCityCode = "12345";
	private final String defaultCity = "Home Town";

	private final UserAccountManager userAccountManager;
	private final UserManager userManager;


	UserDataInitializer(UserAccountManager userAccountManager, UserManager userManager) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userManager, "UserManager must not be null!");

		this.userAccountManager = userAccountManager;
		this.userManager = userManager;
	}

	@Override
	public void initialize() {

		userAccountManager.create(userNameBoss, Password.UnencryptedPassword.of(defaultPassword), Role.of("BOSS"));

		List.of(//
			new Registration(defaultUserName, defaultFirstName, defaultLastName, defaultEmail,
					defaultPassword, defaultStreet, defaultNumber, defaultCity, defaultCityCode),
			new Registration(defaultUserName2, defaultFirstName2, defaultLastName2, defaultEmail2,
					defaultPassword, defaultStreet, defaultNumber, defaultCity, defaultCityCode),
		new Registration(defaultUserName3, defaultFirstName3, defaultLastName3, defaultEmail3,
				defaultPassword, defaultStreet, defaultNumber, defaultCity, defaultCityCode),
				new Registration("frankguenther1965","Günther", "Frank",
						"frankguenther1965@hotmail.de", "123", "Lindenstraße",
						"4", "Nürnberg", "90402"),
				new Registration("BH" ,"Brunhilde", "Heuss",
						"brunhildeHeuss@web.de", "123", "Promilleweg", "23",
						"Mannheim", "12345"),
				new Registration("Hofen","Eugen", "Hofmann", "xXeugenHofmannXx@gmail.com",
						"123","Goethe-Straße", "12", "Karlsruhe", "12345"),
				new Registration("Potto", "Peter", "Otto", "peterotto@gmx.de",
						"123", "Carl-Marx-Straße", "14", "Checmnitz", "12345")
		).forEach(userManager::createUser);
		userAccountManager.findByUsername(defaultUserName).get().add(Role.of("TOURGUIDE"));
	}
}
