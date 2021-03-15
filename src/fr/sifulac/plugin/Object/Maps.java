package fr.sifulac.plugin.Object;

import java.util.HashSet;

public class Maps {

	private String name;
	private HashSet<Region> regions;
	
	public Maps(String name) {
		this.name = name; 
		regions = new HashSet<>();
	}
	
	public HashSet<Region> getRegions() {
		return regions;
	}
	public void setRegions(HashSet<Region> regions) {
		this.regions = regions;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
