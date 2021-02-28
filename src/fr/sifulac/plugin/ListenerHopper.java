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
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListenerHopper implements Listener {

	private Main main = Main.getInstance();
	boolean state = true;
	private Manager manager = new Manager();

	@EventHandler
	public void onPlaceHopper(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();
		Block block = event.getBlock();

		if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		for (String s : item.getItemMeta().getLore()) {
			if (s.contains("§eHopperMod")) {
				HopperObject hopper = new HopperObject(String.valueOf(block.getX() + block.getY() + block.getZ()),
						block.getX(), block.getY(), block.getZ(), block.getChunk().getX(), block.getChunk().getZ(),
						block.getWorld().getName());
				main.getVars().getActiveHopper().add(hopper);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreakHopper(BlockBreakEvent event) {

		Block b = event.getBlock();

		if (b.getType().equals(Material.HOPPER)) {
			state = false;
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

				String id = String.valueOf(b.getX() + b.getY() + b.getZ());
				if (main.getVars().getActiveHopper().remove(main.getVars().getActiveHopper().stream()
						.filter(hp -> id.equals(hp.getId())).findFirst().orElse(null))) {
					Bukkit.broadcastMessage("HOPPER REMOVED");
				}
				state = true;

			});
		}
	}

	@EventHandler
	public void onLoadChunk(ChunkLoadEvent e) {

		Chunk c = e.getChunk();
		BlockState[] blocks = c.getTileEntities();

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

			for (BlockState b : blocks) {
				if (b.getType().equals(Material.HOPPER)) {

					String id = String.valueOf(b.getX() + b.getY() + b.getZ());

					if (main.getVars().getInactiveHopper().remove(main.getVars().getInactiveHopper().stream()
							.filter(hp -> id.equals(hp.getId())).findFirst().orElse(null))) {
						HopperObject hopper = new HopperObject(id, b.getX(), b.getY(), b.getZ(), b.getChunk().getX(),
								b.getChunk().getZ(), b.getWorld().getName());
						main.getVars().getActiveHopper().add(hopper);
						Bukkit.broadcastMessage("LOAD HOPPER");
					}
				}
			}

		});
	}

	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent e) {

		Chunk c = e.getChunk();
		BlockState[] blocks = c.getTileEntities();

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

			for (BlockState b : blocks) {

				if (b.getType().equals(Material.HOPPER)) {

					String id = b.getX() + b.getY() + b.getZ() + "";

					if (main.getVars().getActiveHopper().remove(main.getVars().getActiveHopper().stream()
							.filter(hp -> id.equals(hp.getId())).findFirst().orElse(null))) {

						HopperObject hopper = new HopperObject(id, b.getX(), b.getY(), b.getZ(), b.getChunk().getX(),
								b.getChunk().getZ(), b.getWorld().getName());
						main.getVars().getInactiveHopper().add(hopper);

						Bukkit.broadcastMessage("UNLOAD HOPPER");
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

		if (item.getType().equals(Material.CACTUS) && state) {

			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				
				for (HopperObject hopper : manager.hopperPresence(cX, cZ)) {

					Block b = world.getBlockAt(hopper.getLocationX(), hopper.getLocationY(), hopper.getLocationZ());

					if (!b.getState().getType().equals(Material.AIR)) {

						Hopper hopperObj = (Hopper) b.getState();
						Inventory hopperInv = hopperObj.getInventory();

						if (manager.canReceiveItem(hopperInv)) {
							hopperInv.addItem(item);
							event.getEntity().remove();
							break;
						}
					}
				}

			});
		}
	}
}