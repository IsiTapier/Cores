package de.gigaz.cores.classes;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.util.ItemBuilder;

public class GameruleSetting {
	
	public enum GameruleCategory {
		INVENTORY(new ItemBuilder(Material.CHEST).setName("Inventory").build()),
		GAME(new ItemBuilder(Material.COMMAND_BLOCK).setName("§fGame").build()),
		SPECIAL(new ItemBuilder(Material.END_PORTAL_FRAME).setName("Special").build());
		
		private ItemStack item;
		
		private GameruleCategory(ItemStack item) {
			this.item = item;
		}
		
		public ItemStack getItem() {
			return item;
		}
		
		public static GameruleCategory getCategory(ItemStack item) {
				 if(item.equals(INVENTORY.getItem()))
				return INVENTORY;
			else if(item.equals(GAME.getItem()))
				return GAME;
			else if(item.equals(SPECIAL.getItem()))
				return SPECIAL;
			else
				return null;
		}
		
		public static GameruleCategory getCategory(Inventory inventory) {
			 if(inventory.contains(INVENTORY.getItem()))
			return INVENTORY;
		else if(inventory.contains(GAME.getItem()))
			return GAME;
		else if(inventory.contains(SPECIAL.getItem()))
			return SPECIAL;
		else
			return null;
	}
	}
	
	private static final boolean defaultDefaultValue = false;
	
	private boolean value;
	private ItemStack item;
	private int priority;
	private GameruleCategory category;
	
	public GameruleSetting(GameruleCategory category, int priority, ItemStack item, boolean defaultValue) {
		this.category = category;
		this.priority = priority;
		this.item = item;
		this.value = defaultValue;
	}

	public GameruleSetting(GameruleCategory category, int priority, ItemStack item) {
		this.category = category;
		this.priority = priority;
		this.item = item;
		this.value = defaultDefaultValue;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public GameruleCategory getCategory() {
		return category;
	}

	public void setCategory(GameruleCategory category) {
		this.category = category;
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
