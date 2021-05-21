package de.gigaz.cores.main;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main plugin;
	
	
	public void onEnable() {
		plugin = this;
		
	}
	
	
	
	public void loadWorlds() {
		//load worlds
		///TODO load worldds from config
		File[] worldFiles = Bukkit.getWorldContainer().listFiles();
		for(int i = 0; i < worldFiles.length; i++) {
			if(worldFiles[i].isDirectory()) {
				String world = worldFiles[i].getName();
				if(Bukkit.getWorld(world) == null) {
					/*Bukkit.createWorld(new WorldCreator(world));
					Bukkit.getLogger().info(DEBUGPREFIX + "Loaded world '" + world + "' sucessfully!");*/
				}
			}
		}
	}
	
	public void onDisable() {
		
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public World getWorld(final String name) {
		World world = Bukkit.getWorld(name);
		File file = new File(Bukkit.getWorldContainer(), name);
		if(file.exists() && world == null) {
			WorldCreator creator = new WorldCreator("world");
			creator.generator(new ChunkGenerator() {
			    @Override
			    public byte[] generate(World world, Random random, int x, int z) {
				        return new byte[32768]; //Empty byte array
				    }				});
			world = crreeaator.createWorld();
			world = Bukkit.getWorld(name);
		}
	    return world;
	}
}
