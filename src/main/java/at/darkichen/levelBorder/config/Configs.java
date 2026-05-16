package at.darkichen.levelBorder.config;

import at.darkichen.levelBorder.LevelBorder;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configs {

    @Getter
    private static Config config;
    private static Path configFile;

    /**
     * Loads the Config File
     * @param levelBorder Main plugin instance
     */
    public static void loadConfigs(LevelBorder levelBorder) {
        configFile = Paths.get(levelBorder.getDataPath() + "/config.toml");

        if (!levelBorder.getDataFolder().exists()) {
            levelBorder.getDataFolder().mkdir();
        }

        if (!configFile.toFile().exists()) {
            try (InputStream in = LevelBorder.class.getResourceAsStream("/config.toml")) {
                assert in != null;
                Files.copy(in, configFile);
            } catch (Exception e) {
                levelBorder.getLogger().warning("Failed to load config file!");
                e.printStackTrace();
            }
        }
        Toml toml = new Toml().read(configFile.toFile());
        config = new Config();
        config.setActive(toml.getBoolean("isActive", false));
        config.setPrefix(toml.getString("prefix", ""));
        config.setKeepLevelOnDeath(toml.getBoolean("keepLevelOnDeath", false));
        config.setExpandOnLvlUp(Math.toIntExact(toml.getLong("expandOnLvlUp", 2L)));
        config.setDefaultRadius(Math.toIntExact(toml.getLong("defaultRadius", 2L)));
        config.setSyncLevel(Math.toIntExact(toml.getLong("syncLevel", 0L)));
        config.setSyncExp(toml.getDouble("syncExp", 0.0).floatValue());

        List<List<Integer>> stageList = new ArrayList<>();
        List<Object> rawStage = toml.getList("stage");
        if (rawStage != null) {
            for (Object raw : rawStage) {
                if (raw instanceof List) {
                    List<?> sub = (List<?>) raw;
                    List<Integer> ints = new ArrayList<>();
                    for (Object val : sub) {
                        if (val instanceof Number) {
                            ints.add(((Number) val).intValue());
                        }
                    }
                    stageList.add(ints);
                }
            }
        }
        config.setStage(stageList);

        Toml borderSection = toml.getTable("border");
        Map<String, Location> borderMap = new HashMap<String, Location>();

        if (borderSection != null) {
            for (Map.Entry<String, Object> entry : borderSection.toMap().entrySet()) {
                String borderKey = entry.getKey();
                Toml subTable = borderSection.getTable(borderKey);

                Location loc = new Location(Bukkit.getWorld(subTable.getString("world")), subTable.getDouble("x"), subTable.getDouble("y"), subTable.getDouble("z"));
                borderMap.put(borderKey, loc);
            }
        }
        config.setBorder(borderMap);
    }

    /**
     * Resets the Config File to default
     * @param levelBorder Main plugin instance
     */
    public static void resetConfigs(LevelBorder levelBorder) {
        try {
            Files.deleteIfExists(configFile);
            try (InputStream in = LevelBorder.class.getResourceAsStream("/config.toml")) {
                assert in != null;
                Files.copy(in, configFile);
            }
            loadConfigs(levelBorder);
        } catch (Exception e) {
            levelBorder.getLogger().warning("Failed to reset config file!");
            e.printStackTrace();
        }
    }

    /**
     * Saves the Config File
     * @param levelBorder Main plugin instance
     */
    public static void saveConfigs(LevelBorder levelBorder) {
        if (config == null || configFile == null) return;

        try {
            Map<String, Object> rawData = new HashMap<>();
            rawData.put("isActive", config.isActive());
            rawData.put("prefix", config.getPrefix());
            rawData.put("keepLevelOnDeath", config.isKeepLevelOnDeath());
            rawData.put("expandOnLvlUp", config.getExpandOnLvlUp());
            rawData.put("defaultRadius", config.getDefaultRadius());
            rawData.put("stage", config.getStage());
            rawData.put("syncLevel", config.getSyncLevel());
            rawData.put("syncExp", config.getSyncExp());

            Map<String, Object> borderTable = new HashMap<>();

            if (config.getBorder() != null) {
                for (Map.Entry<String, Location> entry : config.getBorder().entrySet()) {
                    Location loc = entry.getValue();
                    if (loc == null || loc.getWorld() == null) continue;

                    Map<String, Object> locData = new HashMap<>();
                    locData.put("world", loc.getWorld().getName());
                    locData.put("x", loc.getX());
                    locData.put("y", loc.getY());
                    locData.put("z", loc.getZ());

                    borderTable.put(entry.getKey().toLowerCase(), locData);
                }
            }

            rawData.put("border", borderTable);

            // 3. Write out the clean structural tree to your config.toml file
            new TomlWriter().write(rawData, configFile.toFile());
        } catch (Exception e) {
            levelBorder.getLogger().warning("Failed to save config file!");
            e.printStackTrace();
        }
    }

}
