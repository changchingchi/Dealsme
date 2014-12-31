package com.sim.dealsme;

import java.util.Comparator;
import java.util.HashMap;

public class CustomComparatorDistance implements Comparator<HashMap<String, String>> {


	@Override
	public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
		
		return lhs.get("StoreDistance").compareTo(rhs.get("StoreDistance"));
		
	}
    

	
}
