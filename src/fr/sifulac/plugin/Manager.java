package fr.sifulac.plugin;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Manager {

	public static boolean containHopper(String id, List<HopperObject> listHopper) {
		return (listHopper.stream().filter(hp -> id.equals(hp.getId())).findFirst().orElse(null) != null);		
	}

	public static boolean canReceiveItem(Inventory inventory) {

		if (inventory.getSize() == 0)
			return true;

		for (ItemStack item : inventory.getContents()) {
			if (item == null)
				return true;
			if (item.getType().equals(Material.CACTUS)) {
				if (item.getAmount() < 64)
					return true;
			}
		}
		return false;
	}
	
	public static List<HopperObject> hopperPresence(int cX, int cZ) {
		return Reflections.ActiveHopper.stream().filter(hp -> isSimilar(hp, cX, cZ)).collect(Collectors.toList());
	}
	
	private static boolean isSimilar(HopperObject hp, int cX, int cZ) {
		return (hp.getChunkX() == cX && hp.getChunkZ() == cZ);
	}
	
}
