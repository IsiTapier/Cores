package de.gigaz.cores.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.ItemBuilder;

public class MapSelectInventory {
	
	private static String title = "§8Select Map";
	private static int rows = 3;

	
	private static Inventory buildInventory(String title, int rows) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		
		int x = 0;
		for(World world : Bukkit.getWorlds()) {		
			if(Main.getPlugin().getGameManager().checkMap(world)) {
				ItemStack item = new ItemBuilder(Material.GRASS_BLOCK).setName("§7" + world.getName()).build();;
				inventory.setItem(x, item);
				x++;
			}		
		}
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
	
}
