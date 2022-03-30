package biketour.material;



import javax.validation.constraints.NotEmpty;


public class MaterialForm {

		@NotEmpty(message = "{MaterialForm.name.NotEmpty}")
		private final String name;

		@NotEmpty(message = "{MaterialForm.description.NotEmpty}")
		private final String description;

		@NotEmpty(message = "{MaterialForm.type.NotEmpty}")
		private final String type;

		@NotEmpty(message = "{MaterialForm.quantity.NotEmpty}")
		private final String quantity;

		@NotEmpty(message = "{MaterialForm.loc.NotEmpty}")
		private final String loc;


		public MaterialForm(String name, String description, String type,String quantity,String loc){
			this.name = name;
			this.description = description;
			this.type=type;
			this.quantity=quantity;
			this.loc=loc;
		}
		String getName() {
			return name;
		}
		String getDescription() {
			return description;
		}
		String getType() { return type;}
		String getQuantity(){return  quantity; }
		String getLoc(){return loc;}
	}