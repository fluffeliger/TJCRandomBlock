package de.fluffy.tjc.random_block.event.listeners;

import de.fluffy.tjc.random_block.RandomBlockPlugin;
import de.fluffy.tjc.random_block.data.DataInterface;
import de.fluffy.tjc.random_block.data.config.ConfigFile;
import de.fluffy.tjc.random_block.game.GameHandler;
import de.fluffy.tjc.random_block.game.GameState;
import de.fluffy.tjc.random_block.game.message.MessageTemplate;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import de.fluffy.tjc.random_block.game.player.GameStateType;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener(@NotNull JavaPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEvent(@NotNull PlayerJoinEvent event) {
        GameState gameState = GameHandler.getInstance().getGameState();
        event.joinMessage(null);
        if (gameState.getType() == GameStateType.OUT_GAME) MessageTemplate.PLAYER_JOIN.broadcast(
                event.getPlayer().displayName().color(NamedTextColor.GREEN)
        );
        else MessageTemplate.PLAYER_DEATH_JOIN.broadcast(
                event.getPlayer().displayName().color(NamedTextColor.DARK_GRAY)
        );
        GamePlayerManager.getInstance().register(event.getPlayer());
    }

}
