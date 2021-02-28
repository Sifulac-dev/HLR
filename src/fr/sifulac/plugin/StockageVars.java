package fr.sifulac.plugin;

import java.util.ArrayList;
import java.util.List;

public class StockageVars {

	private List<HopperObject> activeHopper = new ArrayList<>();
	private List<HopperObject> inactiveHopper = new ArrayList<>();
	
	public List<HopperObject> getInactiveHopper() {
		return inactiveHopper;
	}
	public void setInactiveHopper(List<HopperObject> inactiveHopper) {
		this.inactiveHopper = inactiveHopper;
	}
	public List<HopperObject> getActiveHopper() {
		return activeHopper;
	}
	public void setActiveHopper(List<HopperObject> activeHopper) {
		this.activeHopper = activeHopper;
	}
	
}
