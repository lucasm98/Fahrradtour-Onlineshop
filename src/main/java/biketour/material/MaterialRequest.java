package biketour.material;

import biketour.resupply.Resupply;

public class MaterialRequest {

	public enum Status {
		CONFIRMED("Confirmed"),
		OPEN("Open"),
		DENIED("Denied");

		private final String statusName;

		private Status(String statusName) {
			this.statusName = statusName;
		}

		public String toString() {
			return statusName;
		}


	}

	private int id;
	private int anzahl;



	private Resupply resupply;
	private Material material;
	private boolean finish;
	private Status status;



	public MaterialRequest(int id, int anzahl, Resupply resupply, Material material, Status status) {

		this.id=id;
		this.anzahl=anzahl;
		this.resupply=resupply;
		this.material=material;
		finish=false;
		this.status=status;
	}

	public void finsh(){
		finish=true;
	}

	public void toggel(){
		if(finish){
			finish=false;
		}else{
			finish=true;
		}
	}

	public int getId() {
		return id;
	}
	public String idToString(){
		return id+"";
	}

	public int getAnzahl() {
		return anzahl;
	}

	public Resupply getResupply() {
		return resupply;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean isFinish() {
		return finish;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
