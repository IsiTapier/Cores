package de.gigaz.cores.special;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ItemBuilder;

public class SpecialItemDrop {
    
    public static ArrayList<SpecialItemDrop> specialItems = new ArrayList<SpecialItemDrop>() {{
        add(new SpecialItemDrop("§cUltra Stone", new ItemBuilder(Material.STONE).build(), 5, Gamerules.getGameruleSetting(Gamerules.aqua), false));
        add(new SpecialItemDrop("§aMagic Glowstone", new ItemBuilder(Material.GLOWSTONE).build(), 10, Gamerules.getGameruleSetting(Gamerules.armor), true));
    }};

    private GameManager gameManager = Main.getPlugin().getGameManager();
    private ItemStack item;
    private String broadcastMessage;
    private int spawnInterval;
    private GameruleSetting gamerule; 
    private boolean isSpawningAtStart;
    private String name;

    public SpecialItemDrop(String name, ItemStack item, int spawnInterval, GameruleSetting gamerule, boolean isSpawningAtStart, String broadcastMessage) {
        this.item = item;
        this.name = name;
        this.spawnInterval = spawnInterval;
        this.gamerule = gamerule;
        this.broadcastMessage = broadcastMessage;
        this.isSpawningAtStart = isSpawningAtStart;
    }

    public SpecialItemDrop(String name, ItemStack item, int spawnInterval, GameruleSetting gamerule, boolean isSpawningAtStart) {
        this.item = item;
        this.spawnInterval = spawnInterval;
        this.gamerule = gamerule;
        this.isSpawningAtStart = isSpawningAtStart;
        this.broadcastMessage = "";
        this.name = name;
    }

    public void spawnItem(Location location) {
        gameManager.getCopiedWorld().dropItemNaturally(location, item);      
    }

    public void broadcastMessage() {
        if(broadcastMessage != "") {
            Bukkit.broadcastMessage(Main.PREFIX + "§k§8--§r§7" + name + "§k§8--§r§7" + "§7 ist gespawned");
        }
    }

    public ItemStack getItem() {
        return item;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }
    
    public int getSpawnInterval() {
        return spawnInterval;
    }

    public GameruleSetting getGamerule() {
        return gamerule;
    }

    public boolean isActive() {
        return gamerule.getValue();
    }
    
    public boolean isSpawningAtStart() {
        return isSpawningAtStart;
    }

    public static ArrayList<SpecialItemDrop> getSpecialItems() {
        return specialItems;
    }

    public String getName() {
        return name;
    }

    public static SpecialItemDrop getItemByName(String name) {
        for(SpecialItemDrop item : getSpecialItems()) {
            if(item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
