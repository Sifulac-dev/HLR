package fr.sifulac.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sifulac.plugin.Object.HopperObject;
import fr.sifulac.plugin.Object.Maps;
import fr.sifulac.plugin.Object.Region;

public class HopperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("hopper"))
			return false;

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("list")) {
				
				for(Maps m : Reflections.getMaps()) {
					for(Region r : m.getRegions()) {
						for(HopperObject hp : r.getHoppersInRegions()) {
							Bukkit.broadcastMessage("§7Chunk location: X§b "
									+ hp.getChunkX() + " §7Z:§b " + hp.getChunkZ() + " §7Coordonnées:§b "
									+ hp.getLocationX() + "," + hp.getLocationY() + "," + hp.getLocationZ());
						}
					}
				}				
				return true;				
			}
		}

		if (Boolean.FALSE.equals(sender instanceof Player))	return false;
		Player player = (Player) sender;

		ItemStack item = new ItemStack(Material.HOPPER, 1);
		ItemMeta infos = item.getItemMeta();
		infos.setDisplayName("§eHopperMod");
		item.setItemMeta(infos);

		player.getInventory().addItem(item);
		return true;
	}

}
