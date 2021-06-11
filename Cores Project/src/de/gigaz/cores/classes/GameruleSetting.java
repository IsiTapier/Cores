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
		
		public ItemStack getItem(boolean reset) {
			if(!reset)
				return item;
			return ItemBuilder.addLore(item.clone(), "click to reset");
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
				 if(inventory.contains(INVENTORY.getItem(true)))
				return INVENTORY;
			else if(inventory.contains(GAME.getItem(true)))
				return GAME;
			else if(inventory.contains(SPECIAL.getItem(true)))
				return SPECIAL;
			else
				return null;
		}
	}
	
	private static final boolean defaultDefaultValue = false;
	private static final int defaultCurrentItem = 0;
	
	private int defaultValue;
	private boolean value;
	private ItemStack[] items;
	private int currentItem;
	private int priority;
	private GameruleCategory category;
	
	public GameruleSetting(GameruleCategory category, int priority, boolean defaultValue, ItemStack... items) {
		this.category = category;
		this.priority = priority;
		this.items = items;
		setValues(defaultValue);
	}
	
	public GameruleSetting(GameruleCategory category, int priority, int defaultValue, ItemStack... items) {
		this.category = category;
		this.priority = priority;
		this.items = items;
		setValues(defaultValue);
	}

	public GameruleSetting(GameruleCategory category, int priority, ItemStack... items) {
		this.category = category;
		this.priority = priority;
		this.items = items;
		this.value = defaultDefaultValue;
		currentItem = defaultCurrentItem;
	}
	
	private void setValues(int value) {
		defaultValue = value;
		this.value = defaultValue!=0;
		if(defaultValue == 0)
			currentItem = defaultCurrentItem;
		else
			currentItem = defaultValue-1;
		if(defaultValue < 0) {
			this.value = false;
			currentItem = Math.abs(defaultValue)-1;
		}
		setItem(currentItem);
	}
	
	private void setValues(boolean value) {
		this.value = value;
		defaultValue = value?1:0;
		currentItem = defaultCurrentItem;
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
		return items[currentItem];
	}
	
	public void setItems(ItemStack... items) {
		this.items = items;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public int getValue(boolean isInt) {
		if(!value)
			return 0;
		setItem(currentItem);
		if(items.length == 0)
			return 0;
		return currentItem+1;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public void switchValue() {
		this.value = !this.value;
	}
	
	public void setItem(int index) {
		if(index >= items.length)
			index = items.length-1;
		if(index < 0)
			index = 0;
		currentItem = index;
	}
	
	public void nextItem() {
		currentItem++;
		if(currentItem >= items.length)
			currentItem = 0;
	}
	
	public void reset() {
		setValues(defaultValue);
	}

}
