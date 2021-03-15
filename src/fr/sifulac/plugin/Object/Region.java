package fr.sifulac.plugin.Object;

import java.util.ArrayList;
import java.util.List;

public class Region {

	private String id;
	private List<HopperObject> hopperAsChunk;
	
	public Region(String id) {
		this.id = id;
		this.hopperAsChunk = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<HopperObject> getHoppersInRegions() {
		return hopperAsChunk;
	}
	public void setRegion(List<HopperObject> hoppers) {
		this.hopperAsChunk = hoppers;
	}
	
	public Boolean addHopper(HopperObject hopper) {		
		return hopperAsChunk.add(hopper);		
	}
	
	public Boolean removeHopper(HopperObject hopper) {
		return hopperAsChunk.remove(hopper);
	}
}
