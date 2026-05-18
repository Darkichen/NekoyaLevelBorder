package at.darkichen.levelBorder.utils;


import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Config;
import at.darkichen.levelBorder.config.Configs;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.*;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendVisualBorder(player);
        }
        teleportBackIntoBorder();
    }

    public void sendVisualBorder(Player player) {
        Bukkit.getScheduler().runTaskLater(levelBorder, new Runnable() {
            @Override
            public void run() {
                int level = config.getSyncLevel();
                World world = player.getWorld();
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.INITIALIZE_BORDER);

                packet.getDoubles().write(0, config.getBorder().get(world.getName()).getX()); //center X
                packet.getDoubles().write(1, config.getBorder().get(world.getName()).getZ()); //center Z

                packet.getDoubles().write(2, (double) (config.getDefaultRadius() + (level > 0 ? (level * (config.getExpandOnLvlUp() * getStageMult(level))) : 0))); //old diameter
                packet.getDoubles().write(3, (double) (config.getDefaultRadius() + (level > 0 ? (level * (config.getExpandOnLvlUp() * getStageMult(level))) : 0))); //new diameter
                packet.getLongs().write(0, 0L);
                packet.getIntegers().write(0, 29999984);

                try {
                    protocolManager.sendServerPacket(player, packet);
                } catch (Exception e) {
                    levelBorder.getLogger().severe("Failed to send modern INITIALIZE_BORDER packet via ProtocolLib!");
                    e.printStackTrace();
                }
            }
        }, 5);
    }

    public void teleportBackIntoBorder() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            teleportPlayer(player);
        }
    }

    public void teleportPlayer(Player player) {
        if (!levelBorder.getBorderApi().checkIfInsideBorder(player)) {
            player.teleport(getBorderCenter(player));
        }
    }

    public Location getBorderCenter(Player player) {
        Location loc = config.getBorder().get(player.getWorld().getName());
        int highestY = loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ());
        loc.setY(highestY + 2);
        return loc;
    }

    public boolean checkIfInsideBorder(Player player) {
        Location loc = player.getLocation();
        int level = config.getSyncLevel();

        double radius = (double) (config.getDefaultRadius() + (level > 0 ? (level * (config.getExpandOnLvlUp() * getStageMult(level))) : 0)) / 2;
        double centerX = config.getBorder().get(player.getWorld().getName()).getX();
        double centerZ = config.getBorder().get(player.getWorld().getName()).getZ();

        double minX = centerX - radius;
        double maxX = centerX + radius;
        double minZ = centerZ - radius;
        double maxZ = centerZ + radius;

        return (loc.getX() >= minX && loc.getX() <= maxX) && (loc.getZ() >= minZ && loc.getZ() <= maxZ);
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
        for  (Player player : Bukkit.getOnlinePlayers()) {
            // Create the initialization packet that overrides the existing client border
            PacketContainer packet = ProtocolLibrary.getProtocolManager()
                    .createPacket(PacketType.Play.Server.INITIALIZE_BORDER);

            // Write the default vanilla values to effectively "delete" the visual border
            packet.getDoubles().write(0, 0.0);       // Center X (0)
            packet.getDoubles().write(1, 0.0);       // Center Z (0)
            packet.getDoubles().write(2, 6.0E7);     // Old Size (Default 60,000,000)
            packet.getDoubles().write(3, 6.0E7);     // New Size (Default 60,000,000)
            packet.getIntegers().write(0, 29999984); // Portal Teleport Boundary

            // Force send it directly to the player's client
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }
    }

}
