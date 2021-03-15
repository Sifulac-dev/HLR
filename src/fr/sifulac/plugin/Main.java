package fr.sifulac.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

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
	
		getServer().getPluginManager().registerEvents(new Listener(), this);			
		getCommand("hopper").setExecutor(new HopperCommand());
		
		database = new DataBase("DataBase", getDataFolder());
		database.connect();	
		database.loadHoppers();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}
	
}
