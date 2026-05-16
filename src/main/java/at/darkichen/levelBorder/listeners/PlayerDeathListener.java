package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Config config;
    private final LevelBorder levelBorder;

    public PlayerDeathListener(LevelBorder levelBorder) {
        config = Configs.getConfig();
        this.levelBorder = levelBorder;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (config.isKeepLevelOnDeath() && config.isActive()) {
            e.setDroppedExp(0);
            e.setKeepLevel(true);
        }
    }


}
