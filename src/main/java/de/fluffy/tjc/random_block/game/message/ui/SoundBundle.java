package de.fluffy.tjc.random_block.game.message.ui;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public record SoundBundle(Sound sound, float volume, float pitch) { // TODO FIND BETTER NAME FOR SOUND BUNDLE

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound(), volume(), pitch());
        });
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound(), volume(), pitch());
    }

}