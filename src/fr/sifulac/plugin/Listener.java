package fr.sifulac.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {

	boolean STATE = true;
	
	@EventHandler
	public void onPlaceHopper(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();
		Block block = event.getBlock();

		if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		for (String s : item.getItemMeta().getLore()) {
			if (s.contains("§eHopperMod")) {

				HopperObject hopper = new HopperObject(String.valueOf(block.getX() + block.getY() + block.getZ()), block.getX(), block.getY(), block.getZ(), block.getChunk().getX(), block.getChunk().getZ(), block.getWorld().getName());
				Reflections.ActiveHopper.add(hopper);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreakHopper(BlockBreakEvent event) {

		Block b = event.getBlock();
		STATE = false;
		if (b.getType().equals(Material.HOPPER)) {

			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					String id = String.valueOf(b.getX() + b.getY() + b.getZ());
					
					Reflections.ActiveHopper.remove(Reflections.ActiveHopper.stream()
							.filter(hp -> id.equals(hp.getId())).findFirst().orElse(null));
					
					Bukkit.broadcastMessage("HOPPER REMOVED");
					
					STATE = true;
					
					return;

				}
			});
		}
	}

	@EventHandler
	public void onLoadChunk(ChunkLoadEvent e) {

		Chunk c = e.getChunk();
		BlockState[] blocks = c.getTileEntities();

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

			@Override
			public void run() {

				for (BlockState b : blocks) {
					if (b.getType().equals(Material.HOPPER)) {

						String id = String.valueOf(b.getX() + b.getY() + b.getZ());
						
						if (Reflections.InactiveHopper.remove(Reflections.InactiveHopper.stream().filter(hp -> id.equals(hp.getId())).findFirst().orElse(null))) {
							HopperObject hopper = new HopperObject(id, b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ(), b.getWorld().getName());							
							Reflections.ActiveHopper.add(hopper);
							Bukkit.broadcastMessage("LOAD HOPPER");
						}
					}
				}
			}
		});
	}

	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent e) {

		Chunk c = e.getChunk();
		BlockState[] blocks = c.getTileEntities();

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

			@Override
			public void run() {

				for (BlockState b : blocks) {

					if (b.getType().equals(Material.HOPPER)) {

						String id = b.getX() + b.getY() + b.getZ() + "";

						if (Reflections.ActiveHopper.remove(Reflections.ActiveHopper.stream()
								.filter(hp -> id.equals(hp.getId())).findFirst().orElse(null))) {
							
							HopperObject hopper = new HopperObject(id, b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ(), b.getWorld().getName());
							Reflections.InactiveHopper.add(hopper);

							Bukkit.broadcastMessage("UNLOAD HOPPER");
						}
					}
				}
			}
		});

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemDrop(ItemSpawnEvent event) {

		ItemStack item = event.getEntity().getItemStack();
		int cX = event.getLocation().getChunk().getX();
		int cZ = event.getLocation().getChunk().getZ();
		World world = event.getLocation().getWorld();

		if (item.getType().equals(Material.CACTUS)) {

			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {

					if (STATE == false)
						return;

					for (HopperObject hopper : Manager.hopperPresence(cX, cZ)) {

						Block b = world.getBlockAt(hopper.getLocationX(), hopper.getLocationY(), hopper.getLocationZ());

						if (!b.getState().getType().equals(Material.AIR)) {

							Hopper hopperObj = (Hopper) b.getState();
							Inventory hopperInv = hopperObj.getInventory();

							if (Manager.canReceiveItem(hopperInv)) {
								hopperInv.addItem(item);
								event.getEntity().remove();
								break;
							}
						}
					}
				}
			});
		}
	}
}