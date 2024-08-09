package me.waterarchery.littournaments;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.littournaments.database.Database;
import me.waterarchery.littournaments.handlers.CommandHandler;
import me.waterarchery.littournaments.handlers.LoadHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class LitTournaments extends JavaPlugin {

    private static LitLibs litLibs;
    private static Database database;

    @Override
    public void onEnable() {
        litLibs = LitLibs.of(this);
        Logger logger = litLibs.getLogger();
        LoadHandler loadHandler = LoadHandler.getInstance();
        loadHandler.load();
        logger.log("LitTournaments enabled &bv" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        Logger logger = litLibs.getLogger();

        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.unRegisterCommands();

        database.shutdown();

        logger.log("Good bye :(");
    }

    public static LitTournaments getInstance() { return getPlugin(LitTournaments.class); }

    public static LitLibs getLitLibs() { return litLibs; }

    public static Database getDatabase() { return database; }

    public static void setDatabase(Database newDatabase) { database = newDatabase; }

}