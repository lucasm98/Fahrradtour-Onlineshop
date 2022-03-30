package biketour.user;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.Iterator;

/**
 * @author Marcel Körner
 */
@Entity
public class User {

	private @Id
	@GeneratedValue
	long id;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	@OneToOne
	private UserAccount userAccount;

	private User() {
	}

	public User(UserAccount userAccount, Address address) {
		this.address = address;
		this.userAccount = userAccount;
	}

	@Override
	public String toString() {
		return "[" + this.getId() + "]\n" +
			"[" + this.getUserAccount().getUsername() + "]\n" +
			this.getAddress().toString();
	}

	public long getId() {
		return id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}


	public String rolesToString() {
		Iterator<Role> roles = this.userAccount.getRoles().get().iterator();

		String output = "";
		while(roles.hasNext()) {
			output += "[" + roles.next().toString() + "]";
		}
		return output;
	}
}
