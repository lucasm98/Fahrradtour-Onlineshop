package biketour.tour;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tour is the product of our web page
 * @author julian
 */
@Entity
public class Tour{

	private @Id @GeneratedValue long id;
	@Lob
	private String descriptionText;
	private String name;
	private String image;
	private int capacity;


	private ArrayList<Long> resupplyLocations;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@SuppressWarnings("unused")
	private Tour(){}

	/**
	 * Constructor for Entity tour
	 * @param name of the tour
	 * @param descriptionText describes the Tour in a few words
	 * @param image name of the .jpg file, saved in static.resources.img folder
	 * @param capacity users that can join the tour
	 * @param arrayList with a resupply stations
	 */
	public Tour(String name, String descriptionText, String image, int capacity, ArrayList<Long> arrayList){
		this.name = name;
		this.image = image;
		this.descriptionText = descriptionText;
		this.capacity = capacity;
		resupplyLocations = arrayList;
	}

	public ArrayList<Long> getResupplyLocations() {
		return resupplyLocations;
	}

	public String getName(){return name;}

	public void setName(String name) {this.name = name;}

	public String getDescriptionText(){return descriptionText;}

	public String getImage() { return image;}

	public int getCapacity(){return capacity;}

	public void setDescriptionText(String descriptionText){ this.descriptionText = descriptionText;}

	public void setCapacity(int capacity) {this.capacity = capacity;}

	public void setImage(String image) { this.image = image;}

	public long getId() {return id;}

	public void setId(long id) {this.id = id;}

	/**
	 * adds a new comment to the arraylist comments
	 * @param comment to be passed to the tour object
	 */
	public void addComment(Comment comment){comments.add(comment);}

	/**
	 * @return all comments for a tour
	 */
	public Iterable<Comment> getComments(){return comments;}
}
