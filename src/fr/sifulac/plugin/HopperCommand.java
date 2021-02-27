package fr.sifulac.plugin;

import java.util.Arrays;

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
				for(HopperObject hp : Reflections.ActiveHopper) {
					Bukkit.broadcastMessage("hopper: " + i + " location X: " + hp.getChunkX() + " Z: " + hp.getChunkZ());
					i++;
				}
				return true;
			}
		}
		
		Player player = (Player)sender;
		
		ItemStack item = new ItemStack(Material.HOPPER, 1);
		ItemMeta infos = item.getItemMeta();
		infos.setLore(Arrays.asList("§eHopperMod"));
		item.setItemMeta(infos);
		
		player.getInventory().addItem(item);
		
		
		return false;
	}

}
