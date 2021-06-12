package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import de.gigaz.cores.util.Team;
import de.gigaz.cores.util.inventory.InventoryClass;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.inventories.IngameInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.special.ActionBlock;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;

public class PlayerProfile {
	
	private static final boolean changeEditGameMode = true;
	
	private Player player;
	private boolean editMode = false;
	private Team team = Team.UNSET;
	private int kills = 0;
	private int deaths = 0;
	private Scoreboard currentScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private Player lastAttacker;
	private GameManager gameManager = Main.getPlugin().getGameManager();
	private InventoryClass normalInventory = IngameInventory.getInventory(team);
	private Integer coreRepairing;
	private int gamerulePage;
	private ActionBlock editActionBlock;
	

	public PlayerProfile(Player player) {
		this.player = player;
		//normalInventory 
		player.setPlayerListName(team.getColorCode() + player.getName());
	}
	
	public int getGamerulePage() {
		return gamerulePage;
	}

	public void setGamerulePage(int gamerulePage) {
		this.gamerulePage = gamerulePage;
	}

	public void startCoreRepairing(Core core) {
		String coreName = team.getColorCode()+"Core "+core.getDisplayName()+"§7";
		Bukkit.broadcastMessage(Main.PREFIX+team.getColorCode()+player.getName()+"§7 repariert den "+coreName+"!");
		coreRepairing = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			int seconds = 20;
			@Override
			public void run() {
				switch(seconds) {
				case 10: case 5:
					Bukkit.broadcastMessage(Main.PREFIX+"§7Der "+coreName+" ist in "+seconds+" Sekunden repariert!");
					break;
				case 20: case 3: case 2: case 1:
					for(PlayerProfile playerProfile : gameManager.getPlayerProfiles())
						if(playerProfile.getTeam()==team)
							playerProfile.getPlayer().sendMessage(Main.PREFIX+"§7Der "+coreName+" ist in "+seconds+" Sekunden repariert!");
					break;
				case 0:
					if (coreRepairing != null)
						Bukkit.getScheduler().cancelTask(coreRepairing);
					coreRepairing = null;
					Bukkit.broadcastMessage(Main.PREFIX+"§7Der "+coreName+" wurde repariert!");
					player.getInventory().getItem(player.getInventory().first(Material.END_CRYSTAL)).setAmount(player.getInventory().getItem(player.getInventory().first(Material.END_CRYSTAL)).getAmount()-1);
					gameManager.addCore(core);
					ScoreboardManager.drawAll();
					break;
				default:
					player.sendMessage(Main.PREFIX+"§7Der "+coreName+" ist in "+seconds+" Sekunden repariert!");
					break;
				}
				player.setLevel(seconds);
				player.setExp(1-((float) seconds/20));
				seconds--;
			}
		}, 0L, 20L);
	}
	
	public void stopCoreRepairing() {
		if(coreRepairing != null) {
			Bukkit.getScheduler().cancelTask(coreRepairing);
			coreRepairing = null;
			player.setLevel(1);
			player.setExp(0);
			player.sendMessage(Main.PREFIX+"§7Du hast das Core Reparieren abgebrochen");
			for(PlayerProfile playerProfile : gameManager.getPlayerProfiles())
				if(/*playerProfile.getTeam()==team && */playerProfile != this)
					playerProfile.getPlayer().sendMessage(Main.PREFIX+"§7Das Core Reparieren wurde abgebrochen");
		}
	}
	
	public int getCoreRepairing() {
		return coreRepairing;
	}

	public void setCoreRepairing(int coreRepairing) {
		this.coreRepairing = coreRepairing;
	}

	public Player getPlayer() {
		return player;
	}
	
	public Team getTeam() {
		return team;
	}

	public boolean isEditMode() {
		return editMode;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setEditMode(boolean editMode) {
		if(this.editMode == editMode)
			return;
		this.editMode = editMode;
		if(editMode && changeEditGameMode)
			player.setGameMode(GameMode.CREATIVE);
		else
			player.setGameMode(GameMode.SURVIVAL);
		if(!this.editMode)
			Inventories.setLobbyInventory(this);
	}

	public void setTeam(Team team) {
		this.team = team;
		this.player.setPlayerListName(team.getColorCode() + player.getName());
		if(gameManager.getCurrentGameState().equals(GameState.INGAME_STATE)) {
			ScoreboardManager.draw(getPlayer());
			IngameState.teleportPlayer(this);
			IngameState.giveItems(this);
			IngameState.deactivateEditMode(this);
			IngameState.setGameMode(this);
		} else {
			for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
				if(!playerProfile.isEditMode())
					Inventories.setLobbyInventory(playerProfile);
			}
		}
		normalInventory = IngameInventory.getInventory(team);
	}
	
	public void respawn() {
		respawn(false);
	}

	public void respawn(boolean isStarting) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		while(player.getInventory().contains(Material.END_CRYSTAL)) {
			int slot = player.getInventory().first(Material.END_CRYSTAL);
			ItemStack item = player.getInventory().getItem(slot);
			player.getWorld().dropItemNaturally(player.getLocation(), item);
			player.getInventory().setItem(slot, null);
		}
		
		//player.getInventory().clear();
		
		ScoreboardManager.draw(player);	
		
		for(PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());		
		setEditMode(false);
		player.setGameMode(GameMode.SURVIVAL);
		IngameState.giveItems(gameManager.getPlayerProfile(player));
		player.setHealth(20);
		player.setFoodLevel(20);	
		addPotionEffects();	
		player.setLevel(1);
		player.setExp(0);
		if(Gamerules.getValue(Gamerules.quickRespawn) || isStarting) {
			player.teleport(gameManager.getSpawnOfTeam(team, gameManager.getMap()));
		} else {
			Location location = player.getLocation();
			if(player.getLocation().getY() < gameManager.getFloorHight())
				location.setY(gameManager.getFloorHight());

			Location tempLocation = location;
			//Prevent Teleporting into a block
			for(double x = 0; x <= 7; x++) {
				tempLocation = new Location(location.getWorld(), location.getX(), location.getY() + x, location.getZ());
				if(gameManager.getCopiedWorld().getBlockAt(tempLocation).getType() != Material.AIR) {
					tempLocation.setY(tempLocation.getY() - 1);
					break;
				}
			}
			tempLocation.setY(tempLocation.getY() - 1);
			location = tempLocation;
			location.setPitch(90);
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(location);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					player.teleport(gameManager.getSpawnOfTeam(team, gameManager.getMap()));
					player.setGameMode(GameMode.SURVIVAL);
				}
			}, 5*20);
		}
	}

	public Scoreboard getCurrentScoreboard() {
		return currentScoreboard;
	}
	
	public void setCurrentScoreboard(Scoreboard scoreboard) {
		this.currentScoreboard = scoreboard;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void setKills(int number) {
		kills = number;
	}
	
	public void resetKills() {
		setKills(0);
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addDeath() {
		deaths++;
	}
	
	public void setDeaths(int number) {
		deaths = number;
	}
	
	public void resetDeaths() {
		setDeaths(0);
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void resetStats() {
		resetKills();
		resetDeaths();
	}
	
	public void setLastAttacker(Player player) {
		lastAttacker = player;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                clearLastAttacker();
            }
        }, 7*20);
	}
	
	public void clearLastAttacker() {
		lastAttacker = null;
	}
	
	public Player getLastAttacker() {
		return lastAttacker;
	}
	
	public void playSound(Sound sound) {
		if(Gamerules.getValue(Gamerules.soundEffects))
			player.playSound(player.getLocation(), sound, 5, 1);
	}
	
	public String getName() {
		return getTeam().getColorCode()+getPlayer().getName()+"§7";
	}
	
	public Inventory getInventory() {
		//return inventory;
		return normalInventory.buildInventory();
	}
	
	public void setInventory(Inventory inventory) {
		this.normalInventory.setInventory(inventory);
	}
	
	public void teleportToSpawn() {
		Location spawn = MainCommand.getConfigGeneralLocation("lobbyspawn");
		if(spawn != null)
			player.teleport(spawn);
	}
	
	public String getInventoryName() {
		return normalInventory.getTitle();
	}

	public ActionBlock getEditActionBlock() {
		return editActionBlock;
	}

	public void setEditActionBlock(ActionBlock editActionBlock) {
		this.editActionBlock = editActionBlock;
	}

	private void addPotionEffects() {
		if(Gamerules.getValue(Gamerules.aqua)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, Gamerules.getValue(Gamerules.speed) ? 1 : 0, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
		}
		if(Gamerules.getValue(Gamerules.haste))
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, false, false, false));
		if(Gamerules.getValue(Gamerules.jumpboost))
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false, false));
		if(Gamerules.getValue(Gamerules.speed))
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
		if(Gamerules.getValue(Gamerules.invisibility))
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
		if(Gamerules.getValue(Gamerules.glowing))
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
	}

	
}
