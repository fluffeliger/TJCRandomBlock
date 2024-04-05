package de.fluffy.tjc.random_block;

import de.fluffy.tjc.random_block.commands.WorldCommand;
import de.fluffy.tjc.random_block.data.DataInterface;
import de.fluffy.tjc.random_block.event.ListenerHandler;
import de.fluffy.tjc.random_block.game.GameHandler;
import de.fluffy.tjc.random_block.game.player.GamePlayerManager;
import de.fluffy.tjc.random_block.generation.chunk.VoidChunkGenerator;
import de.fluffy.tjc.random_block.generation.world.CustomWorld;
import de.fluffy.tjc.random_block.generation.world.WorldManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class RandomBlockPlugin extends JavaPlugin {

    private static RandomBlockPlugin pluginInstance;
    private final DataInterface dataInterface = new DataInterface();
    private final WorldManager worldManager = new WorldManager();
    private GamePlayerManager gamePlayerManager;
    private GameHandler gameHandler;
    private File voidOriginFolder;

    @Override
    public void onEnable() {
        pluginInstance = this;

        this.dataInterface.registerDirectory(this.getDataFolder());
        this.dataInterface.registerDirectory(
                Path.of(this.getDataFolder().getPath(), "users").toFile(),
                "users"
        );
        this.dataInterface.registerDirectory(
                Path.of(this.getDataFolder().getPath(),"worlds").toFile(),
                "worlds"
        );

        voidOriginFolder = Path.of(
                Objects.requireNonNull(this.dataInterface.getFile("worlds")).getPath(),
                "void_origin"
        ).toFile();

        if (!voidOriginFolder.exists()) {
            CustomWorld void_origin = this.worldManager.register(
                    "void_origin",
                    new VoidChunkGenerator(),
                    null
            );

            void_origin.loadAndGenerate();
            void_origin.loadStartingArea();
            void_origin.unload(true);
            void_origin.copyTo(voidOriginFolder);

            DataInterface.deleteDir(new File("./void_origin"));
        }

        prepareVoidWorld();

        gameHandler = new GameHandler();
        gamePlayerManager = new GamePlayerManager();

        new WorldCommand(pluginInstance, "world");
        new ListenerHandler().registerAll(pluginInstance);

        Bukkit.getOnlinePlayers().forEach(player ->
                player.kick(MiniMessage.miniMessage().deserialize("<gray>Du wurdest gekickt\n\nGrund: <aqua>Plugin Startup"))
        );

        gameHandler.update();
    }

    @Override
    public void onDisable() {
        deleteVoidWorld();
        pluginInstance = null;
    }

    public void resetVoidWorld() {
        deleteVoidWorld();
        prepareVoidWorld();
    }

    private void deleteVoidWorld() {
        CustomWorld void_copy = this.worldManager.getWorld("void_copy");
        assert void_copy != null;
        void_copy.unload(false);
        void_copy.deleteWorldFolder();
    }

    private void prepareVoidWorld() {
        DataInterface.copyDir(voidOriginFolder, new File("./void_copy"));
        CustomWorld void_copy = this.worldManager.register(
                "void_copy",
                new VoidChunkGenerator(),
                null
        );
        void_copy.loadAndGenerate();
        void_copy.loadStartingArea();
    }

    public File getRelativeFile(Path path) {
        return Path.of(getDataFolder().getPath(), path.toFile().getPath()).toFile();
    }

    public static RandomBlockPlugin getInstance() {
        return pluginInstance;
    }
}