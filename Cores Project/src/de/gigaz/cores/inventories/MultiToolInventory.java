package de.gigaz.cores.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class MultiToolInventory {
	
	private static String title = Inventories.getMultiTool().getName();
	private static int rows = 1;

	
	private static Inventory buildInventory(String title, int rows, Team team) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		
		GameManager gameManager = Main.getPlugin().getGameManager();
		ItemBuilder teamRedSelector = Inventories.getTeamRedSelector();
		ItemBuilder teamBlueSelector = Inventories.getTeamBlueSelector();

		if(team.equals(Team.BLUE)) {	
			inventory.setItem(3, new ItemBuilder(teamBlueSelector.getType()).setName(teamBlueSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).setLore(getLore(Team.BLUE)).build());
			inventory.setItem(5, new ItemBuilder(teamRedSelector.getType()).setName(teamRedSelector.getName()).setLore(getLore(Team.RED)).build());
		} else if(team.equals(Team.RED)) {
			inventory.setItem(3, new ItemBuilder(teamBlueSelector.getType()).setName(teamBlueSelector.getName()).setLore(getLore(Team.BLUE)).build());
			inventory.setItem(5, new ItemBuilder(teamRedSelector.getType()).setName(teamRedSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).setLore(getLore(Team.RED)).build());
		} else {
			inventory.setItem(3, teamBlueSelector.setLore(getLore(Team.BLUE)).build());
			inventory.setItem(5, teamRedSelector.setLore(getLore(Team.RED)).build());
		}
		
		return inventory;	
	}
	
	public static Inventory getInventory(Team team) {
		return buildInventory(title, rows, team);
	}

	public static String getTitle() {
		return title;
	}

	public static int getRows() {
		return rows;
	}
	
	private static ArrayList<String> getLore(Team team) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		ArrayList<String> list = new ArrayList<String>();
		for(Player player : gameManager.getPlayersOfTeam(team)) {
			list.add("§8- §7" + player.getName());	
		}
		return list;
	}
}
