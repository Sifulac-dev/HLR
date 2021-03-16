package fr.sifulac.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.sifulac.plugin.ActionBar.ActionBar;
import fr.sifulac.plugin.Object.HopperObject;
import fr.sifulac.plugin.Object.Maps;
import fr.sifulac.plugin.Object.Region;
import fr.sifulac.plugin.Utils.Reflections;

public class EventListener implements Listener {

	boolean state = true;
	private Main main = Main.getInstance();

	@EventHandler
	public void onSetHopper(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();
		Block b = event.getBlock();

		if (Boolean.FALSE.equals(isHopperMod(item)))
			return;

		int cx = b.getChunk().getX();
		int cz = b.getChunk().getZ();

		int regX;
		int regZ;

		regX = b.getChunk().getX() / 32;
		regZ = b.getChunk().getZ() / 32;

		Maps maps = Reflections.getMaps().stream().filter(map -> map.getName().equals(b.getWorld().getName()))
				.findFirst().orElse(null);

		HopperObject hop = new HopperObject(b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ(),
				b.getWorld().getName(), 0);

		if (maps != null) { // ALREADY EXIST

			Region region = maps.getRegions().stream().filter(r -> r.getId().equals(String.valueOf(regX + regZ)))
					.findFirst().orElse(null);

			if (region != null) { // ALREADY EXIST

				HopperObject hopper = region.getHoppersInRegions().stream()
						.filter(hp -> hp.getChunkX() == cx && hp.getChunkZ() == cz).findFirst().orElse(null);

				if (hopper != null) { // ALREADY EXIST

					event.setCancelled(true);
					event.getPlayer().sendMessage("§cUn hopper est déjà dans le chunk !");

				} else { // NO EXIST

					region.addHopper(hop);
					main.database.addHopper(hop, regX, regZ, b.getWorld().getName());
					ActionBar.sendActionBar(event.getPlayer(), Reflections.msgSetHopper);	

				}

			} else { // NO EXIST

				Region r = new Region(String.valueOf(regX + regZ));
				if (Boolean.TRUE.equals(r.addHopper(hop))) {
					maps.getRegions().add(r);
					ActionBar.sendActionBar(event.getPlayer(), Reflections.msgSetHopper);	
				}

			}

		} else {

			Maps m = new Maps(b.getWorld().getName());
			Region r = new Region(String.valueOf(regX + regZ));
			r.addHopper(hop);
			m.getRegions().add(r);
			Reflections.getMaps().add(m);
			ActionBar.sendActionBar(event.getPlayer(), Reflections.msgSetHopper);		
		}
	}

