package biketour.material;

import biketour.resupply.Resupply;
import biketour.resupply.ResupplyCatalog;
import biketour.resupply.ResupplyManager;
import com.mysema.commons.lang.Assert;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class MaterialManager {

	@Autowired
	private final MaterialCatalog catalog;
	private ResupplyCatalog resupplyCatalog;
	private ResupplyManager resupplyManager;
	private ArrayList<MaterialRequest> materialRequests;


	public MaterialManager(MaterialCatalog catalog,ResupplyManager resupplyManager){
		Assert.notNull(catalog,"Tour Catalog must not be null");
		this.catalog=catalog;
		this.resupplyManager=resupplyManager;
		this.materialRequests = new ArrayList<>();
	}


	/**
	 * Creates an Material out of the Data from the Website.
	 * @param form Materialdata that has been committed
	 */
	public void  createMaterial(MaterialForm form){
		Assert.notNull(form, "Tour Form must not be empty");
		ArrayList<Long> longArrayList = new ArrayList<>();
		for(String s:form.getLoc().split(",")){
			longArrayList.add(Long.parseLong(s));
		}
		System.out.println("Size" +" | "+form.getLoc());
		for(long i : longArrayList){
			Resupply resupply = resupplyManager.findResupplyById(i).get();
			if (resupplyManager.findResupplyById(i).isEmpty()){
				throw new IllegalArgumentException("Cannot be empty");
			}
			Material ret = new Material(form.getType().contains("versorgung")? MaterialType.ERSATZTEIL :MaterialType.VERSORGUNG,
										form.getName(),
										form.getDescription(),
										Quantity.of(Integer.parseInt(form.getQuantity())),
										i,
										resupply.getName());
			catalog.save(ret);
		}
	}

	/**
	 * Creates an Material out of the Data from the Website.
	 * @param form Materialdata that has been committed
	 * @param inputMaterial material that will be edited
	 */
	public void editMaterial(MaterialForm form,Material inputMaterial){
		Assert.notNull(form, "Tour Form must not be empty");
		ArrayList<Material> deleteList = new ArrayList<>();
		for(Material material:findSameMats(inputMaterial)){
			deleteList.add(material);
		}
		System.out.println("Edit Material : "+deleteList.size()+": is|");
		createMaterial(form);
		catalog.deleteAll(deleteList);
	}

	/**
	 * deletes the given Material
	 * @param inputMaterial that will be deleted
	 */
	public void deleteMaterial(Material inputMaterial){
		Assert.notNull(inputMaterial,"Material must not be null");
		System.out.println("Delete mat "+inputMaterial.getName());
		for(Material material : catalog.findSameMats(inputMaterial)){
			catalog.delete(material);
		}
	}

	/**
	 * decrease the given Materials Quantity
	 * @param inputMaterial that will be decreased
	 */
	public void decQuantity(Material inputMaterial){
		Assert.notNull(inputMaterial,"Material must not be null");
		if(!inputMaterial.getQuantity().isZeroOrNegative()) {
			inputMaterial.setQuantity(inputMaterial.getQuantity().add(Quantity.of(-1)));
			catalog.save(inputMaterial);
		}

	}

	/**
	 * increase the given Materials Quantity
	 * @param inputMaterial that will be increased
	 */
	public void addQuantity(Material inputMaterial){
		Assert.notNull(inputMaterial,"Material must not be null");
		inputMaterial.setQuantity(inputMaterial.getQuantity().add(Quantity.of(1)));
		catalog.save(inputMaterial);
	}


	/**
	 * gets all Materials
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findAll() {
		return catalog.findAll();
	}

	/**
	 * gets all Materials with Type Versorgung
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findVersorgung() {
		return catalog.findByType(MaterialType.VERSORGUNG);
	}

	/**
	 * gets all Materials with Type Ersatzteile
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findErsatz() {
		return catalog.findByType(MaterialType.ERSATZTEIL);
	}

	/**
	 * gets all Materials that are most equal to the given Search input
	 * @param name Search Input
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findName(String name) {
		Assert.notNull(name,"name must not be null");
		return catalog.findByString(name);
	}

	/**
	 * this Method finds all Material that is equal to the given ones
	 * @param inputMaterial that will be searched
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findSameMats(Material inputMaterial){
		Assert.notNull(inputMaterial,"Material must not be null");
		return getActiv(catalog.findSameMats(inputMaterial));
	}

	/**
	 * this Method finds all Unique Material
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findAllUnique(){return catalog.findUniqueMat();}

	/**
	 * this Method finds all Material where the LocationId is similar to the given ones
	 * @param id of the material
	 * @return Iterable of Materials
	 */
	public Iterable<Material> findByLoc(long id){
		Assert.notNull(id,"Id must not be null");
		return catalog.findByLoc(id);
	}


	/**
	 * @return all materials with quantity lower than 3
	 */
	public Iterable<Material> getLowMaterials(){
		ArrayList<Material> returnList = new ArrayList<>();
		for(Material material : findAllActiv()){
			if(material.getQuantity().isLessThan(Quantity.of(3))){
				returnList.add(material);
			}
		}
		return returnList;
	}

	/**
	 * restocks all given Materials with 3
	 * @param ids of materials with the quantity 3
	 */
	public void reStock(String ids){
		ArrayList<String> idArray = new ArrayList<>();
		for(String s:ids.split(",")){
			idArray.add(s);
		}
		for(String id : idArray){
			for(Material material : findAllActiv()){
				if(material.getId().toString().equals(id)){
					//material.setQuantity(Quantity.of(3));
					createRequest(3,material);
				}
			}
		}
		System.out.println("Success restock");
	}

	/**
	 * @return all materials that are active
	 */
	private Iterable<Material> findAllActiv(){
		ArrayList<Material> returnList = new ArrayList<>();
		for(Material material:catalog.findAll()) {
			if((resupplyManager)==null){
				System.out.println("mem");
			}

			for (Resupply resupply : resupplyManager.findAllActive()) {
				if(material.getLocid()==resupply.getId()){
					returnList.add(material);
				}
			}
		}
		return  returnList;
	}

	/**
	 * @param iterable all materials
	 * @return materials that are active
	 */
	private Iterable<Material> getActiv(Iterable<Material> iterable){
		ArrayList<Material> returnList = new ArrayList<>();
		for(Material material:iterable) {
			for (Resupply resupply : resupplyManager.findAllActive()) {
				if(material.getLocid()==resupply.getId()){
					returnList.add(material);
				}
			}
		}
		return  returnList;
	}

	public ArrayList<MaterialRequest> getAllRequests(){
		return materialRequests;
	}
	public ArrayList<MaterialRequest> getAllActivRequests() {
		ArrayList<MaterialRequest> returnList=new ArrayList<>();
		for(MaterialRequest request:getAllRequests()){
			if(!request.isFinish()){
				returnList.add(request);
			}
		}
		return returnList;
	}

	public void createRequest(int count,Material material){

		materialRequests.add(new MaterialRequest(materialRequests.size(),count,
				resupplyManager.findResupplyById(material.getLocid()).get(),material, MaterialRequest.Status.OPEN));
	}

//	public void toggel(int id){
//		MaterialRequest request = materialRequests.get(id);
//		request.toggel();
//		Material material = catalog.findById(request.getMaterial().getId()).get();
//		Quantity quantity = material.getQuantity();
//		if(!request.isFinish()){
//			material.setQuantity(quantity.subtract(Quantity.of(request.getAnzahl())));
//		}else{
//			material.setQuantity(quantity.add(Quantity.of(request.getAnzahl())));
//		}
//	}

//	public void setRequestStatus(int id ,String status){
//		materialRequests.get(id).setStatus(MaterialRequest.Status.valueOf(status));
//	}
//
//	public void toggleStatusGreen(int id) {
//		MaterialRequest materialRequest = materialRequests.get(id);
//		Material material = catalog.findById(materialRequest.getMaterial().getId()).get();
//		Quantity quantity = material.getQuantity();
//		if (materialRequest.getStatus().equals(MaterialRequest.Status.OPEN)) {
//			material.setQuantity(quantity.add(Quantity.of(materialRequest.getAnzahl()))); //confirmed
//			materialRequest.setStatus(MaterialRequest.Status.CONFIRMED);
//		} else if (materialRequest.getStatus().equals(MaterialRequest.Status.CONFIRMED)) {
//			material.setQuantity(quantity.subtract(Quantity.of(materialRequest.getAnzahl()))); //undo
//			materialRequest.setStatus(MaterialRequest.Status.OPEN);
//
//		}
//	}

//	public void toggleStatusRed ( int id){
//		MaterialRequest materialRequest = materialRequests.get(id);
//		Material material = catalog.findById(materialRequest.getMaterial().getId()).get();
//		Quantity quantity = material.getQuantity();
//		if (materialRequest.getStatus().equals(MaterialRequest.Status.OPEN)) {
//			materialRequest.setStatus(MaterialRequest.Status.DENIED);
//		} else if (materialRequest.getStatus().equals(MaterialRequest.Status.DENIED)) {
//			materialRequest.setStatus(MaterialRequest.Status.OPEN); //undo
//
//		}
//	}

}
