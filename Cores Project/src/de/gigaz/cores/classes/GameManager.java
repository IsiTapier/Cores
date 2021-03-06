package de.gigaz.cores.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import de.gigaz.cores.classes.GameruleSetting.GameruleCategory;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.special.ActionBlock;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;
import de.gigaz.cores.special.SpecialItemManager;

public class GameManager {
	
	private final ArrayList<World> aquaMaps = new ArrayList<World>(Arrays.asList(Bukkit.getWorld("aquaMap")));
	
	private ArrayList<PlayerProfile> playerProfiles = new ArrayList<PlayerProfile>();;
	private LobbyState gameStates[] = new LobbyState[3];
	private GameState currentGameState = GameState.LOBBY_STATE;
	private LobbyState lobbyState;
	private IngameState ingameState;
	private EndingState endingState;
	private SpecialItemManager specialItemManager;
	
	private World map;
	private World lastMap;
	private World configureMap;
	private ArrayList<Core> cores = new ArrayList<Core>();
	private ArrayList<Core> stockedCores = new ArrayList<Core>();
	private ArrayList<ActionBlock> actionBlocks = new ArrayList<ActionBlock>();
	private ArrayList<Block> buildBlocks = new ArrayList<Block>();
	
	public GameManager() {
		//new LobbyState();
		
	}
	public void start() {
		FileConfiguration config = Main.getPlugin().getConfig();
		if(config.contains(Main.CONFIG_ROOT + "currentMap")) {
			String name = config.getString(Main.CONFIG_ROOT + "currentMap");
			//Main.getPlugin().saveConfig();
			if(name == null)
				return;
			lastMap = Main.getPlugin().getWorld(name);
		}
		registerActionBlocks();
	}

	public void setMap(World map) {
		this.map = map;
		lastMap = map;
		
		if(currentGameState.equals(GameState.INGAME_STATE))
			IngameState.stop(Team.UNSET);
		for(Player player : Main.getPlugin().getWorld("curretworld").getPlayers())
			player.teleport(Main.getPlugin().getGameManager().getLobbySpawn());
		Main.getPlugin().setMap(map.getName());

		Gamerules.setValue(Gamerules.aqua, aquaMaps.contains(map) ? true : false);
		
		/*FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + "currentMap", this.map.getName());
		//Main.getPlugin().saveConfig();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Main.getPlugin().saveConfig();
			}
		}, 1*20);*/
		
	}
	
	public void setMap(String name) {
		setMap(Main.getPlugin().getWorld(name));
	}
	
	public void setVirtualMap(World world) {
		map = world;
	}
	
	public World getMap() {
		return map;
	}
	
	public World getCopiedWorld() {
		return Main.getPlugin().getWorld(Main.COPIED_WORLD_NAME);
	}

	public World getLastMap() {
		return lastMap;
	}
	
	public World getConfigureMap() {
		return configureMap;
	}
	
	public void setConfigureMap(World world) {
		configureMap = world;
	}
	
	public void resetMap() {
		map = null;
	}
	
	public boolean checkWin() {
		if(getCores(Team.BLUE).size() == 0) {
			endGame(Team.RED);
			return true;
		}
		if(getCores(Team.RED).size() == 0) {
			endGame(Team.BLUE);
			return true;
		}
		return false;
	}
	
	public void endGame(Team team) {
		if(currentGameState == GameState.INGAME_STATE) {
			IngameState.stop(team);
		}
	}
	
	public void tieBreaker() {
		if(getCores(Team.BLUE).size() == getCores(Team.RED).size())
			endGame(Team.UNSET);
		else if(getCores(Team.BLUE).size() > getCores(Team.RED).size())
			endGame(Team.BLUE);
		else
			endGame(Team.RED);
	}
	
	public void registerCores() {
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + "worlds." + map.getName() + ".blue.core.";
		String name = null;
		
		cores.clear(); //new
		boolean valid = false;
		for(int x = 0; x <= 30; x++) {
			if(config.contains(root + x + ".location")) {
				Location location = config.getLocation(root + x + ".location");
				location.setWorld(Main.getPlugin().getWorld("currentworld"));
				//Bukkit.broadcastMessage("?bCore" + x);
				cores.add(new Core(location, Team.BLUE, x + ""));
				valid = true;
			}
		}
		root = Main.CONFIG_ROOT + "worlds." + map.getName() + ".red.core.";
		for(int x = 0; x <= 30; x++) {
			if(config.contains(root + x + ".location")) {
				Location location = config.getLocation(root + x + ".location");
				location.setWorld(Main.getPlugin().getWorld("currentworld"));
				//Bukkit.broadcastMessage("?cCore" + x);
				cores.add(new Core(location, Team.RED, x + ""));

				valid = true;
			}
		}
		//Bukkit.broadcastMessage(cores.size() + " size");
		if(valid == false)
			Bukkit.broadcastMessage(Main.PREFIX + "?7Das Spiel besitzt ?ckeine?7 vollst?ndig konfigurierte Map");
	}
	
