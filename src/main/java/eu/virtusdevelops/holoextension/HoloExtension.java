package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.commands.MainCommand;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.holoextension.storage.storages.MySQLStorage;
import eu.virtusdevelops.holoextension.storage.storages.SQLiteStorage;
import eu.virtusdevelops.holoextension.utils.Metrics;
import eu.virtusdevelops.virtuscore.gui.GuiListener;
import eu.virtusdevelops.virtuscore.gui.Handler;
import org.bukkit.plugin.java.JavaPlugin;


public class HoloExtension extends JavaPlugin {

    //private ModuleManager moduleManager;
    private LeaderBoardManager leaderBoardManager;
//    private Cache cache;

    @Override
    public void onEnable() {
        // The enable stuff.
        saveDefaultConfig();

        // Cache
//        cache = new Cache(this);
//        cache.setup();
        // init storage stuff
        DataStorage storage = switch (getConfig().getString("system.storage_type").toLowerCase()) {
            case "mysql" -> new MySQLStorage(this,
                    getConfig().getString("system.username"),
                    getConfig().getString("system.password"),
                    getConfig().getString("system.database"),
                    getConfig().getString("system.server"),
                    getConfig().getString("system.port"),
                    getConfig().getBoolean("system.useSSL"));
            default -> new SQLiteStorage(this);
        };

        // Module manager
        leaderBoardManager = new LeaderBoardManager(this, storage);


        //moduleManager = new ModuleManager(this, storage);
        //moduleManager.reload();

        // Load commands
        //getCommand("he").setExecutor(new TemporaryCommand(this, moduleManager));
        getCommand("he").setExecutor(new MainCommand(leaderBoardManager));

        new GuiListener(new Handler(this), this);

        // metrics
        Metrics metrics = new Metrics(this, 5834);
        //metrics.addCustomChart(new Metrics.SimplePie("amount_of_modules", () -> ""+moduleManager.getModuleList().size()));
    }


    // Reload
    public void reload(){
        this.reloadConfig();
//        cache.reload();
        //moduleManager.reload();
    }



}
