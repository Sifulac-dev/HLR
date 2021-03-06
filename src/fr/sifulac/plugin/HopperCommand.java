package fr.sifulac.plugin;

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

		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			
			int i = 0;
			for (Maps m : Reflections.getMaps()) {
				for (Region r : m.getRegions()) {
					for (HopperObject hp : r.getHoppersInRegions()) {
						sender.sendMessage("�7Hoppper �cn: " + i + " �7Chunk X:�b " + hp.getChunkX() + " �7Z:�b " + hp.getChunkZ()
								+ " �7Coordonn�es:�b " + hp.getLocationX() + "," + hp.getLocationY() + ","
								+ hp.getLocationZ());
						i++;
					}
				}
			}
			sender.sendMessage("�7Nombre d'hopper:�b " + i);
			return true;		
		}
		
		if (Boolean.FALSE.equals(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		player.getInventory().addItem(Reflections.getHopper());
		return true;
	}	
}
