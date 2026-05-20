package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class PlayerRespawnListener implements Listener {

    private final LevelBorder levelBorder;
    private final Config config;

    public PlayerRespawnListener(LevelBorder levelBorder) {
        this.config = Configs.getConfig();
        this.levelBorder = levelBorder;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (config.isActive()) {
            levelBorder.getBorderApi().sendVisualBorder(event.getPlayer());
            event.setRespawnLocation(levelBorder.getBorderApi().getBorderCenter(Objects.requireNonNull(Bukkit.getWorld("world"))));
        }
    }

}
