package at.darkichen.levelBorder.utils;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

    public int getSyncAmount() {
        return config.getSyncAmount();
    }

    public void setSyncLevel(int level) {
        config.setSyncLevel(level);
        Configs.saveConfigs(levelBorder);
    }

    public void setSyncAmount(int exp) {
        config.setSyncAmount(exp);
        Configs.saveConfigs(levelBorder);
    }

    public void setPlayerLevel(Player player) {
        player.setLevel(getSyncLevel());
        player.setExperienceLevelAndProgress(getSyncAmount());
    }

    public void updateLevel() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(getSyncLevel());
        }
        levelBorder.getBorderApi().updateBorders();
    }

    public void updateAmount() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setExperienceLevelAndProgress(getSyncAmount());
        }
    }

    public void reset() {
        config.setSyncLevel(0);
        config.setSyncAmount(0);
        Configs.saveConfigs(levelBorder);
        updateLevel();
        updateAmount();
    }

    public int getXpNeededForNextLevel(int level) {
        if (level < 16) return 2 * level + 7;
        else if (level < 31) return 5 * level - 38;
        else return 9 * level - 158;
    }

}
