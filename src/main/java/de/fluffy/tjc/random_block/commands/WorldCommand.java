package de.fluffy.tjc.random_block.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorldCommand implements CommandExecutor, TabCompleter {

    private final JavaPlugin pluginInstance;

    public WorldCommand(JavaPlugin pluginInstance, String command) {
        this.pluginInstance = pluginInstance;
        PluginCommand pluginCommand = pluginInstance.getCommand(command);
        assert pluginCommand != null;
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Nur Spieler können diesen Befehl ausführen"));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Nutze: /world <dark_red><name>"));
            return false;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Welt <dark_red>" + worldName + " <red>wurde nicht gefunden"));
            return false;
        }

        if (player.getWorld().equals(world)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du bist bereits auf der Welt <dark_red>" + worldName));
            return false;
        }

        player.teleport(world.getSpawnLocation());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            Bukkit.getWorlds().forEach(world -> tab.add(world.getName()));
        }

        return tab;
    }
}
