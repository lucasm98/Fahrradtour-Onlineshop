package biketour.user;

import biketour.booking.Booking;
import biketour.booking.BookingManager;
import biketour.purchase.Purchase;
import biketour.purchase.PurchaseManager;
import biketour.register.Registration;
import biketour.update.PasswordUpdateForm;
import biketour.update.UserUpdateForm;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Marcel Körner
 */
@Service
@Transactional
@Order(value = 2)
public class UserManager {

	public final static Role CUSTOMER_ROLE = Role.of("CUSTOMER");

	private UserRepository userRepository;
	@Autowired
	private UserAccountManager userAccountManager;
	private final BookingManager bookingManager;
	private final PurchaseManager purchaseManager;

	public UserManager(UserRepository userRepository, UserAccountManager userAccountManager,
					   BookingManager bookingManager, PurchaseManager purchaseManager) {

		Assert.notNull(userRepository, "CustomerRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(bookingManager, "BookingManager must not be null!");
		Assert.notNull(purchaseManager, "PurchaseManager must not be null!");

		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
		this.bookingManager = bookingManager;
		this.purchaseManager = purchaseManager;
	}

	/**
	 * @param registration from that gives all the needed information to create a new user
	 * @return a new user
	 */
	public User createUser(Registration registration) {

		Assert.notNull(registration, "CustomerRepository must not be null!");

		UserAccount newUserAccount = userAccountManager.create(
			registration.getUserName(),
			Password.UnencryptedPassword.of(registration.getPassword()),
			CUSTOMER_ROLE);

		newUserAccount.setLastname(registration.getLastName());
		newUserAccount.setFirstname(registration.getFirstName());
		newUserAccount.setEmail(registration.getEmail());

		//newUserAccount.add(Role.of("TOURGUIDE"));
		newUserAccount.add(Role.of("CUSTOMER"));

		return userRepository.save(
			new User(newUserAccount,
				new Address(registration.getStreet(), registration.getNumber(),
					registration.getCityCode(), registration.getCity())));
	}

	/**
	 * @return all users
	 */
	public Streamable<User> findAll() {
		return userRepository.findAll();
	}

	/**
	 * @param userName search term to get a user
	 * @return a user with the given name
	 */
	public Optional<UserAccount> findUserAccountByUserName(String userName) {
		return userAccountManager.findByUsername(userName);
	}

	/**
	 * adds a role to a given useraccout
	 * @param userAccount that gets another role
	 */
	public void addRoleTourGuide(UserAccount userAccount) {
		userAccountManager.get(userAccount.getId()).get().add(Role.of("TOURGUIDE"));
	}

	/**
	 * removes a role from a tour guide
	 * @param userAccount useraccount that gets the role removed
	 */
	public void removeRoleTourGuide(UserAccount userAccount) {
		userAccountManager.get(userAccount.getId()).get().remove(Role.of("TOURGUIDE"));
	}

	/**
	 * checks if useraccout is tourguide
	 * @param userAccount that will be checked
	 * @return true if useraccout is tourguide
	 */
	public boolean isTourGuide(UserAccount userAccount) {
		return userAccountManager.get(userAccount.getId()).get().hasRole(Role.of("TOURGUIDE"));
	}

	/**
	 *
	 * @param role to search the usercounts for
	 * @return all useraccounts that have the given role
	 */
	public Streamable<UserAccount> findByRole(Role role){
		return userAccountManager.findAll().filter(account ->
				account.hasRole(role));
	}

	/**
	 * @return the useraccoutmanager
	 */
	public UserAccountManager getUserAccountManager(){
		return userAccountManager;
	}

	public Optional<User> findUserByUserAccount(UserAccount userAccount) {
		return userRepository.findAll().filter(user -> user.getUserAccount().equals(userAccount)).get().findFirst();
	}

	public boolean updateUserPassword(UserAccount userAccount, PasswordUpdateForm passwordUpdateForm) {
		if(passwordUpdateForm.getNewPassword().equals(passwordUpdateForm.getNewPasswordConfirm())) {
			this.userAccountManager.changePassword(userAccount, Password.UnencryptedPassword.
					of(passwordUpdateForm.getNewPassword()));
			return true;
		}
		return false;
	}

	public boolean updateUserInformation(UserAccount userAccount, UserUpdateForm userUpdateForm) {
		userAccount.setFirstname(userUpdateForm.getFirstName());
		userAccount.setLastname(userUpdateForm.getLastName());
		userAccount.setEmail(userUpdateForm.getEmail());
		this.findUserByUserAccount(userAccount).get().setAddress(
				new Address(
						userUpdateForm.getStreet(),
						userUpdateForm.getNumber(),
						userUpdateForm.getCityCode(),
						userUpdateForm.getCity()
				)
		);
		return true;
	}

	public int getCreditsOfUser(UserAccount userAccount) {
		int creditsFromBookings = 0;
		Iterator<Booking> iteratorBooking = bookingManager.getBookingsFromUserAccount(userAccount)
				.filter(booking -> booking.getOrderStatus().equals(OrderStatus.COMPLETED)).iterator();
		while(iteratorBooking.hasNext()) {
			creditsFromBookings += iteratorBooking.next().getOrderLine().getQuantity().getAmount().intValue();
		}
		int creditsUsedForPurchases = 0;
		Iterator<Purchase> iteratorPurchase = purchaseManager.getPurchasesFromUserAccount(userAccount).iterator();
		while (iteratorPurchase.hasNext()) {
			creditsUsedForPurchases += iteratorPurchase.next().getPoints();
		}
		if(creditsFromBookings - creditsUsedForPurchases < 0) {
			return 0;
		} else {
			return creditsFromBookings - creditsUsedForPurchases;
		}
	}
}
