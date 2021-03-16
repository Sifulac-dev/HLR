package fr.sifulac.plugin.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sifulac.plugin.Object.Maps;

public class Reflections {
	
	private static HashSet<Maps> maps = new HashSet<>();
	
	public static String msgSetHopper = "§eHopper mis en place !";
	
	public static Set<Maps> getMaps() {
		return maps;
	}
	
	public static ItemStack getHopper() {
		ItemStack item = new ItemStack(Material.HOPPER, 1);
		ItemMeta infos = item.getItemMeta();
		infos.setDisplayName("§eHopperMod");
		item.setItemMeta(infos);
		return item;
	}
	
}
