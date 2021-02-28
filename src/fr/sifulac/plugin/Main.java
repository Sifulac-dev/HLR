package fr.sifulac.plugin;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private StockageVars vars;
	
	private static Main plugin;
	
	public Main() {
		plugin = this;
	}
	
	public static Main getInstance() {
		return plugin;
	}
	
	@Override
	public void onEnable() {
	
		vars = new StockageVars();
		
		getServer().getPluginManager().registerEvents(new ListenerHopper(), this);			
		getCommand("hopper").setExecutor(new HopperCommand());
		getLogger().log(Level.INFO, "Activation du plugin");
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "Désactivation du plugin");
		super.onDisable();
	}

	public StockageVars getVars() {
		return vars;
	}
	
}
