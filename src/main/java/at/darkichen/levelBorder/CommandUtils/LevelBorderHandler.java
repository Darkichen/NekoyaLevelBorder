package at.darkichen.levelBorder.CommandUtils;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.entity.Player;

public class LevelBorderHandler {

    private static Config config = Configs.getConfig();

    /**
     * Enables the LevelBorder
     * @param player (Player) Command sender
     */
    public static void handleEnable(Player player, LevelBorder levelBorder) {
        if (!config.isActive()) {
            config.setActive(true);
            Configs.saveConfigs(LevelBorder.getInstance());
            levelBorder.getBorderApi().initialOverworldLoc();
            levelBorder.getBorderApi().updateBorders();
            player.sendMessage(config.getPrefix() + "LevelBorder §aenabled.");
        } else  {
            player.sendMessage(config.getPrefix() + "LevelBorder already §aenabled.");
        }
    }

    /**
     * Disables the LevelBorder
     * @param player (Player) Command sender
     */
    public static void handleDisable(Player player, LevelBorder levelBorder) {
        if (config.isActive()) {
            config.setActive(false);
            Configs.saveConfigs(LevelBorder.getInstance());
            levelBorder.getBorderApi().disableBorders();
            player.sendMessage(config.getPrefix() + "LevelBorder §cdisabled");
        } else {
            player.sendMessage(config.getPrefix() + "LevelBorder already §cdisabled");
        }
    }

    public static void handleReset(Player player, LevelBorder levelBorder) {
        Configs.resetConfigs(levelBorder);
        levelBorder.getBorderApi().disableBorders();
        levelBorder.getLevelApi().reset();
        player.sendMessage(config.getPrefix() + "Reset §ecompleted");
    }

    public static void handleKeepLevelOnDeathEnable(Player player, LevelBorder levelBorder) {
        config.setKeepLevelOnDeath(true);
        Configs.saveConfigs(LevelBorder.getInstance());
        player.sendMessage(config.getPrefix() + "Keep Level On Death §aEnabled");
    }

    public static void handleKeepLevelOnDeathDisable(Player player, LevelBorder levelBorder) {
        config.setKeepLevelOnDeath(false);
        Configs.saveConfigs(LevelBorder.getInstance());
        player.sendMessage(config.getPrefix() + "Keep Level On Death §cDisabled");
    }

}
