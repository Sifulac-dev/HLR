package fr.sifulac.plugin;

import java.util.HashSet;
import java.util.Set;

import fr.sifulac.plugin.Object.Maps;

public class Reflections {
	
	private static HashSet<Maps> maps = new HashSet<>();
	
	public static String msgSetHopper = "§eHopper mis en place !";
	
	public static Set<Maps> getMaps() {
		return maps;
	}
}
