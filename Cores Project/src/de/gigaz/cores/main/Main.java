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
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.gigaz.cores.classes.EndingState;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.CrossCommand;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.listeners.BasicListeners;
import de.gigaz.cores.listeners.BreakBlockListener;
import de.gigaz.cores.listeners.BuildBlockListener;
import de.gigaz.cores.listeners.ConnectionListener;
import de.gigaz.cores.listeners.DropListener;
import de.gigaz.cores.listeners.EntityDamageListener;
import de.gigaz.cores.listeners.InventoryClickListener;
import de.gigaz.cores.listeners.MoveListener;
import de.gigaz.cores.listeners.PlayerInteractListener;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class Main extends JavaPlugin {

	//Todos
	//
	// - Save location of items in inventory
	
	
	
	
	private static Main plugin;
	public static final String CONFIG_ROOT = "cores.";
	public static final String PREFIX = "§8[§bCores§8] §r";
	public static final String PERMISSION_DENIED = PREFIX + "§7Dazu hast du §ckeine §7Rechte";
	public GameManager currentGameManager;
	public static final boolean autoteamrejoin = false;
	public static final boolean autoteam = true;
	public static final boolean autoteamcountoffline = true;
	public static final boolean autoteamsetoffline = false; //not recommended
	
	@Override
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
		pluginManager.registerEvents(new EntityDamageListener(), this);
		pluginManager.registerEvents(new BasicListeners(), this);
		pluginManager.registerEvents(new ScoreboardManager(), this);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			currentGameManager.getPlayerProfiles().add(new PlayerProfile(player));
			player.teleport(MainCommand.getConfigGeneralLocation("lobbyspawn"));
			Inventories.setLobbyInventory(currentGameManager.getPlayerProfile(player));
		}
	}
	
	
	
	public void loadWorlds() {
		//TODO load worlds from config
		File[] worldFiles = Bukkit.getWorldContainer().listFiles();
		for(int i = 0; i < worldFiles.length; i++) {
			if(worldFiles[i].isDirectory()) {
				String world = worldFiles[i].getName();
				if(Bukkit.getWorld(world) == null) {
					Bukkit.createWorld(new WorldCreator(world));
					//Bukkit.getLogger().info(DEBUGPREFIX + "Loaded world '" + world + "' sucessfully!");
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		if(!currentGameManager.getCurrentGameState().equals(GameState.LOBBY_STATE)) {
			EndingState.teleportPlayers();
			EndingState.showTitle(Team.UNSET);
			currentGameManager.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, currentGameManager.getLobbySpawn().getWorld(), 5);
			EndingState.replaceBlocks();
			EndingState.stop();
		}
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
	    	Bukkit.broadcastMessage("World copy error");
	    }
	}
	
	public boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	public void unloadWorld(World world) {
	    World _world = Bukkit.getWorld(""); //??????????????
	    if(!world.equals(null)) {
	        Bukkit.getServer().unloadWorld(world, true);
	    }
	}
	
	public World setMap(String name) {
		World worldtemplate = getWorld(name);
		File templateFolder = worldtemplate.getWorldFolder();
		World worldcopy = getWorld("currentWorld");
		File copyFolder = worldcopy.getWorldFolder();//new File(Bukkit.getWorldContainer(), "currentworld");
		unloadWorld(Bukkit.getWorld("currentworld"));
		//deleteWorld(copyFolder);
		copyWorld(templateFolder, copyFolder);
		worldcopy = getWorld("currentWorld");
		//currentGameManager.setMap(name);
		return worldcopy;
		//Bukkit.broadcastMessage(PREFIX + "Die Map wurde auf "+name+" gesetzt. copied from "+templateFolder.getName()+" "+worldtemplate.getName()+" "+copyFolder+" "+worldcopy.getName());
	}
		
	public String getFallDeathMessage(PlayerProfile ...player) {
		Random random = new Random();
		if(player.length < 1)
			return "";
		else if(player.length < 2) {
			switch(random.nextInt(2)) {
				case 0: return PREFIX+player[0].getName()+" ist der Schwerkraft zu Opfer gefallen.";
				case 1: return PREFIX+player[0].getName()+" ist auf dem Boden zerschellt.";
				default: return "";
			}
		} else {
			switch(random.nextInt(4)) {
				case 0: return PREFIX+player[0].getName()+" fiel im Kampf mit "+player[1].getName()+" zu tief.";
				case 1: return PREFIX+player[0].getName()+" wurde von "+player[1].getName()+" von einer Klippe geschmissen.";
				case 2: return PREFIX+player[0].getName()+" stieß sich im Kampf gegen "+player[1].getName()+" den Kopf.";
				case 3: return PREFIX+player[0].getName()+" zerschellte bei einer Reiberei mit  "+player[1].getName()+" am Boden.";
				default: return "";
			}
		}
	}
	
	public String getVoidDeathMessage(PlayerProfile player) {
		Random random = new Random();
		switch(random.nextInt(3)) {
			case 0: return PREFIX+player.getName()+" hat sich ins Nichts gestürtzt.";
			case 1: return PREFIX+player.getName()+" wurde von der Finsternis verschlungen.";
			case 2: return PREFIX+"§7Keiner hat "+player.getName()+" getötet.";
			default: return "";
		}
	}
	
	public String getVoidDeathMessage(PlayerProfile target, PlayerProfile attacker) {
		Random random = new Random();
		switch(random.nextInt(2)) {
			case 0: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" ins Nichts geschmissen.";
			case 1: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" aus dem Weg geräumt.";
			default: return "";
		}
	}
	
	public String getFireDeathMessage(PlayerProfile player) {
		Random random = new Random();
			switch(random.nextInt(2)) {
			case 0: return PREFIX+player.getName()+" ist in Feuer aufgegangen.";
			case 1: return PREFIX+player.getName()+" fiel den Flammen zu Opfer.";
			default: return "";
		}
	}
	
	public String getFireDeathMessage(PlayerProfile target, PlayerProfile attacker) {
		Random random = new Random();
		switch(random.nextInt(2)) {
			case 0: return PREFIX+target.getName()+" wurde im Kampf mit "+attacker.getName()+" verbrannt.";
			case 1: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" angezündet.";
			default: return "";
		}
	}
	
	public String getPVPDeathMessage(PlayerProfile target, PlayerProfile attacker) {
		Random random = new Random();
		switch(random.nextInt(3)) {
			case 0: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" geschnetzelt.";
			case 1: return PREFIX+target.getName()+" ist im Kampf mit "+attacker.getName()+" gefallen.";
			case 2: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" eliminiert.";
			default: return "";
		}
	}
	
	public String getBowDeathMessage(PlayerProfile target, PlayerProfile attacker) {
		Random random = new Random();
		switch(random.nextInt(3)) {
			case 0: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" erschossen.";
			case 1: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" durchlöchert.";
			case 2: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" mit Robin Hoods Lieblings-Waffe aufgespießt.";
			default: return "";
		}
	}
	
	/*public String getFallDeathMessage(PlayerProfile attacker, PlayerProfile target) {
		Random random = new Random();
		switch(random.nextInt(4)) {
			case 0: return PREFIX+target.getName()+" fiel im Kampf mit "+attacker.getName()+" zu tief.";
			case 1: return PREFIX+target.getName()+" wurde von "+attacker.getName()+" von einer Klippe geschmissen.";
			case 2: return PREFIX+target.getName()+" stieß sich im Kampf gegen "+attacker.getName()+" den Kopf.";
			case 3: return PREFIX+target.getName()+" zerschellte bei einer Reiberei mit  "+attacker.getName()+" âm Bode.";
			default: return "";
		}
	}*/
}
