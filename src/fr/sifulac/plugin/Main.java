package fr.sifulac.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.sifulac.plugin.Utils.DataBase;

public class Main extends JavaPlugin {

	public String nmsver;
	public Boolean useOldMethods = false;
	
	public DataBase database;
	
	public Main() {
		instance = this;
	}
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
	
		
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
		if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_")) {// Not sure if 1_7 works for the																				// protocol hack?
			useOldMethods = true;
		}
		
		getServer().getPluginManager().registerEvents(new EventListener(), this);			
		getCommand("hopper").setExecutor(new HopperCommand());
		
		database = new DataBase("DataBase", getDataFolder());
		database.connect();	
		database.loadHoppers();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		database.disconnect();
		
		super.onDisable();
	}
	
}
