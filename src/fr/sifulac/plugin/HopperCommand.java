package fr.sifulac.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HopperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!cmd.getName().equalsIgnoreCase("hopper")) return false;
		if(sender instanceof Player == false) return false;
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("list")) {
				int i = 0;
				for(Region reg : Reflections.getRegions()) {
					for(HopperObject c : reg.getHoppersInRegions()) {
						Bukkit.broadcastMessage("§7Hopper:§b " + i + " §7Chunk location: X§b " + c.getChunkX() + " §7Z:§b " + c.getChunkZ() + " §7Coordonnées:§b " + c.getLocationX() + "," + c.getLocationY() + "," + c.getLocationZ());
						i++;
					}				
				}
				return true;
			}
		}
		
		Player player = (Player)sender;
		
		ItemStack item = new ItemStack(Material.HOPPER, 1);
		ItemMeta infos = item.getItemMeta();
		infos.setDisplayName("§eHopperMod");
		item.setItemMeta(infos);
		
		player.getInventory().addItem(item);
		
		
		return false;
	}

}
