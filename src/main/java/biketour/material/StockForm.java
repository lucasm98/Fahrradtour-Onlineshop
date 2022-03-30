package biketour.material;

import org.salespointframework.core.SalespointIdentifier;

import javax.validation.constraints.NotEmpty;

public class StockForm {
	@NotEmpty(message = "{MaterialForm.name.NotEmpty}")
	private final String ids;

	public StockForm(String ids){
		this.ids = ids;
	}

	String getIDs() {
		return ids;
	}
}
