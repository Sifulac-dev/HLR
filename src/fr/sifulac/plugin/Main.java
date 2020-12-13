package fr.sifulac.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements org.bukkit.event.Listener{

	public Main() {
		INSTANCE = this;
	}
	private static Main INSTANCE;
	
	public static Main getInstance() {
		return INSTANCE;
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
