package fr.sifulac.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener{

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
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}
	
}
