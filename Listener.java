package fr.sifulac.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {

	List<HopperObject> HopperActif = new ArrayList<HopperObject>();
	
	@EventHandler
	public void onPlaceHopper(BlockPlaceEvent event) {
		
		// VERIFIER ITEM
		// LE PLACE DANS LA LIST
		ItemStack item = event.getItemInHand();
		Block block = event.getBlock();
		
		if (item.hasItemMeta() && item.getItemMeta().hasLore())	return;
		
		for (String s : item.getItemMeta().getLore()) {
			if (s.contains("§eHopperMod")) {
				
				HopperObject hopperObject = new HopperObject();
				
				hopperObject.setChunkX(block.getChunk().getX());
				hopperObject.setChunkZ(block.getChunk().getZ());
				
				hopperObject.setLocationX(block.getX());
				hopperObject.setLocationY(block.getY());
				hopperObject.setLocationZ(block.getZ());
				
				hopperObject.setWorld(block.getWorld());
				
				HopperActif.add(hopperObject);
				break;
			}
		}
	}

	@EventHandler
	public void onBreakHopper(BlockBreakEvent event) {
		Block block = event.getBlock();		
		for(HopperObject obj : HopperActif) {
			if(obj.getWorld().equals(block.getWorld()) && block.getX() == obj.getLocationX() && block.getY() == obj.getLocationY() && block.getZ() == obj.getLocationZ()) {
				HopperActif.remove(obj);
				break;
			}
		}		
	}
	
	@EventHandler
	public void onLoadChunk(ChunkLoadEvent e) {
		Chunk c = e.getChunk();
		for(BlockState b : c.getTileEntities()) {
			if(b.getBlock().getType().equals(Material.HOPPER)) {
				
				//charge in list le hopper
				
			}
		}
	}
	
	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent e) {
		Chunk c = e.getChunk();
		for(BlockState b : c.getTileEntities()) {
			if(b.getBlock().getType().equals(Material.HOPPER)) {
				
				//decharge in list le hopper
				
			}
		}
	}	
}
