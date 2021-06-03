package de.gigaz.cores.classes;

import org.bukkit.inventory.ItemStack;

public class GameruleSetting {
	
	private static final boolean defaultDefaultValue = false;
	
	private boolean value;
	private ItemStack item;
	
	public GameruleSetting(ItemStack item, boolean defaultValue) {
		this.item = item;
		this.value = defaultValue;
	}
	
	public GameruleSetting(ItemStack item) {
		this.item = item;
		this.value = defaultDefaultValue;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public void switchValue() {
		this.value = !this.value;
	}

}
