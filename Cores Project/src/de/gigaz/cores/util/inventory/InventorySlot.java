package de.gigaz.cores.util.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.main.Main;

public class InventorySlot {
	private InventoryItem template;
	private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
	
	public InventorySlot() {}
	
	public InventorySlot(InventoryItem... items) {
		this.items = new ArrayList<InventoryItem>(Arrays.asList(items));
	}

	/*public InventorySlot(InventoryItem template, InventoryItem... items) {
		this.template = template;
		this.items = new ArrayList<InventoryItem>(Arrays.asList(items));
	}
	
	/*public int getNextPriority() {
		int next = 0;
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getValue() == next) {
				next++;
				i = 0;
			}
		}
		return next;
	}*/
	
	public InventoryItem getTemplate() {
		return template;
	}
	
	public void setTemplate(InventoryItem template) {
		this.template = template;
	}
	
	public void setItems(ArrayList<InventoryItem> items) {
		this.items = items;
	}
	
	public ItemStack getItem(boolean template) {
		if(template && this.template != null)
			return this.template.getItem();
		else {
			if(items.size() == 0)
				return null;
			InventoryItem item = items.get(0);
			for(int i = 1; i < items.size(); i++)
				if(items.get(i).isValid() && (items.get(i).getPriority() < item.getPriority() || !item.isValid()))
					item = items.get(i);
			if(!item.isValid())
				return null;
			else
				return item.getItem();
		}	
	}
	
	public ArrayList<InventoryItem> getItems() {
		return items;
	}

	public ItemStack getItem() {
		return getItem(false);
	}
	
	public ArrayList<ItemStack> getOverFlow() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for(InventoryItem item : this.items)
			if(item.isValid() && !item.getItem().equals(getItem()))
				items.add(item.getItem());
		return items;
	}
	
	public void addItem(InventoryItem item) {
		items.add(item);
	}
	
	public boolean containsItem(int id) {
		return getItem(id) != null;
	}

	public InventoryItem getItem(int id) {
		for(InventoryItem item : this.items)
			if(item.getId() == id)
				return item;
		return null;
	}
	
	public void removeItem(int id) {
		items.remove(items.indexOf(getItem(id)));
	}
	
	public boolean hasItem(int id) {
		if(getItem(id) != null)
			return true;
		else
			return false;
	}
	
	public boolean hasTemplate() {
		return template != null;
	}
	
	public boolean isTemplate(int id) {
		if(hasTemplate() && template.getId() == id)
			return true;
		else
			return false;
	}
}
