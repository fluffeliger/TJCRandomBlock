package de.fluffy.tjc.random_block.game.player.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerSpecificScoreboard {

    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private Component title = MiniMessage.miniMessage().deserialize("Scoreboard");
    private final ArrayList<String> content = new ArrayList<>();
    private final ArrayList<String> oldContent = new ArrayList<>();

    public PlayerSpecificScoreboard(@NotNull Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("playersb", Criteria.DUMMY, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void setTitle(@NotNull Component component) {
        this.title = component;
    }

    public void setContent(@NotNull ArrayList<String> buffer) {
        content.clear();
        content.addAll(buffer);
    }

    public void updateContent() {
        if (oldContent.size() > content.size())
            for (int i = oldContent.size()-content.size(); i < oldContent.size(); i++)
                objective.getScore(oldContent.get(oldContent.size()-1-i)).resetScore();

        for (int i = 0; i < content.size(); i++) {
            String newBuffer = content.get(content.size()-1-i);
            String oldBuffer = getOld(content.size()-1-i);

            if (oldBuffer != null && !oldBuffer.equals(newBuffer)) objective.getScore(oldBuffer).resetScore();
            objective.getScore(newBuffer).setScore(i);
        }

        oldContent.clear();
        oldContent.addAll(content);
    }

    private String getOld(int id) {
        if (id >= this.oldContent.size()) return null;
        return this.oldContent.get(id);
    }

    public void updateTitle() {
        objective.displayName(title);
    }
}
