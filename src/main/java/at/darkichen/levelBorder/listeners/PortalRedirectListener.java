package at.darkichen.levelBorder.listeners;

import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Map;

public class PortalRedirectListener implements Listener {

    private final LevelBorder levelBorder;

    public PortalRedirectListener(LevelBorder plugin) {
        this.levelBorder = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (!Configs.getConfig().isActive()) {
            return;
        }
        TeleportCause  cause = event.getCause();

        if (Configs.getConfig() == null) return;
        Map<String, Location> borderMap = Configs.getConfig().getBorder();
        if (borderMap == null) return;

        //if portal is nether portal
        if (cause == TeleportCause.NETHER_PORTAL) {
            World fromWorld = event.getFrom().getWorld();

            //if destination is nether
            if (fromWorld != null && fromWorld.getEnvironment() == World.Environment.NORMAL) {
                //if nether location wasn't saved before save it now
                if (!borderMap.containsKey("world_nether")) {
                    Location initialNetherLanding = event.getTo();
                    borderMap.put("world_nether", initialNetherLanding);
                    Configs.saveConfigs(levelBorder);
                    levelBorder.getBorderApi().updateBorders();
                    levelBorder.getLogger().info("First time nether entrance detected and location saved!");
                }
                //else set destination to saved nether location
                else {
                    setDestination(event, borderMap.get("world_nether"));
                }
            }
            //if destination is overworld
            else {
                setDestination(event, borderMap.get("world"));
            }
        }
        //if portal is end portal
        else if (cause == TeleportCause.END_PORTAL) {
            World fromWorld = event.getFrom().getWorld();

            //if destination is end
            if (fromWorld != null && fromWorld.getEnvironment() == World.Environment.NORMAL) {
                //if end location wasn't saved before save it now
                if (!borderMap.containsKey("world_the_end")) {
                    Location initialEndLanding = event.getTo();
                    borderMap.put("world_the_end", initialEndLanding);
                    Configs.saveConfigs(levelBorder);
                    levelBorder.getBorderApi().updateBorders();
                    levelBorder.getLogger().info("First time end entrance detected and location saved!");
                }
                //else set destination to saved end location
                else {
                    setDestination(event, borderMap.get("world_the_end"));
                }
            }
            //if destination is overworld
            else {
                setDestination(event, borderMap.get("world"));
            }
        }
        //if destination is overworld
        else {
            setDestination(event, borderMap.get("world"));
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if (Configs.getConfig().isActive()) {
            levelBorder.getBorderApi().sendVisualBorder(event.getPlayer());

        }
    }

    private void setDestination(PlayerPortalEvent event, Location location) {
        if (location != null) {
            event.setTo(location);
        }
    }

}
