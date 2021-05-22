package de.gigaz.cores.util;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	private ItemStack item;
	private ItemMeta itemMeta;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material, 1);
		itemMeta = item.getItemMeta();
	}
	
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}
	
	public ItemBuilder setBreakable(boolean value) {
		itemMeta.setUnbreakable(!value);
		return this;
	}
	
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
}
