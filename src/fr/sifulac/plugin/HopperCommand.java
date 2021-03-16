package fr.sifulac.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sifulac.plugin.Object.HopperObject;
import fr.sifulac.plugin.Object.Maps;
import fr.sifulac.plugin.Object.Region;
import fr.sifulac.plugin.Utils.Reflections;

public class HopperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length > 0) {
			
			if (args[0].equalsIgnoreCase("list")) {
				
				for(Maps m : Reflections.getMaps()) {
					for(Region r : m.getRegions()) {
						for(HopperObject hp : r.getHoppersInRegions()) {
							Bukkit.broadcastMessage("§7Chunk location: X§b "+ hp.getChunkX() + " §7Z:§b " + hp.getChunkZ() + " §7Coordonnées:§b " + hp.getLocationX() + "," + hp.getLocationY() + "," + hp.getLocationZ());
						}
					}
				}				
				return true;				
			}
		}

		if (Boolean.FALSE.equals(sender instanceof Player))	return false;
		Player player = (Player) sender;
		player.getInventory().addItem(Reflections.getHopper());
		return true;
	}

}
