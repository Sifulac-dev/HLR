package fr.sifulac.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class StockageVars {

	private List<HopperObject> inactiveHopper = new ArrayList<>();
		
	private HashMap<String, HashSet<String>> hopper = new HashMap<>();
	
	public List<HopperObject> getInactiveHopper() {
		return inactiveHopper;
	}
	public void setInactiveHopper(List<HopperObject> inactiveHopper) {
		this.inactiveHopper = inactiveHopper;
	}
	
	public Boolean createHopper(int chunkX, int chunkZ) {
		if()
	}
	
	
	public Boolean isContains(int chunkX, int chunkZ) {		
		int x, z;
		x = chunkX / 32;
		z = chunkZ / 32;		
		String region = String.valueOf(x+z);		
		if(!hopper.containsKey(region)) return false;		
		if(!hopper.get(region).contains(String.valueOf(chunkX+chunkZ))) return false;
		
		return true;		
	}	
}
