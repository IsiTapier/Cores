package de.gigaz.cores.util;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder implements Cloneable {
	private ItemStack item;
	private ItemMeta itemMeta;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material, 1);
		itemMeta = item.getItemMeta();
	}
	
	public ItemBuilder(ItemBuilder itemBuilder) {
		this.item = itemBuilder.item;
		this.itemMeta = itemBuilder.itemMeta;
	}
	
	public ItemBuilder(Material material, String name, int amount) {
		item = new ItemStack(material, amount);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		
	}
	
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	public String getName() {
		return itemMeta.getDisplayName();
	}
	
	public Material getType() {
		return item.getType();
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


	public ItemStack getItem() {
		return item;
	}

	public ItemMeta getItemMeta() {
		return itemMeta;
	}

}
