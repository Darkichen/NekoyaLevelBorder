package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Configs;
import at.darkichen.levelBorder.utils.LevelApi;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelUpListener implements Listener {

    private final LevelApi levelApi;
    private final LevelBorder levelBorder;

    public LevelUpListener(LevelBorder levelBorder) {
        levelApi = levelBorder.getLevelApi();
        this.levelBorder = levelBorder;
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        if (Configs.getConfig().isActive()) {
            if (event.getNewLevel() != levelApi.getSyncLevel()) {
                if (event.getNewLevel() > event.getOldLevel()) {
                    sendSoundEffect(true);
                } else if (event.getNewLevel() < event.getOldLevel()) {
                    sendSoundEffect(false);
                }
                levelApi.setSyncLevel(event.getNewLevel());
                levelApi.updateLevel();
            }
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (Configs.getConfig().isActive()) {
            if (event.getAmount() != levelApi.getSyncExp()) {
                levelApi.setSyncExp(event.getAmount());
                levelApi.updateExp();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
        }
    }

    /**
     * Sends a sound effect to all players online
     * @param increase true -> border gets bigger sound | false -> border gets smaller sound
     */
    private void sendSoundEffect(boolean increase) {
        for  (Player player : Bukkit.getOnlinePlayers()) {
            if (increase) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        }
    }

}
