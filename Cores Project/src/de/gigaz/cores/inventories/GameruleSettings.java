package de.gigaz.cores.inventories;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.ItemBuilder;

public class GameruleSettings {
	
	private static final boolean useTwoInputs = false;
	
	private static ItemStack disable = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("disable"+(useTwoInputs?"":"d")).build();
	private static ItemStack enable = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("enable"+(useTwoInputs?"":"d")).build();
	
	private static int page = 0;
	
	public static Inventory buildInventory() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		HashMap<String, GameruleSetting> gameruleSettings = gameManager.getGameruleSettings();
		int rows = (int)Math.ceil((double)gameruleSettings.size()/(useTwoInputs?2:4));
		int pages = (int)Math.ceil((double)rows/6);
		if(page > pages)
			page = 1;
		rows -= 6*(page-1);
		if(rows > 6)
			rows = 6;
		Inventory inventory = Bukkit.createInventory(null, rows*9, "Gamerule Settings");
		ItemStack barrier = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
		int i = 0;
		for(Entry<String, GameruleSetting> gamerule : gameruleSettings.entrySet()) {
			if(i < (page-1)*6*9) {
				if(i%9==0)
					i++;
				if(useTwoInputs)
					i+=4;
				else
					i+=2;
				continue;
			}
			if(i%9==0) {
				inventory.setItem((i%(6*9)), barrier);
				i++;
			}
			ItemStack inputSlot;
			if(gamerule.getValue().getValue()) {
				if(useTwoInputs) {
					ItemMeta meta = enable.getItemMeta();
					meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
					enable.setItemMeta(meta);
					if(disable.containsEnchantment(Enchantment.ARROW_INFINITE))
						disable.removeEnchantment(Enchantment.ARROW_INFINITE);
				} else
					inputSlot = enable;
			} else {
				if(useTwoInputs) {
					ItemMeta meta = disable.getItemMeta();
					meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
					disable.setItemMeta(meta);
					if(enable.containsEnchantment(Enchantment.ARROW_INFINITE))
						enable.removeEnchantment(Enchantment.ARROW_INFINITE);
				} else
					inputSlot = disable;
			}
			if(useTwoInputs) {
				inventory.setItem((i%(6*9)), disable);
				inventory.setItem((i%(6*9))+1, gamerule.getValue().getItem());
				inventory.setItem((i%(6*9))+2, enable);
				inventory.setItem((i%(6*9))+3, barrier);
				i+=4;
			} else {
				inventory.setItem((i%(6*9)), gamerule.getValue().getItem());
				inventory.setItem((i%(6*9))+1, inputSlot);
				i+=2;
			}
			if(i >= ((page-1)*6+rows)*9)
				break;
		}
		while(i%9>0) {
			inventory.setItem((i%(6*9)), barrier);
			i++;
		}
		return inventory;
	}
	
	public static boolean getUiMode() {
		return useTwoInputs;
	}
	
	public static ItemStack getEnable() {
		return enable;
	}
	
	public static ItemStack getDisable() {
		return disable;
	}
	
	public static void nextPage() {
		page++;
	}
}
