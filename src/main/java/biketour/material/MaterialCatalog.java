package biketour.material;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.domain.Sort;

import java.util.*;

public interface MaterialCatalog extends Catalog<Material> {

	Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();
	Iterable<Material> findByType(MaterialType type, Sort sort);





	/**
	 * Finds the Materials in the Catalog with the given Type
	 * @param type Material Type
	 * @return Iterable with Materials
	 */
	default Iterable<Material> findByType(MaterialType type) {
		return toUnique(findByType(type, DEFAULT_SORT));
	}


	/**
	 * is a function that gets an iterable an returns it with unique material
	 * @param iterable gives materials
	 * @return all materials
	 */
	default Iterable<Material> toUnique(Iterable<Material> iterable){
		HashMap<String,Material> map = new HashMap<>();
		ArrayList<Material> list = new ArrayList<>();
		for(Material material : iterable){
			if(!map.containsKey(material.getName())){
				map.put(material.getName(),material);
			}
		}
		map.forEach((k,v) -> {list.add(v);});
		return list;
	}


	/**
	 * gets all unique Material from given Materials
	 * @return all matierals that are unique
	 */
	default Iterable<Material> findUniqueMat(){
		HashMap<String,Material> map = new HashMap<>();
		ArrayList<Material> list = new ArrayList<>();
		for(Material material : findAll()){
			if(!map.containsKey(material.getName())){
				map.put(material.getName(),material);
			}
		}
		map.forEach((k,v) -> {list.add(v);});
		return list;
	}

	/**
	 * finds all materials that are equal to the given one
	 * @param inputMaterial material
	 * @return all materials that are equal
	 */
	default Iterable<Material> findSameMats(Material inputMaterial){
		ArrayList<Material> list = new ArrayList<>();
		for(Material material : findAll()){
			if(material!=null) {
				if (inputMaterial.getName() == material.getName()) {
					list.add(material);
				}
			}else{
				System.out.println("failed same mats");
			}
		}
		return list;
	}

	/**
	 * finds all materials with the given Location ID
	 * @param id of the material
	 * @return all materials with id
	 */
	default Iterable<Material> findByLoc(long id){
		ArrayList<Material> list = new ArrayList<>();
		for(Material material : findAll()){
			if(material.getLocid()==id){
				list.add(material);

			}
		}
		return list;
	}

	/**
	 * Finds the nearest search results in the Catalog and when there ar less then 3
	 * results, it returns the 3 best results
	 * @param name search input
	 * @return all materials with name
	 */
	default Iterable<Material> findByString(String name){
		ArrayList<Material> list =new ArrayList<>();
		Map<Double,Material> doubleMaterialHashMap = new HashMap<>();
		Map<Material, Double> materialDoubleHashMap = new HashMap<>();
		double[]close = new double[3];
		close[0]=0;
		close[1]=0;
		close[2]=0;

		for (Material material :findUniqueMat()) {

			materialDoubleHashMap.put(material, ( similarity(material.getName(), name)));
			doubleMaterialHashMap.put( similarity(material.getName(), name),material);
			System.out.println(name+" "+material.getName()+" : "+similarity(material.getName(),name)+
					" mem "+materialDoubleHashMap.get(material));
			if(returnIntFormDouble(materialDoubleHashMap.get(material))> returnIntFormDouble(close[0])){
				close[2]=close[1];
				close[1]=close[0];
				close[0]=materialDoubleHashMap.get(material);
			}
			if (returnIntFormDouble(materialDoubleHashMap.get(material))>returnIntFormDouble(close[1])
					&& returnIntFormDouble(materialDoubleHashMap.get(material)) != returnIntFormDouble(close[0])){
				close[2]=close[1];
				close[1]=materialDoubleHashMap.get(material);
			}
			if(returnIntFormDouble(materialDoubleHashMap.get(material))>returnIntFormDouble(close[2]) &&
					returnIntFormDouble(materialDoubleHashMap.get(material)) !=
							returnIntFormDouble(close[0])&&
					returnIntFormDouble(materialDoubleHashMap.get(material)) !=
							returnIntFormDouble(close[1])){
				close[2]=materialDoubleHashMap.get(material);
			}

			if( material.getName().contains(name)
				||(0.5<similarity(material.getName(),name)) ){
				list.add(material);
			}
		}
		if(list.size()<3){
			System.out.println("Add weil zu wenig");
			list.clear();
			list.add(doubleMaterialHashMap.get(close[0]));
			list.add(doubleMaterialHashMap.get(close[1]));
			list.add(doubleMaterialHashMap.get(close[2]));
			System.out.println(close[0]+" : "+close[1]+" : "+close[2]);
		}
		return list;
	}

	public static int returnIntFormDouble(double input){
		return (int) Math.round(input * 100);
	}

	/**
	 * this Method compares to Strings
	 * @param s1 String
	 * @param s2 String
	 * @return Double between 0 and 1
	 */
	public static double similarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater length
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0){
			return 1.0; /* both strings are zero length */
		}
    /* // If you have Apache (bleibt gleich) Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	}

	// Example implementation of the Levenshtein Edit Distance
	// See http://rosettacode.org/wiki/Levenshtein_distance#Java
	/**
	 * @param s1 String
	 * @param s2 String
	 * @return the distance
	 */
	public static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0){
					costs[j] = j;
				}else{
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1)){
							newValue = Math.min(Math.min(newValue, lastValue),costs[j]) + 1;
						}
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0){
				costs[s2.length()] = lastValue;
			}
		}
		return costs[s2.length()];
	}
}
