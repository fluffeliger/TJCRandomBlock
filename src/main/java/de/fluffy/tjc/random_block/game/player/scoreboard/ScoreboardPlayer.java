package de.fluffy.tjc.random_block.game.player.scoreboard;

import de.fluffy.tjc.random_block.RandomBlockPlugin;
import de.fluffy.tjc.random_block.data.config.cinterface.interfaces.ConfigPlayerInterface;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ScoreboardPlayer extends PlayerSpecificScoreboard {

    private final ConfigPlayerInterface configPlayerInterface;

    public ScoreboardPlayer(Player player, ConfigPlayerInterface configPlayerInterface) {
        super(player);
        this.configPlayerInterface = configPlayerInterface;
        final double[] millisF = { -1 };
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                setTitle(MiniMessage.miniMessage().deserialize(
                        " <b><gradient:dark_purple:blue:" + millisF[0] + ">Random Block Challenge ")
                );
                updateTitle();

                millisF[0] += .1F;
                if (millisF[0] > 1) millisF[0] = -1;
            }
        }.runTaskTimer(RandomBlockPlugin.getInstance(), 0, 1);
        configPlayerInterface.setUpdateLambda((ignored) -> updateScoreboard());
        updateScoreboard();
    }

    public void updateScoreboard() {
        ArrayList<String> buffer = new ArrayList<>();
        buffer.add("     ");
        buffer.add(" §7§lCoins");
        buffer.add(" §8» §6" + configPlayerInterface.getCoins() + " ");
        buffer.add("    ");
        buffer.add(" §7§lWins");
        buffer.add(" §8» §a" + configPlayerInterface.getWins() + "  ");
        buffer.add("   ");
        buffer.add(" §7§lLosses");
        buffer.add(" §8» §c" + configPlayerInterface.getLosses() + "   ");
        buffer.add("  ");
        buffer.add(" §7§lKit");
        /*if (hasKit()) buffer.add(" §8» " + selectedKit.getPlainTitle() + "    ");
        else*/ buffer.add(" §8» §cKein Kit Ausgewählt    ");
        buffer.add(" ");
        this.setContent(buffer);
        this.updateContent();
    }

}