	public void addCore(Core core) {
		stockedCores.add(core);
		cores.add(core);
		getCopiedWorld().getBlockAt(core.getLocation()).setType(Material.BEACON);
	}
	
	public Core getCore(Location location) {
		for(Core core : cores)
			if(core.getLocation().equals(location))
				return core;
		return null;
	}
	
	public Core getCore(Location location, int distance) {
		for(Core core : cores)
			if(core.getLocation().distance(location)<=distance)
				return core;
		return null;
	}
	
	public void stockCores() {
		registerCores();
		stockedCores.clear();
		for(Core core : getCores())
			stockedCores.add(core);
	}
	public ArrayList<Core> getStockedCores() {
		return stockedCores;
	}
	
	public ArrayList<Core> getStockedCores(Team team) {
		ArrayList<Core> list = new ArrayList<Core>();
		for(Core core : stockedCores)
			if(core.getTeam().equals(team))
				list.add(core);
		return list;
	}
	
	public Core getNearBrokenCore(Location location, int distance, Team team) {
		for(Core core : getStockedCores(team)) {
			if(getCores().contains(core))
				continue;
			if(core.getLocation().distance(location)<=distance)
				return core;
		}
		return null;
	}
	
	public ArrayList<Player> getPlayersOfTeam(Team team) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(PlayerProfile playerProfile : getPlayerProfiles()/*Bukkit.getOnlinePlayers()*/) {
			Player player = playerProfile.getPlayer();
			if(Main.getPlugin().getGameManager().getPlayerProfile(player).getTeam() == team)
				players.add(player);
		}		
		return players;
	}
	public List<Player> getPlayersOfTeamAsList(Team team) {
		List<Player> players = getPlayersOfTeam(team);
		return players;
	}
	
	public boolean checkMap(World world, boolean message) {
		boolean valid = true;
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + "worlds." + world.getName() + ".";
		if(!config.contains(root + "blue.spawn")) {
			//Bukkit.broadcastMessage("blue spawn");
			valid = false;
			}
		if(!config.contains(root + "red.spawn")) {
			//Bukkit.broadcastMessage("red spawn");
			valid = false;
			}
		if(!config.contains(root + "red.core")) {
			//Bukkit.broadcastMessage("red core");
			valid = false;
			}
		if(!config.contains(root + "blue.core")) {
			//Bukkit.broadcastMessage("blue core");
			valid = false;
			}
		if(!config.contains(root + "deathhight")) {
			//Bukkit.broadcastMessage("deathhight");
			valid = false;
			}
		if(valid == false) {
			if(message)
				Bukkit.broadcastMessage("?8[?fHinweis?8]?7 Die Map ?6" + world.getName() + "?7 ist noch ?cnicht?7 vollst?ndig konfiguriert");
		}
		return valid;
	}
	public boolean checkMap(World world) {
		return checkMap(world, false);
	}
	
	public ArrayList<Core> getCores() {
		return cores;
	}
	
	public ArrayList<Core> getCores(Team team) {
		ArrayList<Core> list = new ArrayList<Core>();
		for(Core core : cores) {
			if(core.getTeam().equals(team)) {
				list.add(core);
			}
		}
		return list;
	}

	public ArrayList<ActionBlock> getActionBlocks(World world) {
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + ".actionBlocks";
		ArrayList<ActionBlock> output = new ArrayList<ActionBlock>();
		for(ActionBlock actionBlock : actionBlocks) {
			if(actionBlock.getWorld().equals(world)) {
				output.add(actionBlock);
			}	
		}
		return output;
	}
	
	public ActionBlock getActionBlock(Location location) {
		for(ActionBlock actionBlock : getActionBlocks()) {
			if(actionBlock.getLocation().equals(location)) {
				return actionBlock;
			}
		}
		return null;
	}

	public ArrayList<ActionBlock> getActionBlocks() {
		return actionBlocks;
	}

	public void registerActionBlocks() {
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + ".actionBlocks";
		System.out.println(Main.getPlugin().getGameManager().getClass().toString());
		if(!config.contains(root)) {
			config.set(root, "");
			Main.getPlugin().saveConfig();
		} else {
			
			if(!config.contains(root + ".size")) {
				config.set(root + ".size", 0);
			} else {
				int size = config.getInt(root + ".size");
				getActionBlocks().clear();
				for(int x = 1; x <= size; x++) {
					getActionBlocks().add(ActionBlock.getFromConfig(root + "." + x));
				}
			}						
		}
		
	}

	public SpecialItemManager getSpecialItemManager() {
		return specialItemManager;
	}

	public void setSpecialItemManager(SpecialItemManager specialItemManager) {
		this.specialItemManager = specialItemManager;
	}

	public ArrayList<PlayerProfile> getPlayerProfiles() {
		return playerProfiles;
	}

	public void setPlayerProfiles(ArrayList<PlayerProfile> playerProfiles) {
		this.playerProfiles = playerProfiles;
	}

	public GameState getCurrentGameState() {
		return currentGameState;
	}
	
	public boolean checkBeaconView(Location location) {
		location = location.getBlock().getLocation();
		for(Core core : getCores())
			if(location.getBlock().getType().isSolid()&&location.getZ()==core.getLocation().getZ()&&location.getX()==core.getLocation().getX()&&location.getY()>core.getLocation().getY())
				return false;
		return true;
	}
	
	public boolean checkProtection(Block block) {
		return checkProtection(block.getLocation());
	}
	
	public boolean checkProtection(Location location) {
		return checkProtection(location, false);
	}
	
	public boolean checkProtection(Location location, boolean exceptCore, boolean exceptMap) {
		return checkSpawnProtection(location) || checkCoreProtection(location, exceptCore) || checkActionBlockProtection(location) || (checkMapProtection(location) && !exceptMap);
	}
	
	public boolean checkProtection(Location location, boolean exceptCore) {
		return checkSpawnProtection(location) || checkActionBlockProtection(location) || checkCoreProtection(location, exceptCore) || checkMapProtection(location) ;
	}
	
	public boolean checkMapProtection(Location location) {
		return Gamerules.getValue(Gamerules.mapProtection) && !buildBlocks.contains(location.getBlock()) && !location.getBlock().getType().equals(Material.IRON_BLOCK) && !location.getBlock().getType().equals(Material.DIAMOND_BLOCK);
	}
	
	public boolean checkSpawnProtection(Location location) {
		int radius = Gamerules.getValue(Gamerules.spawnProtection, true);
		return checkBlockProtection(location, getSpawnOfTeam(Team.BLUE, getMap()), radius) || checkBlockProtection(location, getSpawnOfTeam(Team.RED, getMap()), radius);
	}
	
	public boolean checkActionBlockProtection(Location location) {
		for(ActionBlock actionBlock : getActionBlocks()) {
			Location actionBlockLocation = actionBlock.getLocation();
			Location checkLocation = location.clone();
			Bukkit.broadcastMessage("World 1" + location.getWorld().getName() + " World 2"  + actionBlockLocation.getWorld().getName());
			checkLocation.add(0, -1, 0);
			if(checkLocation.equals(actionBlockLocation)) {
				return true;
			}		
		}
		return false;
	}

	public boolean checkCoreProtection(Location location, boolean exceptCore) {
		for(Core core : getCores())
			if(location.equals(core.getLocation()) && exceptCore)
				return false;
		for(Core core : getCores()) {
			if(checkBlockProtection(location, core.getLocation(), Math.max(Gamerules.getValue(Gamerules.coreProtection, true), 1), Gamerules.getValue(Gamerules.coreProtection, true)))
				return true;
			if(location.getBlock().getType().equals(Material.IRON_BLOCK)&&location.getY()+1==core.getLocation().getY()&&Math.abs(location.getX()-core.getLocation().getX())<=1&&Math.abs(location.getZ()-core.getLocation().getZ())<=1)
				return true;
			if((location.getBlock().getType().equals(Material.BLUE_STAINED_GLASS)&&core.getTeam().equals(Team.BLUE)||location.getBlock().getType().equals(Material.RED_STAINED_GLASS)&&core.getTeam().equals(Team.RED))&&location.getZ()==core.getLocation().getZ()&&location.getX()==core.getLocation().getX())
				return true;
		}
		
		return false;
		//TODO check iron and glass
	}
	
	public boolean checkBlockProtection(Location location, Location protect, int radius, int height) {
		if(radius <= 0 || height < 0) return false;
		if(Math.abs(protect.getY() - location.getY()) > height) return false;
		location = new Location(location.getWorld(), location.getX(), protect.getY(), location.getZ());
		if(protect.distance(location) < radius)  return true;			
		return false;
	}
	
	public boolean checkBlockProtection(Location location, Location protect, int radius) {
		return checkBlockProtection(location, protect, radius, radius);
	}

	public void setGameState(GameState gameState) {
		this.currentGameState = gameState;
	}
	
	public Location getCorrectedLocation(Location location) {
		Location output = location.clone();
		if(getMap() != null) {
			output.setWorld(getCopiedWorld());
		}
		return output;
		
	}
	
	public PlayerProfile getPlayerProfile(Player player) {
		//return playerProfiles.get(player.getName());
		for(PlayerProfile playerProfile : playerProfiles) {
			if(playerProfile.getPlayer().getName().equals(player.getName()))
				return playerProfile;
		}
		return null;
	}
	
	public PlayerProfile addPlayer(Player player) {
		PlayerProfile playerProfile = new PlayerProfile(player);
		playerProfiles.add(playerProfile);
		return playerProfile;
	}
	
	public void removePlayer(Player player) {
		playerProfiles.remove(playerProfiles.indexOf(getPlayerProfile(player)));
	}
 
	public LobbyState[] getGameStates() {
		return gameStates;
	}

	public void setGameStates(LobbyState[] gameStates) {
		this.gameStates = gameStates;
	}

	public LobbyState getLobbyState() {
		return lobbyState;
	}

	public void setLobbyState(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	public IngameState getIngameState() {
		return ingameState;
	}

	public void setIngameState(IngameState ingameState) {
		this.ingameState = ingameState;
	}

	public EndingState getEndingState() {
		return endingState;
	}

	public void setEndingState(EndingState endingState) {
		this.endingState = endingState;
	}

	public void setCurrentGameState(GameState currentGameState) {
		this.currentGameState = currentGameState;
	}
	
	public Location getLobbySpawn() {
		Location location = MainCommand.getConfigGeneralLocation("lobbyspawn");
		return location;
	}
	
	public Location getSpawnOfTeam(Team team, World world) {
		return MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", world);
	}
	
	public void  playSound(Sound sound, World world, int tone) {
		playSound(sound, world, tone, false);
	}
	public void playSound(Sound sound, World world, int tone, boolean isMusic) {
		if(isMusic && !Gamerules.getValue(Gamerules.winMusic))
			return;
		if(!isMusic && !Gamerules.getValue(Gamerules.soundEffects))
			return;
		for(Player player : world.getPlayers()) {
			player.playSound(player.getLocation(), sound, tone, 1);		
		}
	}
	public void playSound(Sound sound, World world, int tone, Team team) {
		if(!Gamerules.getValue(Gamerules.soundEffects))
			return;
		for(Player player : world.getPlayers()) {
			Team playerTeam = Main.getPlugin().getGameManager().getPlayerProfile(player).getTeam();
			if(playerTeam.equals(team)) {
				player.playSound(player.getLocation(), sound, tone, 1);
			}				
		}
	}
	public void playSound(Sound sound, World world) {
		playSound(sound, world, 5);
	}
	
	public void autoTeam(PlayerProfile playerProfile) {
		int red = 0;
		int blue = 0;
		for(PlayerProfile player : getPlayerProfiles()) {
			if(player.getPlayer().isOnline() || Main.autoteamcountoffline) {
				if(player.getTeam().equals(Team.RED))
					red++;
				else if(player.getTeam().equals(Team.BLUE))
					blue++;
			}
			
		}
		if(red < blue)
			playerProfile.setTeam(Team.RED);
		else
			playerProfile.setTeam(Team.BLUE);
	}
	
	public void setTeams() {
		ArrayList<PlayerProfile> offlinePlayers = new ArrayList<PlayerProfile>();
		for(PlayerProfile playerProfile : getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			if(!player.isOnline()) {
				offlinePlayers.add(playerProfile);
				continue;
			}
			if(playerProfile.getTeam().equals(Team.UNSET))
				autoTeam(playerProfile);
		}
		if(Main.autoteamsetoffline) {
			for(PlayerProfile playerProfile : offlinePlayers) {
				if(playerProfile.getTeam().equals(Team.UNSET))
					autoTeam(playerProfile);
			}
		}
	}
	
	public int getFloorHight() {
		int floorHight = 0;
		ArrayList<Integer> hights = new ArrayList<Integer>(Arrays.asList(
						(int)getSpawnOfTeam(Team.BLUE, getMap()).getY(),
						(int)getSpawnOfTeam(Team.RED, getMap()).getY()));
		for(Core core : getCores())
			hights.add((int) core.getLocation().getY());
		
		for(int hight : hights)
			if(hight > floorHight)
				floorHight = hight;
		return floorHight;
	}
	
	public void addBuildBlock(Block block) {
		buildBlocks.add(block);
	}
	
	public void removeBuildBlock(Block block) {
		buildBlocks.remove(block);
	}
	
	public void resetBuildBlocks() {
		buildBlocks.clear();
	}
	
}


  
