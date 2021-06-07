package de.gigaz.cores.classes;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import de.gigaz.cores.util.Team;
import de.gigaz.cores.util.inventory.InventoryClass;
import de.gigaz.cores.util.inventory.InventoryItem;
import de.gigaz.cores.util.inventory.InventoryItem.DisplayCondition;
import de.gigaz.cores.util.inventory.InventorySlot;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
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
	private Inventory inventory;
	private GameManager gameManager = Main.getPlugin().getGameManager();
	private InventoryClass normalInventory;
	
	private boolean getGamerule(String name) {
		return gameManager.getGameruleSetting(name).getValue();
	}
	
	public PlayerProfile(Player player) {
		this.player = player;
		inventory = Inventories.getDefaultInventory();
		normalInventory = getNormalInventory();
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
	}
	
	public void respawn() {
		respawn(false);
	}

	public void respawn(boolean isStarting) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		player.getInventory().clear();
		
		ScoreboardManager.draw(player);	
		
		for(PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());		

		player.setGameMode(GameMode.SURVIVAL);
		IngameState.giveItems(gameManager.getPlayerProfile(player));
		player.setHealth(20);
		player.setFoodLevel(20);	
		addPotionEffects();	

		if(gameManager.getGameruleSetting(gameManager.quickRespawnGamerule).getValue() || isStarting) {
			player.teleport(gameManager.getSpawnOfTeam(team, gameManager.getMap()));
			
		} else {
			Location location = player.getLocation();
			if(player.getLocation().getY() < gameManager.getSpawnOfTeam(Team.RED, gameManager.getMap()).getY())
				location.setY(gameManager.getSpawnOfTeam(Team.RED, gameManager.getMap()).getY());

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
	
	public String getName() {
		return getTeam().getColorCode()+getPlayer().getName()+"§7";
	}
	
	public Inventory getInventory() {
		//return inventory;
		return normalInventory.buildInventory();
	}
	
	public void setInventory(Inventory inventory) {
		//this.inventory = inventory;
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

	private void addPotionEffects() {
	if(gameManager.getGameruleSetting(gameManager.aquaGamerule).getValue()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, gameManager.getGameruleSetting(gameManager.speedGamerule).getValue() ? 1 : 0, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
		}
		if(gameManager.getGameruleSetting(gameManager.hasteGamerule).getValue())
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, false, false, false));
		if(gameManager.getGameruleSetting(gameManager.jumpboostGamerule).getValue())
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false, false));
		if(gameManager.getGameruleSetting(gameManager.speedGamerule).getValue())
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
		if(gameManager.getGameruleSetting(gameManager.invisibilityGamerule).getValue())
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
		if(gameManager.getGameruleSetting(gameManager.glowingGamerule).getValue())
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
	}

	private InventoryClass getNormalInventory() {
		return new InventoryClass("normal inventory", 18, new HashMap<Integer, InventorySlot>() {{
		put(0, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_SWORD).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.swordGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.STICK).setName("Knockback Stick").addEnchantment(Enchantment.KNOCKBACK, 2).hideEnchants().build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.knockbackStickGamerule);}})));
		put(1, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.CROSSBOW).setName("X-BOW").setLore("The legend X-Bow of strengthness.").setAmount(1).addEnchantment(Enchantment.QUICK_CHARGE, 5).hideEnchants().setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.superCrossbowGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.CROSSBOW).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.crossbowGamerule);}}),
				new InventoryItem(2, new ItemBuilder(Material.BOW).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.bowGamerule);}})));
		put(2, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_AXE).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.combatAxeGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.GOLDEN_AXE).setBreakable(false).addEnchantment(Enchantment.DIG_SPEED, 1).hideEnchants().build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.axeGamerule);}})));
		put(3, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_LOG).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_STEM).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_STEM).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && getGamerule(gameManager.blocksGamerule);}})));
		put(4, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && getGamerule(gameManager.blocksGamerule);}})));
		put(5, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && getGamerule(gameManager.blocksGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && getGamerule(gameManager.blocksGamerule);}})));
		put(6, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.goldApplesGamerule);}}),
				new InventoryItem(1, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.moreGoldApplesGamerule);}})));
		put(7, new InventorySlot(
				new InventoryItem(1, new ItemBuilder(Material.ARROW).setAmount(12).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.arrowsGamerule);}}),
				new InventoryItem(2, new ItemBuilder(Material.ARROW).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.moreArrowsGamerule);}}),
				new InventoryItem(0, new ItemBuilder(Material.ARROW).setName("§6Infinity Arrow").addEnchantment(Enchantment.ARROW_INFINITE, 10).setAmount(1).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.infiniteArrowsGamerule) && !(getGamerule(gameManager.superCrossbowGamerule) || getGamerule(gameManager.crossbowGamerule));}}),
				new InventoryItem(0, new ItemBuilder(Material.ARROW).setName("§6Infinity Arrow").addEnchantment(Enchantment.ARROW_INFINITE, 10).setAmount(2).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.infiniteArrowsGamerule) && (getGamerule(gameManager.superCrossbowGamerule) || getGamerule(gameManager.crossbowGamerule));}}),
				new InventoryItem(5, new ItemBuilder(Material.FLINT_AND_STEEL).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.flintandsteelGamerule);}}),
				new InventoryItem(6, new ItemBuilder(Material.SNOWBALL).setAmount(16).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.snowballGamerule);}}),
				new InventoryItem(7, new ItemBuilder(Material.LAVA_BUCKET).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.lavaBukketGamerule);}}),
				new InventoryItem(8, new ItemBuilder(Material.WATER_BUCKET).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.waterBukketGamerule);}}),
				new InventoryItem(3, new ItemBuilder(Material.ENDER_PEARL).setAmount(8).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.enderpearlGamerule);}}),
				new InventoryItem(4, new ItemBuilder(Material.FISHING_ROD).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.fishingRodGamerule);}}),
				new InventoryItem(0, new ItemBuilder(Material.WITHER_SKELETON_SKULL).setName("Wither Skull").setLore("Right click to throw a wither skull").build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.witherSkullGamerule);}})));
		put(8, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_PICKAXE).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return getGamerule(gameManager.pickaxeGamerule);}})));
		}});
		
	}
}
