package at.darkichen.levelBorder;

import at.darkichen.levelBorder.commands.LevelBorderCommand;
import at.darkichen.levelBorder.config.Configs;
import at.darkichen.levelBorder.listeners.*;
import at.darkichen.levelBorder.utils.BorderApi;
import at.darkichen.levelBorder.utils.LevelApi;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LevelBorder extends JavaPlugin {

    @Getter
    private static LevelBorder instance;
    @Getter
    private BorderApi borderApi;
    @Getter
    private LevelApi levelApi;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        //Load Configs
        Configs.loadConfigs(instance);
        //Load Borders
        borderApi = new BorderApi(instance);
        //Initialize LevelApi
        levelApi = new LevelApi(instance);
        //Register commands
        registerCommands();
        //Register listeners
        registerListeners();
        //Update borders
        borderApi.updateBorders();
        instance.getLogger().info("Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Configs.saveConfigs(instance);
        instance.getLogger().info("Plugin Disabled!");
        instance = null;
    }

    private void registerCommands() {
        Objects.requireNonNull(instance.getCommand("levelborder")).setExecutor(new LevelBorderCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PortalRedirectListener(instance), instance);
        pm.registerEvents(new JoinListener(instance), instance);
        pm.registerEvents(new LevelUpListener(instance), instance);
        pm.registerEvents(new PlayerDeathListener(instance), instance);
        pm.registerEvents(new QuitListener(), instance);
    }
}
