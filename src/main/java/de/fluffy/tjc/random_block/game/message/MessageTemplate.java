package de.fluffy.tjc.random_block.game.message;

import de.fluffy.tjc.random_block.game.message.ui.SoundBundle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public enum MessageTemplate {

    PLAYER_REWARD_COINS("<gray>Du hast <gold>%s</gold> Coins bekommen", new SoundBundle(
            Sound.ENTITY_PLAYER_LEVELUP, 1, 1
    )),
    PLAYER_DEATH("%c <red>ist gestorben", new SoundBundle(
            Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1.5F
    )),
    PLAYER_JOIN("%c <gray>ist beigetreten", null),
    PLAYER_QUIT("%c <gray>ist gegangen", null),
    PLAYER_DEATH_JOIN("%c <dark_gray>ist beigetreten", null),
    PLAYER_DEATH_QUIT("%c <dark_gray>ist gegangen", null),
    PLAYER_IS_WINNER("%c <aqua>hat gewonnen", null),
    COUNTDOWN_STARTING_IN("<gray>Startet in <aqua>%d</aqua> Sekunden...", new SoundBundle(
            Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1
    )),
    COUNTDOWN_STARTING_IN_ONE("<gray>Startet in <aqua>einer</aqua> Sekunde...", new SoundBundle(
            Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1
    )),
    COUNTDOWN_STARTING_NOW("<gray>Spiel Startet...", new SoundBundle(
            Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1
    )),
    LOBBY_WAITING_FOR("<gray>Warte auf <aqua>%d</aqua> Spieler", new SoundBundle(
            Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1
    )),
    LOBBY_WAITING_FOR_ONE("<gray>Warte auf <aqua>einen</aqua> Spieler", new SoundBundle(
            Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1
    ));

    private final String template;
    @Nullable private final SoundBundle sound;

    MessageTemplate(@NotNull String template, @Nullable SoundBundle sound) {
        this.template = template;
        this.sound = sound;
    }

    public void broadcast(@NotNull Object... replacements) {
        Bukkit.broadcast(getComponent(replacements));
        if (this.sound == null) return;
        this.sound.broadcast();
    }

    public void send(@NotNull Player player, boolean silent, @NotNull Object... replacements) {
        player.sendMessage(getComponent(replacements));
        if (silent || this.sound == null) return;
        this.sound.broadcast();
    }

    public Component getComponent(@NotNull Object... replacements) {
        ArrayList<Object> objects = new ArrayList<>();
        ArrayList<Component> components = new ArrayList<>();
        for (Object replacement : replacements) {
            if (replacement instanceof Component component) components.add(component);
            else objects.add(replacement);
        }
        StringBuilder newString = new StringBuilder();
        boolean inPercent = false;
        for (Character character : this.template.toCharArray()) {
            if (character.equals('%')) {
                inPercent = true;
                continue;
            }
            if (inPercent) {
                inPercent = false;
                if (character.equals('c')) newString.append("<REPLACE>");
                else newString.append('%').append(character);
                continue;
            }
            newString.append(character);
        }
        String finalString = newString.toString().formatted(objects.toArray());
        MiniMessage msg = MiniMessage.miniMessage();
        if (!finalString.contains("<REPLACE>")) return msg.deserialize(finalString);
        String[] elements = finalString.split("<REPLACE>");
        Iterator<Component> componentIterator = components.iterator();
        Component current = msg.deserialize(elements[0]);
        for (int i = 1; i < elements.length; i++)
            current = current.append(componentIterator.next()).append(msg.deserialize(elements[i]));
        return current;
    }

}
