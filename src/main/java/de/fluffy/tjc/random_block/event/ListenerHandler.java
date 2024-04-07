package de.fluffy.tjc.random_block.event;

import de.fluffy.tjc.random_block.event.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerHandler {

    public void registerAll(JavaPlugin instance) {
        new PlayerJoinListener(instance);
        new PlayerQuitListener(instance);

        new AsyncChatListener(instance);

        new PlayerMoveListener(instance);
        new EntityDamageListener(instance);
        new PlayerDeathListener(instance);

        new InventoryClickListener(instance);
        new PlayerDropItemListener(instance);
        new PlayerInteractListener(instance);
    }

}
