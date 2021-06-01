
package de.gigaz.cores.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.gigaz.cores.main.Main;

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.Container;
import net.minecraft.server.v1_16_R3.ContainerAccess;
import net.minecraft.server.v1_16_R3.ContainerAnvil;
import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;


public class AnvilGUI {
	
	//anvil slot enum
	public enum AnvilSlot {
		INPUT_LEFT(0),
		INPUT_RIGHT(1),
		OUTPUT(2);
		
		private int value;
	    private static Map map = new HashMap<>();

	    private AnvilSlot(int value) {
	        this.value = value;
	    }

	    static {
	        for (AnvilSlot anvilSlot : AnvilSlot.values()) {
	            map.put(anvilSlot.value, anvilSlot);
	        }
	    }

	    public static AnvilSlot valueOf(int value) {
	        return (AnvilSlot) map.get(value);
	    }

	    public int getValue() {
	        return value;
	    }
	}
	
	//event class
	public class AnvilClickEvent {
        private AnvilSlot slot;
        
        //item
        private ItemStack item;
        private Material type;
        private String name;
 
        private boolean close = true;
        private boolean destroy = true;
 
        public AnvilClickEvent(AnvilSlot slot, ItemStack item) {
            this.slot = slot;
            this.item = item;
            
            if(item == null)
            	return;
            type = item.getType();
            if(item.hasItemMeta()) {
            	ItemMeta meta = item.getItemMeta();
            	if(meta.hasDisplayName())
            		name = meta.getDisplayName();
        	}
        }
 
        public AnvilSlot getSlot() {
            return slot;
        }
        
        public ItemStack getItem() {
        	return item;
        }
        
        public Material getType() {
        	return type;
        }
 
        public String getName() {
            return name;
        }
 
        public boolean getWillClose() {
            return close;
        }
 
        public void setWillClose(boolean close) {
            this.close = close;
        }
 
        public boolean getWillDestroy() {
            return destroy;
        }
 
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
	
	//event handler
    public interface AnvilClickEventHandler {
        public void onAnvilClick(AnvilClickEvent event);
    }
    
    //variables
	private Player player;
	private EntityPlayer entityPlayer;
	
	private Inventory inventory;
	private Container container;
	
    private Listener listener;
	
    //functions
	public AnvilGUI(Player player, String name, String title, final AnvilClickEventHandler handler) {
		//set variables
		this.player = player;
		entityPlayer = ((CraftPlayer)player).getHandle();
		
		//register events
        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if(event.getWhoClicked() instanceof Player){
                    Player clicker = (Player) event.getWhoClicked();
 
                    if(event.getInventory().equals(inventory)) {
                        event.setCancelled(true);
 
                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
 
                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.valueOf(slot), item);
                        handler.onAnvilClick(clickEvent);
 
                        if(clickEvent.getWillClose())
                            event.getWhoClicked().closeInventory();
                        if(clickEvent.getWillDestroy())
                            destroy();
                    }
                }
            }
 
            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if(event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inv = event.getInventory();
 
                    if(inv.equals(inventory)) {
                        inv.clear();
                        destroy();
                    }
                }
            }
 
            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
            	if(event.getPlayer().equals(player))
                    destroy();
            }
        };
        
        Bukkit.getPluginManager().registerEvents(listener, Main.getPlugin());
        
		
		//open GUI
		int containerCounter = entityPlayer.nextContainerCounter();
		
		/*
		World world = ((CraftWorld)player.getWorld()).getHandle();
		BlockPosition position = new BlockPosition(0, 0, 0);
		ContainerAccess access = ContainerAccess.at(world, position);
		ContainerAnvil anvil = new ContainerAnvil(containerCounter, entityPlayer.inventory, access);
		Container container = CraftEventFactory.callInventoryOpenEvent(entityPlayer, anvil);
		*/
		container = CraftEventFactory.callInventoryOpenEvent(entityPlayer, new ContainerAnvil(containerCounter, entityPlayer.inventory, ContainerAccess.at(((CraftWorld)player.getWorld()).getHandle(), new BlockPosition(0, 0, 0))));
		if(container == null) return;
		
		/*
		IChatBaseComponent displayName = new ChatComponentText(name);
		PacketPlayOutOpenWindow window = new PacketPlayOutOpenWindow(containerCounter, Containers.ANVIL, displayName);
		entityPlayer.playerConnection.sendPacket(window);
		*/
		entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, Containers.ANVIL, new ChatComponentText(name)));
		
		container.checkReachable = false;
		container.setTitle(new ChatComponentText(title));
		container.addSlotListener(entityPlayer);
		update();
		
		inventory = player.getOpenInventory().getTopInventory();
	}
	
	private void update() {
		entityPlayer.activeContainer = container;
	}
	
	public AnvilGUI setSlot(AnvilSlot slot, ItemStack item) {
		container.setItem(slot.value, CraftItemStack.asNMSCopy(item));
		update();
		
		return this;
	}
	
	public ItemStack getSlot(AnvilSlot slot) {
		return player.getOpenInventory().getItem(slot.getValue());
	}
	
	public void destroy() {
        HandlerList.unregisterAll(listener);
        player = null;
        entityPlayer = null;
        inventory = null;
        container = null;
        listener = null;
    }
	
	
	
 
 
	
}