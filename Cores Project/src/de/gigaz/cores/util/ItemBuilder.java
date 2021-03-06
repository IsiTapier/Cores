package de.gigaz.cores.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
	
	public ItemBuilder setLore(ArrayList<String> list) {
		itemMeta.setLore(list);
		return this;
	}
	
	public ItemBuilder clearLore() {
		itemMeta.setLore(null);
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
	
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		itemMeta.removeEnchant(enchantment);
		return this;
	}
	
	public ItemBuilder disenchant() {
		for(Enchantment enchantment : itemMeta.getEnchants().keySet())
			itemMeta.removeEnchant(enchantment);
		return this;
	}
	
	public ItemBuilder hideEnchants() {
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
	
	public List<String> getLore() {
		return itemMeta.getLore();
	}
	
	public static ItemStack rename(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack setColor(ItemStack armor, Color c) {
		LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
		meta.setColor(c);
		armor.setItemMeta(meta);
		return armor;
	}
	
	public static ItemStack addLore(ItemStack item, String text) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<String>();
		lore.add(text);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack removeLore(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(new ArrayList<String>());
		item.setItemMeta(meta);
		return item;
	}

}
