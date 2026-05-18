package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Configs;
import at.darkichen.levelBorder.utils.LevelApi;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            final int newLevel = event.getPlayer().getLevel();
            if (newLevel != levelApi.getSyncLevel()) {
                if (newLevel > levelApi.getSyncLevel()) {
                    sendSoundEffect(true);
                } else if (newLevel < levelApi.getSyncLevel()) {
                    sendSoundEffect(false);
                    int cost = 0;
                    for (int level = newLevel; level < levelApi.getSyncLevel(); level++) {
                        cost += levelApi.getXpNeededForNextLevel(level);
                    }
                    levelApi.setSyncAmount(levelApi.getSyncAmount() - cost);
                }
                levelApi.setSyncLevel(newLevel);
                levelApi.updateLevel();
            }
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (Configs.getConfig().isActive()) {
            levelApi.setSyncAmount(levelApi.getSyncAmount() + event.getAmount());
            levelApi.updateAmount();
            event.setAmount(0);
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
