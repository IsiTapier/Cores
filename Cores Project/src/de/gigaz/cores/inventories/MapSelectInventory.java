package de.gigaz.cores.inventories;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.ItemBuilder;

public class MapSelectInventory {
	private static final boolean showUnsetMaps = true;
	private static final boolean showIncompleteMaps = true;
	
	private static final ArrayList<String> ignoreWorlds = new ArrayList<String>(Arrays.asList("world", "world_nether", "world_the_end"));
	
	private static String titleAdmin = "§8Select Map";
	private static String titleNormal = "§8Vote for Map";
	private static int rows = (int) Math.ceil((Bukkit.getWorlds().size()-ignoreWorlds.size())/9.0);
	private static Material defaultMaterial = Material.GRASS_BLOCK;

	
	private static Inventory buildInventory(String title, int rows, boolean showUnsetMaps, boolean showIncompleteMaps) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		GameManager gameManager = Main.getPlugin().getGameManager();
		FileConfiguration config = Main.getPlugin().getConfig();
		String root;
		Material material;
		int x = 0;
		for(World world : Bukkit.getWorlds()) {
			if(ignoreWorlds.contains(world.getName()))
				continue;
			root = Main.CONFIG_ROOT + "worlds." + world.getName();
			material = defaultMaterial;
			     if(!config.contains(root) && !showUnsetMaps)
				continue;
			else if(!config.contains(root))
				material = Material.WHITE_STAINED_GLASS_PANE;
			else if(!gameManager.checkMap(world) && !showIncompleteMaps)
				continue;
			else if(!gameManager.checkMap(world))
				material = Material.BARRIER;
			else if(config.contains(root+".item"))
				material = Material.getMaterial((String) config.get(root+".item"));
			ItemStack item = new ItemBuilder(material).setName(world.getName()).setLore(material.equals(Material.WHITE_STAINED_GLASS_PANE) ? "map not added yet" : material.equals(Material.BARRIER) ? "map not completely set yet" : "").build();
			inventory.setItem(x, item);
			x++;
		}
		return inventory;
	}
	
	public static Inventory getAdminInventory() {
		return buildInventory(titleAdmin, rows, showUnsetMaps, showIncompleteMaps);
	}
	
	public static Inventory getNormalInventory() {
		return buildInventory(titleNormal, rows, false, false);
	}

	public static String getTitle(boolean isAdmin) {
		return isAdmin ? titleAdmin : titleNormal;
	}

	public static int getRows() {
		return rows;
	}
	
}
