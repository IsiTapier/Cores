package de.gigaz.cores.classes;

import org.bukkit.inventory.ItemStack;

public class GameruleSetting {
	
	private boolean value = false;
	private ItemStack item;
	
	public GameruleSetting(ItemStack item) {
		this.item = item;
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

}
