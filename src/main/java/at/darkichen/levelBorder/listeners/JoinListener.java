package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import at.darkichen.levelBorder.utils.BorderApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final LevelBorder levelBorder;
    private final Config config;

    public JoinListener(LevelBorder levelBorder) {
        this.levelBorder = levelBorder;
        this.config = Configs.getConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.isActive()) {
            levelBorder.getLevelApi().setPlayerLevel(player);
            levelBorder.getBorderApi().sendVisualBorder(player);
            levelBorder.getBorderApi().teleportPlayer(player);
        }
        event.joinMessage(Component.text(" » ").color(NamedTextColor.GRAY).append(Component.text(player.getName()).color(NamedTextColor.GREEN)));
    }
}