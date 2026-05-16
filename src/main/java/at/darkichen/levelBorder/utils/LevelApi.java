package at.darkichen.levelBorder.utils;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LevelApi {

    private final LevelBorder levelBorder;
    private final Config config;

    public LevelApi(LevelBorder levelBorder) {
        this.levelBorder = levelBorder;
        config = Configs.getConfig();
    }

    public int getSyncLevel() {
        return config.getSyncLevel();
    }

    public float getSyncExp() {
        return config.getSyncExp();
    }

    public void setSyncLevel(int level) {
        config.setSyncLevel(level);
    }

    public void setSyncExp(float exp) {
        config.setSyncExp(exp);
    }

    public void setPlayerLevel(Player player) {
        player.setLevel(getSyncLevel());
        player.setExp(getSyncExp());
    }

    public void updateLevel() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(getSyncLevel());
        }
        levelBorder.getBorderApi().updateBorders();
    }

    public void updateExp() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setExp(getSyncExp());
        }
    }

    public void reset() {
        config.setSyncLevel(0);
        config.setSyncExp(0f);
        updateLevel();
        updateExp();
    }

}
