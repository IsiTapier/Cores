package de.gigaz.cores.inventories;

import java.util.ArrayList;
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
	
	public static Inventory buildInventory() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		HashMap<String, GameruleSetting> gameruleSettings = gameManager.getGameruleSettings();
		int size = 9*(int)Math.ceil((double)gameruleSettings.size()/2);
		Inventory inventory = Bukkit.createInventory(null, size, "Gamerule Settings");
		ItemStack barrier = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
		int i = 0;
		for(Entry<String, GameruleSetting> gamerule : gameruleSettings.entrySet()) {
			ItemStack disable = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("disable").build();
			ItemStack enable = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("enable").build();
			if(i%9==0) {
				inventory.setItem(i, barrier);
				i++;
			}
			if(gamerule.getValue().getValue()) {
				ItemMeta meta = enable.getItemMeta();
				meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
				enable.setItemMeta(meta);
			} else {
				ItemMeta meta = disable.getItemMeta();
				meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
				disable.setItemMeta(meta);
			}
			inventory.setItem(i, disable);
			inventory.setItem(i+1, gamerule.getValue().getItem());
			inventory.setItem(i+2, enable);
			inventory.setItem(i+3, barrier);
			i+=4;
		}
		while(i%9>0) {
			inventory.setItem(i, barrier);
			i++;
		}
		return inventory;
	}
}
