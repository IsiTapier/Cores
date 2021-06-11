package de.gigaz.cores.util.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.util.ItemBuilder;

public class InventoryClass {
	private String title;
	private int size;
	private int currentid = 0;
	private Inventory inventory;
	private HashMap<Integer, InventorySlot> slots = new HashMap<Integer, InventorySlot>();
	private HashMap<Integer, Integer> overflowAssignment = new HashMap<Integer, Integer>(); //Id / Slot
	
	public InventoryClass(String title, int size) {
		this.title = title;
		this.size = size;
		this.inventory = Bukkit.createInventory(null, size, title);
		for(int i = 0; i < size; i++)
			slots.put(i, new InventorySlot());
	}
	
	public InventoryClass(String title, int size, HashMap<Integer, InventorySlot> slots) {
		this.title = title;
		this.size = size;
		this.inventory = Bukkit.createInventory(null, size, title);
		//this.slots = slots;
		for(int i = 0; i < size; i++)
			if(slots.containsKey(i))
				setSlot(i, slots.get(i));
			else
				this.slots.put(i, new InventorySlot());
	}
	//TODO include template
	public Inventory buildInventory(boolean template) {
		inventory = Bukkit.createInventory(null, size, title);
		inventory.clear();
		
		for(Entry<Integer, InventorySlot> slot : slots.entrySet())
			if(slot.getValue().getOverFlow() != null || !overflowAssignment.containsKey(Integer.parseInt(slot.getValue().getItem().getItemMeta().getLore().get(slot.getValue().getItem().getItemMeta().getLore().size()-1))))
				inventory.setItem(slot.getKey(), slot.getValue().getItem(template));
		
		for(Entry<Integer, Integer> assignment : overflowAssignment.entrySet()) {
			if(inventory.getItem(assignment.getValue()) == null)
				inventory.setItem(assignment.getValue(), getItem(assignment.getKey()).getItem());
		}
		
		//TODO check if item is not assigned
		for(ItemStack item : getOverflow()) {
			if(item == null)
				continue;
			int id = Integer.parseInt(InventoryItem.convertToVisibleString(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
			if(!overflowAssignment.containsKey(id) || !inventory.getItem(overflowAssignment.get(id)).equals(getItem(id).getItem()))
				inventory.addItem(item);
		}
		//return inventory;
		Inventory out = Bukkit.createInventory(null, size, title);
		out.setContents(inventory.getContents());
		
		return out;
	}
	
	public Inventory buildInventory() {
		return buildInventory(false);
	}

	public void addItem(int slot, InventoryItem item) {
		slots.get(slot).addItem(new InventoryItem(currentid, item));
		currentid++;
	}
	
	public void setSlot(int slotNum, InventorySlot slot) {
		InventorySlot newSlot = new InventorySlot();
		for(InventoryItem item : slot.getItems()) {
			newSlot.addItem(new InventoryItem(currentid, item));
			currentid++;
		}
		slots.put(slotNum, newSlot);
	}
	
	public ArrayList<ItemStack> getOverflow() {
		ArrayList<ItemStack> overflow = new ArrayList<ItemStack>();
		for(Entry<Integer, InventorySlot> slot : slots.entrySet())
			for(ItemStack item : slot.getValue().getOverFlow())
				overflow.add(item);
		return overflow;
	}
	
	public void changeSlotOfItem(InventoryItem item, int slot) {
		int id = item.getId();
		getSlotOfItem(id).removeItem(id);
		slots.get(slot).addItem(item);
	}
	
	public void setInventory(Inventory inventory) {
		//Player player = (Player) inventory.getHolder();
		//if(!player.getOpenInventory().getTitle().equals(title) && false)
			//return;
		HashMap<InventorySlot, Boolean> overflowSlots = new HashMap<InventorySlot, Boolean>();
		for(ItemStack item : inventory.getStorageContents()) {
			if(item == null)
				continue;
			if(!item.hasItemMeta())
				continue;
			if(!item.getItemMeta().hasLore())
				continue;
			int id = 0;
			//TODO get idä
			id = Integer.parseInt(InventoryItem.convertToVisibleString(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
			InventorySlot slot = getSlotOfItem(id);
			if(!overflowSlots.containsKey(slot))
				overflowSlots.put(slot, false);
			else if(!overflowSlots.get(slot))
				overflowSlots.put(slot, true);
		}
		for(ItemStack item : inventory.getStorageContents()) {
			if(item == null)
				continue;
			if(this.inventory.first(item) == inventory.first(item))
				continue;
			int id = 0;
			//TODO get id
			id = Integer.parseInt(InventoryItem.convertToVisibleString(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
			InventorySlot slot = getSlotOfItem(id);
			if(overflowSlots.containsKey(slot) && overflowSlots.get(slot) && !item.equals(slot.getItem())) {
				//is overflow
				overflowAssignment.put(id, inventory.first(item));
			} else {
				if(overflowSlots.containsKey(slot) && overflowSlots.get(slot)&&false) {
					if(inventory.first(item) != getSlotNumberOfItem(id)) {
						slots.get(inventory.first(item)).addItem(slot.getItem(id));
						slot.removeItem(id);
					}
				} else {
					//is not overflow
					InventorySlot oldSlot = getSlotOfItem(id);
					slots.put(getSlotNumberOfItem(id), slots.get(inventory.first(item)));
					slots.put(inventory.first(item), oldSlot);
				}
			}
		}
		/*for(Entry<Integer, Integer> i : overflowAssignment.entrySet()) {
			Bukkit.broadcastMessage("ID"+i.getKey()+"slot"+i.getValue());
		}
		for(Entry<Integer, InventorySlot> slot : slots.entrySet()) {
			Bukkit.broadcastMessage(slot.getKey()+" "+slot.getValue().getItems().toString());
		}*/
	}
	
	public int getSlotNumberOfItem(int id) {
		for(int i = 0; i < size; i++)
			if(slots.get(i).containsItem(id))
				return i;
		return -1;
	}
	
	public InventorySlot getSlotOfItem(int id) {
		for(int i = 0; i < size; i++)
			if(slots.get(i).containsItem(id))
				return slots.get(i);
		return null;
	}
	
	public InventoryItem getItem(int id) {
		if(getSlotOfItem(id) == null)
			return null;
		return getSlotOfItem(id).getItem(id);
	}
	
	public String getTitle() {
		return title;
	}
	
}
	