package de.gigaz.cores.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class MainLoop {

    private GameManager gameManager;
    private int moveListenerID;
    private int peepListenerID;
    private int AttackedCoreListenerID;
 
    

    public MainLoop() {
        gameManager = Main.getPlugin().getGameManager();
        MoveListener();
        AttackedCoreListener();
        PeepListener();
    }

    private void MoveListener() {
        moveListenerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            
            @Override
            public void run() {
                for(Player player : gameManager.getCopiedWorld().getPlayers()) {
                    PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
                    Team team = playerProfile.getTeam();
                    if(player.getLocation().getY() <= MainCommand.getConfigLocation("deathhight", gameManager.getMap()).getY()) {
                        if(playerProfile.getLastAttacker() != null) {
                            Player attacker = playerProfile.getLastAttacker();
                            PlayerProfile attackerProfile = Main.getPlugin().getGameManager().getPlayerProfile(attacker);
                            attackerProfile.addKill();
                            ScoreboardManager.draw(attacker);
                            Bukkit.broadcastMessage(Main.getPlugin().getVoidDeathMessage(playerProfile, attackerProfile));
                        } else
                            Bukkit.broadcastMessage(Main.getPlugin().getVoidDeathMessage(playerProfile));
                        playerProfile.addDeath();     
                        playerProfile.respawn();                  
                    }
                    if(gameManager.getCurrentGameState() != GameState.INGAME_STATE) {
                        Bukkit.getScheduler().cancelTask(moveListenerID);
                    }
                }
            }
        }, 0, 10);
               	
    }

    @SuppressWarnings("deprecation")
	private void PeepListener() {
        peepListenerID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(), new Runnable() {	
			@Override
			public void run() {
				for(Core core : gameManager.getCores()) {
					if(core.isAttacked()) {
						
						playSound(core);
					}
				}
                if(gameManager.getCurrentGameState() != GameState.INGAME_STATE) {
                    Bukkit.getScheduler().cancelTask(peepListenerID);
                }				
			}         
		}, 0, 18);
    }

    private void AttackedCoreListener() {
        AttackedCoreListenerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(Core core : gameManager.getCores()) {
					boolean requestAttacked = false;
					for(Player player : gameManager.getCopiedWorld().getPlayers()) {
						PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
						if(core.getTeam() != playerProfile.getTeam()) {
							if(core.getLocation().distance(player.getLocation()) <= 7)
								requestAttacked = true;
						}
					}
					if(requestAttacked) {
						if(!core.isAttacked()) {
							core.setAttacked(true);
							gameManager.playSound(Sound.BLOCK_BELL_RESONATE, gameManager.getCopiedWorld(), 3, core.getTeam());
							for(Player loopPlayer : gameManager.getCopiedWorld().getPlayers()) {
								Team loopTeam = gameManager.getPlayerProfile(loopPlayer).getTeam();
								if(core.getTeam() == loopTeam) {
									loopPlayer.sendMessage(Main.PREFIX + "§4Der Core §6" + core.getDisplayName()+ "§4 wird attackiert");													
								}
							}
							ScoreboardManager.drawAll();
						}
						core.setAttacked(true);
					} else {
						if(core.isAttacked()) {
							core.setAttacked(false);
							ScoreboardManager.drawAll();
						}
						core.setAttacked(false);
					}	
				}										
                if(gameManager.getCurrentGameState() != GameState.INGAME_STATE) {
                    Bukkit.getScheduler().cancelTask(AttackedCoreListenerID);
                }
            }
		}, 0, 10);	
	}
    

    private void playSound(Core core) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		gameManager.playSound(Sound.BLOCK_NOTE_BLOCK_BELL, gameManager.getCopiedWorld(), 5, core.getTeam());	
	}
}
