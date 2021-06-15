package de.gigaz.cores.special;

import org.bukkit.Bukkit;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.special.ActionBlock;

public class SpecialItemManager {

    private int scheduleID;
    private GameManager gameManager;
    private int loopCount = 0;

    public SpecialItemManager() {
       gameManager = Main.getPlugin().getGameManager();
       itemSpawnLoop();
    }

    private void itemSpawnLoop() {
    	Bukkit.broadcastMessage("special items loop start");
    	
        scheduleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            
            @Override
            public void run() {                
                for(ActionBlock actionBlock : gameManager.getActionBlocks(gameManager.getMap())) { //Error
                    for(SpecialItemDrop specialItem : actionBlock.getItems()) {
                        if(specialItem.isActive()) {
                        	//Bukkit.broadcastMessage("active item - " + specialItem.getName());
                            if(loopCount%specialItem.getSpawnInterval() == 0) {
                                if(loopCount == 0) {
                                    if(specialItem.isSpawningAtStart()) {
                                    	specialItem.spawnItem(actionBlock.getLocation());
                                    }
                                } else {
                                	//Bukkit.broadcastMessage("spawned - " + specialItem.getName());
                                	specialItem.spawnItem(actionBlock.getLocation());
                                }                     
                            }
                        }      
                    }
                }
                for(SpecialItemDrop item : SpecialItemDrop.getSpecialItems()) {
                    if(item.isActive()) {
                    	boolean AnyActionBlockContainsItem = false;
                    	for(ActionBlock actionBlock : gameManager.getActionBlocks()) {
                    		if(actionBlock.getItems().contains(item)) {
                    			AnyActionBlockContainsItem = true;
                    			break;
                    		}
                    	}
                    	if(AnyActionBlockContainsItem) {
                    		int modulus = loopCount%item.getSpawnInterval();
                            if(modulus == 0) {
                                if(loopCount == 0) {
                                    if(item.isSpawningAtStart()) 
                                        item.broadcastMessage();      
                                } else {
                                    item.broadcastMessage();
                                }    
                            } else if(modulus == Math.round(item.getSpawnInterval()/2)) {
                                Bukkit.broadcastMessage(Main.PREFIX + item.getName() + "§7 spawned in §6" + (item.getSpawnInterval() - modulus) + "§7 Sekunden");
                            }
                    	}
                    }
                }
                loopCount++;   

                if(gameManager.getCurrentGameState().equals(GameState.ENDING_STATE)) {
                    Bukkit.getScheduler().cancelTask(scheduleID);
                }           
            }
        }, 0, 20L);
    }

}
