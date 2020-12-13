package fr.sifulac.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
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

	@EventHandler
	public void onPlaceHopper(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();
		Block block = event.getBlock();

		if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		for (String s : item.getItemMeta().getLore()) {
			if (s.contains("§eHopperMod")) {

				HopperObject hopperObject = new HopperObject();

				hopperObject.setChunkX(block.getChunk().getX());
				hopperObject.setChunkZ(block.getChunk().getZ());

				hopperObject.setLocationX(block.getX());
				hopperObject.setLocationY(block.getY());
				hopperObject.setLocationZ(block.getZ());

				hopperObject.setWorld(block.getWorld().getName());

				Reflections.ActiveHopper.add(hopperObject);
				break;
			}
		}
	}

	@EventHandler
	public void onBreakHopper(BlockBreakEvent event) {

		Block b = event.getBlock();
		String world = b.getWorld().getName();
		if (b.getType().equals(Material.HOPPER)) {

			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {

					HopperObject hopper = getHopper(b.getChunk().getX(), b.getChunk().getZ(), b.getX(), b.getY(),
							b.getZ(), world);

					if (containHopper(hopper, Reflections.ActiveHopper)) {
						removeHopper(hopper, Reflections.ActiveHopper);
						Bukkit.broadcastMessage("HOPPER REMOVED");
						return;
					}
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

						HopperObject hopper = getHopper(b.getChunk().getX(), b.getChunk().getZ(), b.getX(), b.getY(),
								b.getZ(), b.getWorld().getName());

						if (containHopper(hopper, Reflections.InactiveHopper)) {
							removeHopper(hopper, Reflections.InactiveHopper);
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

						HopperObject hopper = getHopper(b.getChunk().getX(), b.getChunk().getZ(), b.getX(), b.getY(),
								b.getZ(), b.getWorld().getName());

						if (containHopper(hopper, Reflections.ActiveHopper)) {
							removeHopper(hopper, Reflections.ActiveHopper);
							Reflections.InactiveHopper.add(hopper);
							Bukkit.broadcastMessage("UNLOAD HOPPER");
						}
					}
				}
			}
		});

	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) {

		ItemStack item = event.getEntity().getItemStack();
		Location location = event.getLocation();
		World world = location.getWorld();

		if (item.getType().equals(Material.CACTUS)) {

			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {

					for (HopperObject hopper : hopperPresence(location)) {

						Block b = world.getBlockAt(hopper.getLocationX(), hopper.getLocationY(), hopper.getLocationZ());
						Hopper hopperObj = (Hopper) b.getState();
						Inventory hopperInv = hopperObj.getInventory();

						if (canReceiveItem(hopperInv)) {
							hopperInv.addItem(item);
							event.getEntity().remove();
							break;
						}
					}
				}
			});
		}
	}

	private HopperObject getHopper(int chunkX, int chunkZ, int x, int y, int z, String world) {

		HopperObject hopper = new HopperObject();

		hopper.setChunkX(chunkX);
		hopper.setChunkZ(chunkZ);
		hopper.setLocationX(x);
		hopper.setLocationY(y);
		hopper.setLocationZ(z);
		hopper.setWorld(world);

		return hopper;
	}

	private boolean canReceiveItem(Inventory inventory) {

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

	private List<HopperObject> hopperPresence(Location location) {

		List<HopperObject> hopperPresence = new ArrayList<HopperObject>();
		for (HopperObject activeHopper : Reflections.ActiveHopper) {

			if (activeHopper.getWorld().equals(location.getWorld().getName())) {

				if (activeHopper.getChunkX() == location.getChunk().getX()
						&& activeHopper.getChunkZ() == location.getChunk().getZ()) {
					hopperPresence.add(activeHopper);
				}
			}
		}
		return hopperPresence;
	}

	private void removeHopper(HopperObject hopperTarget, List<HopperObject> listHopper) {
		for (HopperObject obj : listHopper) {
			if (obj.getLocationX() == hopperTarget.getLocationX() && obj.getLocationY() == hopperTarget.getLocationY()
					&& obj.getLocationZ() == hopperTarget.getLocationZ()
					&& obj.getWorld().equals(hopperTarget.getWorld())) {
				listHopper.remove(obj);
				break;
			}
		}
	}

	private boolean containHopper(HopperObject hopperTarget, List<HopperObject> listHopper) {
		for (HopperObject obj : listHopper) {
			if (obj.getLocationX() == hopperTarget.getLocationX() && obj.getLocationY() == hopperTarget.getLocationY()
					&& obj.getLocationZ() == hopperTarget.getLocationZ()
					&& obj.getWorld().equals(hopperTarget.getWorld())) {
				return true;
			}
		}
		return false;
	}
}
