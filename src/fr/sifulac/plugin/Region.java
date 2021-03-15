package fr.sifulac.plugin;

import java.util.ArrayList;
import java.util.List;

public class Region {

	private String id;
	private List<HopperObject> hopperByChunks;
	
	public Region(String id) {
		this.id = id;
		this.hopperByChunks = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<HopperObject> getHopperInRegion() {
		return hopperByChunks;
	}
	public void setRegion(List<HopperObject> hoppers) {
		this.hopperByChunks = hoppers;
	}
	
	public Boolean addHopper(HopperObject hopper) {		
		return hopperByChunks.add(hopper);		
	}
	
	public Boolean removeHopper(HopperObject hopper) {
		return hopperByChunks.remove(hopper);
	}
}
