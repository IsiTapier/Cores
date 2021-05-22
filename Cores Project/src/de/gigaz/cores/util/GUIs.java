package de.gigaz.cores.util;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;

public class GUIs {
	
	private static ItemBuilder adminSelectMap = new ItemBuilder(Material.MAP).setName("§7Select Map");
	private static ItemBuilder adminStartGame = new ItemBuilder(Material.CLOCK).setName("§7Start Game");
	private static ItemBuilder adminEditTeams = new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7Edit Teams");
	private static ItemBuilder adminEditMode = new ItemBuilder(Material.IRON_PICKAXE).setName("§7EditMode");

	public static Inventory getMultiToolInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, Inventories.getMultiTool().getItemMeta().getDisplayName());
		
		GameManager gameManager = Main.getPlugin().getGameManager();
		ItemBuilder teamRedSelector = Inventories.getTeamRedSelector();
		ItemBuilder teamBlueSelector = Inventories.getTeamBlueSelector();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		
		if(playerProfile.getTeam().equals(Team.BLUE)) {
			inv.setItem(3, new ItemBuilder(teamBlueSelector.getType()).setName(teamBlueSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
			inv.setItem(5, teamRedSelector.build());
		} else if(playerProfile.getTeam().equals(Team.RED)) {
			inv.setItem(3, teamBlueSelector.build());
			inv.setItem(5, new ItemBuilder(teamRedSelector.getType()).setName(teamBlueSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
		} else {
			inv.setItem(3, teamBlueSelector.build());
			inv.setItem(5, teamRedSelector.build());
		}
		inv.setItem(3, Inventories.getTeamBlueSelector().build());
		inv.setItem(5, Inventories.getTeamRedSelector().build());

		return inv;
	}
	public static Inventory getAdminToolInventory() {
		Inventory inv = Bukkit.createInventory(null, 9, Inventories.getAdminTool().getItemMeta().getDisplayName());
		
		inv.setItem(1, adminSelectMap.build());
		inv.setItem(2, adminStartGame.build());
		inv.setItem(6, adminEditTeams.build());
		inv.setItem(7, adminEditMode.build());
		return inv;
	}
	
	public static Inventory getMapSelectInventory() {
		Inventory inv = Bukkit.createInventory(null, 3*9, GUIs.getAdminSelectMap().getItemMeta().getDisplayName());
		
		int x = 0;
		for(World world : Bukkit.getWorlds()) {
			if(MainCommand.getConfigLocation("blue.spawn", world) == null) continue;
			if(MainCommand.getConfigLocation("red.spawn", world) == null) continue;
			inv.setItem(x, new ItemBuilder(Material.GRASS_BLOCK).setName("§7" + world.getName()).build());			
			x++;
		}
		return inv;
	}
	
	public static ItemBuilder getAdminSelectMap() {
		return adminSelectMap;
	}

	public static ItemBuilder getAdminStartGame() {
		return adminStartGame;
	}

	public static ItemBuilder getAdminEditTeams() {
		return adminEditTeams;
	}

	public static ItemBuilder getAdminEditMode() {
		return adminEditMode;
	}
	
	
}
