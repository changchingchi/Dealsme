package com.sim.dealsme;

import java.util.Comparator;
import java.util.HashMap;

public class CustomComparatorRating implements Comparator<HashMap<String, String>> {


	@Override
	public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
		
		return rhs.get("StoreRatingIndex").compareTo(lhs.get("StoreRatingIndex"));
		
	}
    

	
}
