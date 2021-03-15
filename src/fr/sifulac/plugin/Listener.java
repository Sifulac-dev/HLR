package fr.sifulac.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {

	boolean state = true;

	@EventHandler
	public void onPlaceHopper(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();
		Block b = event.getBlock();

		if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
			return;

		if (!item.getItemMeta().getDisplayName().equals("§eHopperMod"))
			return;

		int cx = b.getChunk().getX();
		int cz = b.getChunk().getZ();
		
		int regX;
		int regZ;

		regX = b.getChunk().getX() / 32;
		regZ = b.getChunk().getZ() / 32;

		Region region = Reflections.getRegions().stream().filter(r -> r.getId().equals(String.valueOf(regX + regZ))).findFirst().orElse(null);
		
		if (region != null) {
			
			HopperObject hopper = region.getHoppersInRegions().stream().filter(hp -> hp.getChunkX() == cx && hp.getChunkZ() == cz).findFirst().orElse(null);
			
			if(hopper != null) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cUn hopper est déjà dans le chunk !");
			} else {
				region.addHopper(new HopperObject(b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ(), b.getWorld().getName()));
				event.getPlayer().sendMessage("§eHopper mis en place !");
			}			

		} else {
			
			Region r = new Region(String.valueOf(regX + regZ));
			if (Boolean.TRUE.equals(r.addHopper(new HopperObject(b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ(), b.getWorld().getName())))) {
				Reflections.getRegions().add(r);
				event.getPlayer().sendMessage("§eHopper mis en place !");
			}
			
		}

	}

	@EventHandler
	public void onBreakHopper(BlockBreakEvent event) {

		Block b = event.getBlock();
		state = false;
		
		if (b.getType().equals(Material.HOPPER)) {

			int cx = b.getChunk().getX();
			int cz = b.getChunk().getZ();

			int regX = cx / 32;
			int regZ = cz / 32;

			Region reg = Reflections.getRegions().stream().filter(region -> region.getId().equals(String.valueOf(regX + regZ))).findFirst().orElse(null);

			if (reg != null) {

				HopperObject h = reg.getHoppersInRegions().stream().filter(hp -> hp.getChunkX() == cx && hp.getChunkZ() == cz).findFirst().orElse(null);
				
				if (h != null && b.getLocation().getBlockX() == h.getLocationX() && b.getLocation().getBlockY() == h.getLocationY() && b.getLocation().getBlockZ() == h.getLocationZ()) {
					reg.getHoppersInRegions().remove(h);
					event.getPlayer().sendMessage("§eHopper enlevé !");
				}
			}
		}
		state = true;
	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) {

		ItemStack item = event.getEntity().getItemStack();
		int cx = event.getLocation().getChunk().getX();
		int cz = event.getLocation().getChunk().getZ();

		int regX = cx / 32;
		int regZ = cz / 32;

		if (item.getType().equals(Material.CACTUS)) {

			Long time = System.currentTimeMillis();			
			
			Region reg = Reflections.getRegions().stream().filter(region -> region.getId().equals(String.valueOf(regX + regZ))).findFirst().orElse(null);
			
			if (reg != null) {
				
				HopperObject hopper = reg.getHoppersInRegions().stream().filter(hp -> hp.getChunkX() == cx && hp.getChunkZ() == cz).findFirst().orElse(null);

				if (hopper != null) {
					
					Block b = event.getLocation().getWorld().getBlockAt(hopper.getLocationX(), hopper.getLocationY(), hopper.getLocationZ());
					
					if (b.getState().getType().equals(Material.HOPPER)) {

						Hopper hopperObj = (Hopper) b.getState();
						Inventory hopperInv = hopperObj.getInventory();

						if (canReceiveItem(hopperInv)) {
							hopperInv.addItem(item);
							event.getEntity().remove();
							System.out.println("Absorbé en: " + (System.currentTimeMillis()-time) + " ms");
							return;
						}
					}					
				}
			}
		}
	}
	
	public Boolean canReceiveItem(Inventory inventory) {

		if (inventory.getSize() == 0)
			return true;

		for (ItemStack item : inventory.getContents()) {
			if (item == null) return true;
			if (item.getType().equals(Material.CACTUS)) {
				if (item.getAmount() < 64)
					return true;
			}
		}
		return false;
	}	
}
