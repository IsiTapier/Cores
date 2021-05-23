package de.gigaz.cores.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.CrossCommand;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.listeners.BreakBlockListener;
import de.gigaz.cores.listeners.BuildBlockListener;
import de.gigaz.cores.listeners.ConnectionListener;
import de.gigaz.cores.listeners.DropListener;
import de.gigaz.cores.listeners.InventoryClickListener;
import de.gigaz.cores.listeners.MoveListener;
import de.gigaz.cores.listeners.PlayerInteractListener;
import net.minecraft.server.v1_16_R3.WorldGenerator;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static final String CONFIG_ROOT = "cores.";
	public static final String PREFIX = "§8[§bCores§8] §r";
	public GameManager currentGameManager;
	
	public void onEnable() {
		plugin = this;
		currentGameManager = new GameManager();
		loadWorlds();
		getCommand("cores").setExecutor(new MainCommand());
		getCommand("cross").setExecutor(new CrossCommand());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new BreakBlockListener(), this);
		pluginManager.registerEvents(new BuildBlockListener(), this);
		pluginManager.registerEvents(new ConnectionListener(), this);
		pluginManager.registerEvents(new DropListener(), this);
		pluginManager.registerEvents(new MoveListener(), this);
		pluginManager.registerEvents(new PlayerInteractListener(), this);
		pluginManager.registerEvents(new InventoryClickListener(), this);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			currentGameManager.getPlayerProfiles().put(player.getName(), new PlayerProfile(player));
		}
	}
	
	
	
	public void loadWorlds() {
		//TODO load worlds from config
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
	
	public GameManager getGameManager() {
		return currentGameManager;
	}
	
	
	public World getWorld(final String name) {;
		World world = Bukkit.getWorld(name);
		File file = new File(Bukkit.getWorldContainer(), name);
		if(!file.exists() || world == null) {
			/*WorldCreator creator = new WorldCreator(name);
			creator.generator(new ChunkGenerator() {
			    public byte[] generate(World world, Random random, int x, int z) {
				        return new byte[32768]; //Empty byte array
				    }				});*/
			world = Bukkit.getServer().createWorld(new WorldCreator(name));
			world = Bukkit.getWorld(name);
		}
	    return world;
	}
	
	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
	
	public void setMap(String name) {
		World worldtemplate = getWorld(name);
		File templateFolder = worldtemplate.getWorldFolder();
		World worldcopy = Bukkit.getWorld("currentWorld");
		File copyFolder = worldcopy.getWorldFolder();
		copyWorld(templateFolder, copyFolder);
		worldcopy = Bukkit.getWorld("currentWorld");
		currentGameManager.setMap(name);
		Bukkit.broadcastMessage(PREFIX + "Die Map wurde auf "+name+" gesetzt. copied from "+templateFolder.getName()+" "+worldtemplate.getName()+" "+copyFolder+" "+worldcopy.getName());
	}
}
