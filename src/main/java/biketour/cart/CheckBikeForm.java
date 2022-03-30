package biketour.cart;

import org.salespointframework.core.SalespointIdentifier;

import javax.validation.constraints.NotEmpty;

public class CheckBikeForm {
	private final String ids;

	public CheckBikeForm(String ids){
		this.ids = ids;
	}

	String getIDs() {
		return ids;
	}
}
