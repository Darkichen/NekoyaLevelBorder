package at.darkichen.levelBorder.utils;


import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class BorderApi {

    private final LevelBorder levelBorder;
    private final Config config;

    public BorderApi(LevelBorder levelBorder) {
        this.levelBorder = levelBorder;
        config = Configs.getConfig();
        if (config.isActive()) {
            initialOverworldLoc();
            updateBorders();
        }
    }

    public void initialOverworldLoc() {
        Map<String, Location> borderMap = Configs.getConfig().getBorder();
        if (borderMap != null) {
            if (!borderMap.containsKey("world")) {
                World world = Bukkit.getWorld("world");
                if (world != null) {
                    Location loc = world.getSpawnLocation();
                    borderMap.put("world", loc);
                    levelBorder.getLogger().info("Initialized world spawn at: " + loc);
                } else {
                    levelBorder.getLogger().warning("Could not find world! No spawn location set!");
                }
            }
        } else {
            levelBorder.getLogger().warning("Config not correctly loaded!");
        }
    }

    public void updateBorders() {
        int level = config.getSyncLevel();
        for (Map.Entry<String, Location> entry : config.getBorder().entrySet()) {
            World world = Bukkit.getWorld(entry.getKey());
            if (world != null) {
                WorldBorder worldBorder = world.getWorldBorder();
                worldBorder.setCenter(entry.getValue());
                worldBorder.setSize((config.getDefaultRadius() + (level > 0 ? (level * (config.getExpandOnLvlUp() * getStageMult(level))) : 0)));
            }
        }
        teleportBackIntoBorder();
    }

    public void teleportBackIntoBorder() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (!world.getWorldBorder().isInside(player.getLocation())) {
                player.teleport(config.getBorder().get(world.getName()));
            }
        }
    }

    public int getStageMult(int level) {
        int mult = 1;

        for (List<Integer> stage : config.getStage()) {
            if (stage.get(0) <= level) {
                mult = stage.get(1);
            } else {
                break;
            }
        }

        return mult;
    }

    public void disableBorders() {
        for (Map.Entry<String, Location> entry : config.getBorder().entrySet()) {
            World world = Bukkit.getWorld(entry.getKey());
            if (world != null) {
                world.getWorldBorder().reset();
            }
        }
    }

}
