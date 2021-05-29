package de.gigaz.cores.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import de.gigaz.cores.util.ItemBuilder;

public class AdminToolInventory {
	
	private static String title = "§cAdmin Tools";
	private static int rows = 1;
	
	private static ItemBuilder adminSelectMap = new ItemBuilder(Material.MAP).setName("§7Select Map");
	private static ItemBuilder adminStartGame = new ItemBuilder(Material.CLOCK).setName("§7Start Game");
	private static ItemBuilder adminEditTeams = new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7Edit Teams");
	private static ItemBuilder adminEditMode = new ItemBuilder(Material.IRON_PICKAXE).setName("§7EditMode");

	
	private static Inventory buildInventory(String title, int rows) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		
		inventory.setItem(1, adminSelectMap.build());
		inventory.setItem(2, adminStartGame.build());
		inventory.setItem(6, adminEditTeams.build());
		inventory.setItem(7, adminEditMode.build());
		
		return inventory;	
	}
	
	public static Inventory getInventory() {
		return buildInventory(title, rows);
		
	}

	public static String getTitle() {
		return title;
	}

	public static int getRows() {
		return rows;
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
