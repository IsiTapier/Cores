package de.gigaz.cores.util.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryItem {
	private int id;
	private int priority;
	private ItemStack item;
	private DisplayCondition condition;
	//TODO set item id
	
	public interface DisplayCondition {
        public boolean getCondition();
    }
	
	public InventoryItem(int id, int priority, ItemStack item, DisplayCondition condition) {
		this.id = id;
		this.priority = priority;
		this.item = item;
		this.condition = condition;
		
	}
	
	public InventoryItem(int priority, ItemStack item, DisplayCondition condition) {
		this.priority = priority;
		this.item = item;
		this.condition = condition;
	}
	
	public InventoryItem(ItemStack item, DisplayCondition condition) {
		this.priority = -1;
		this.item = item;
		this.condition = condition;
	}
	
	public InventoryItem(int id,  InventoryItem instance) {
		this.id = id;
		this.priority = instance.getPriority();
		this.item = instance.getItem();
		this.condition = instance.getCondition();
		setId(id);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.hasLore())
			lore = meta.getLore();
		else
			lore = new ArrayList<String>();	
		lore.add(String.valueOf(id));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public DisplayCondition getCondition() {
		return condition;
	}

	public void setCondition(DisplayCondition condition) {
		this.condition = condition;
	}
	
	public boolean isValid() {
		return condition.getCondition();
		//TODO do condition
	}
}
