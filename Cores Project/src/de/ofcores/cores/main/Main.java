package de.ofcores.cores.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public Main plugin;
	
	
	public void onEnable() {
		plugin = this;
	}
	
	public void onDisable() {
		
	}
	
	public Main getPlugin() {
		return plugin;
	}
}
