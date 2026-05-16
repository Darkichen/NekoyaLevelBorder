package at.darkichen.levelBorder.commands;

import at.darkichen.levelBorder.CommandUtils.LevelBorderHandler;
import at.darkichen.levelBorder.LevelBorder;
import at.darkichen.levelBorder.config.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LevelBorderCommand implements CommandExecutor {

    private String prefix = Configs.getConfig().getPrefix();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(prefix + "Usage: /levelborder <enable|disable|keepLevelOnDeath|reset>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                LevelBorderHandler.handleEnable(player, LevelBorder.getInstance());
                break;
            case "disable":
                LevelBorderHandler.handleDisable(player, LevelBorder.getInstance());
                break;
            case "reset":
                player.sendMessage(prefix + "Type: §e/levelborder reset confirm §7to reset");
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("confirm")) {
                        LevelBorderHandler.handleReset(player, LevelBorder.getInstance());
                    }
                }
                break;
            case "keeplevelondeath":
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("enable")) {
                        LevelBorderHandler.handleKeepLevelOnDeathEnable(player, LevelBorder.getInstance());
                    } else if (args[1].equalsIgnoreCase("disable")) {
                        LevelBorderHandler.handleKeepLevelOnDeathDisable(player, LevelBorder.getInstance());
                    } else {
                        player.sendMessage(prefix + "Usage: §e/levelborder keepLevelOnDeath <enable|disable>");
                    }
                } else {
                    player.sendMessage(prefix + "Usage: §e/levelborder keepLevelOnDeath <enable|disable>");
                }
                break;
            default:
                player.sendMessage(prefix + "Usage: /levelborder <enable|disable>");
                break;
        }

        return true;
    }
}
