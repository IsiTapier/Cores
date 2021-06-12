package de.gigaz.cores.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.special.ActionBlock;
import de.gigaz.cores.special.SpecialItemDrop;
import de.gigaz.cores.util.ItemBuilder;

public class ActionBlockInventory {
	
	private static String title = "§7Action Block Menü";
	private static int rows = 3;		
	
	public static Inventory getInventory(ActionBlock actionBlock) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		
		for(int x = 0; x <= 26; x++) {
			if(x == 9)
				x = 18;
			inventory.setItem(x, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
		}
		int slot = 13;
		int itemSize = SpecialItemDrop.getSpecialItems().size();
		if(itemSize > 2) {
			if(itemSize%2 == 0) {
				slot -= Math.round(itemSize / 2); 				
			}
		}
		for(SpecialItemDrop specialItem : SpecialItemDrop.getSpecialItems()) {
			ItemStack item = specialItem.getItem();
			if(actionBlock.getItems().contains(specialItem)) {
				inventory.setItem(slot, new ItemBuilder(item.getType()).setName(specialItem.getName()).setLore("§7aktiviert").addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
			} else {
				inventory.setItem(slot, new ItemBuilder(item.getType()).setName(specialItem.getName()).setLore("§7deaktiviert").build());
			}		
			slot++;
		}
		
		return inventory;	
	}
	

	public static String getTitle() {
		return title;
	}

	public static int getRows() {
		return rows;
	}
}
