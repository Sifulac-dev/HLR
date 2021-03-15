package fr.sifulac.plugin;

import java.util.HashSet;
import java.util.Set;

public class Reflections {
	
	private static HashSet<Region> regions = new HashSet<>();
	
	public static Set<Region> getRegions() {
		return regions;
	}
}