	@EventHandler
	public void onBreakHopper(BlockBreakEvent event) {

		Block b = event.getBlock();
		state = false;
		
		if (b.getType().equals(Material.HOPPER)) {
			
			event.setCancelled(true);
			
			int cx = b.getChunk().getX();
			int cz = b.getChunk().getZ();

			Object[] hp = hasHopperInChunk(b.getLocation().getWorld().getName(), cx, cz);

			if (hp != null && b.getLocation().getBlockX() == ((HopperObject) hp[0]).getLocationX()
					&& b.getLocation().getBlockY() == ((HopperObject) hp[0]).getLocationY()
					&& b.getLocation().getBlockZ() == ((HopperObject) hp[0]).getLocationZ()) {
				
				if(hasAvaliableSlot(event.getPlayer())) { //RECUP HOPPER
					
					((Region) hp[1]).getHoppersInRegions().remove(((HopperObject) hp[0]));
					main.database.removeHopper(((HopperObject) hp[0]));
					ActionBar.sendActionBar(event.getPlayer(), "§eVous avez récupéré le hopper");	
					b.setType(Material.AIR);
					event.getPlayer().getInventory().addItem(Reflections.getHopper());
					
				} else { //NE RECUP PAS					
					
					ActionBar.sendActionBar(event.getPlayer(), "§cVous n'avez pas assez de place dans l'inventaire.");	
					
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

		if (!item.getType().equals(Material.CACTUS))
			return;

		event.setCancelled(true);

		Object[] hp = hasHopperInChunk(event.getLocation().getWorld().getName(), cx, cz);

		if (hp != null) {

			Block b = event.getLocation().getWorld().getBlockAt(((HopperObject) hp[0]).getLocationX(),
					((HopperObject) hp[0]).getLocationY(), ((HopperObject) hp[0]).getLocationZ());

			if (b.getState().getType().equals(Material.HOPPER)) {
				((HopperObject) hp[0]).addCactus(event.getEntity().getItemStack().getAmount());
				return;
			}

		}

		event.setCancelled(false);
	}

	@EventHandler
	public void onClickHopper(PlayerInteractEvent e) {

		int cx = e.getPlayer().getLocation().getChunk().getX();
		int cz = e.getPlayer().getLocation().getChunk().getZ();

		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.HOPPER)) {

			e.setCancelled(true);
			
			Object[] hp = hasHopperInChunk(e.getPlayer().getLocation().getWorld().getName(), cx, cz);

			if (hp != null) {

				Block b = e.getClickedBlock();

				if (b.getX() == ((HopperObject) hp[0]).getLocationX()
						&& b.getY() == ((HopperObject) hp[0]).getLocationY()
						&& b.getZ() == ((HopperObject) hp[0]).getLocationZ()) {				
					
					if(e.getPlayer().isSneaking()) {
						deleteCactusInHopper(e.getPlayer(), (HopperObject) hp[0]);
						return;
					}
					
					ActionBar.sendActionBar(e.getPlayer(), "§bIl y a§c " + ((HopperObject) hp[0]).getNumberCactus() + " §bcactus dans le hopper");					
					return;
				}
			}
			e.setCancelled(false);			
		}
	}

	private boolean hasAvaliableSlot(Player player){
	    Inventory inv = player.getInventory();
	    for (ItemStack item: inv.getContents()) {
	         if(item == null) {
	                 return true;
	         }
	     }
	return false;
	}
	
	private void deleteCactusInHopper(Player p, HopperObject hopper) {
			
			int numberSpaceFree = numberItemCanReceive(p.getInventory())*64;
			if(numberSpaceFree == 0) {
				ActionBar.sendActionBar(p, "§cVous êtes full !");
				return;
			}
			int numberCactus = hopper.getNumberCactus();
			int cactusConsume = numberSpaceFree - numberCactus;
			if(cactusConsume > 0) {
				
				hopper.removeCactus(numberCactus);
				p.getInventory().addItem(new ItemStack(Material.CACTUS, numberCactus));
				ActionBar.sendActionBar(p, "§eVous avez récupéré§b " + numberCactus + " §ecactus");		
			} else {
				
				hopper.removeCactus(numberSpaceFree);
				p.getInventory().addItem(new ItemStack(Material.CACTUS, numberSpaceFree));
				ActionBar.sendActionBar(p, "§eVous avez récupéré§b " + numberSpaceFree + " §ecactus");		
			}						
	}
	
	private Object[] hasHopperInChunk(String worldName, int cx, int cz) {

		int regX = cx / 32;
		int regZ = cz / 32;

		Maps maps = Reflections.getMaps().stream().filter(map -> map.getName().equals(worldName)).findFirst()
				.orElse(null);

		if (maps != null) {

			Region reg = maps.getRegions().stream().filter(region -> region.getId().equals(String.valueOf(regX + regZ)))
					.findFirst().orElse(null);

			if (reg != null) {

				HopperObject hopper = reg.getHoppersInRegions().stream()
						.filter(hp -> hp.getChunkX() == cx && hp.getChunkZ() == cz).findFirst().orElse(null);

				if (hopper != null) {

					return new Object[] { hopper, reg, maps };

				}
			}
		}
		return null;
	}

	private int numberItemCanReceive(PlayerInventory inv) {
		int e = 0;
		for(ItemStack i : inv.getContents())
			if(i == null) e++; 
		return e;
	}
	
	private Boolean isHopperMod(ItemStack item) {
		return (item.hasItemMeta() && item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().equals("§eHopperMod"));
	}
}
