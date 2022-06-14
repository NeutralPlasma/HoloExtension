package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.commands.InfoCommand;
import eu.virtusdevelops.holoextension.commands.MenuCommand;
import eu.virtusdevelops.holoextension.commands.ReloadCommand;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.holoextension.protocollib.ProtocolModule;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.holoextension.storage.storages.MySQLStorage;
import eu.virtusdevelops.holoextension.storage.storages.SQLiteStorage;
import eu.virtusdevelops.holoextension.utils.Metrics;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.command.CommandManager;
import eu.virtusdevelops.virtuscore.command.MainCommand;
import eu.virtusdevelops.virtuscore.gui.GuiListener;
import eu.virtusdevelops.virtuscore.gui.Handler;
import org.bukkit.plugin.java.JavaPlugin;



public class HoloExtension extends JavaPlugin {

    //private ModuleManager moduleManager;
    private LeaderBoardManager leaderBoardManager;
    private CommandManager commandManager;
    private ProtocolModule protocolModule;
//    private Cache cache;

    @Override
    public void onEnable() {
        // The enable stuff.
        saveDefaultConfig();


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


        // commands
        commandManager = new CommandManager(this);
        getCommand("he").setExecutor(commandManager);
        loadCommands();

        // GUI
        new GuiListener(new Handler(this), this);


        // Protocollib
        if(VirtusCore.plugins().isPluginEnabled("ProtocolLib")){
            if(this.getConfig().getBoolean("protocollib")){
                protocolModule = new ProtocolModule(this);
                protocolModule.onEnable();
            }
        }

        // metrics
        Metrics metrics = new Metrics(this, 5834);
        metrics.addCustomChart(new Metrics.SimplePie("amount_of_modules", () -> "" + leaderBoardManager.getModules().size()));
        //metrics.addCustomChart(new Metrics.SimplePie("amount_of_modules", () -> ""+moduleManager.getModuleList().size()));

    }

    @Override
    public void onDisable() {
        if(protocolModule != null){
            protocolModule.onDisable();
        }
        leaderBoardManager.disable();

    }

    // Reload
    public void reload(){
        this.reloadConfig();
        if(protocolModule != null){
            protocolModule.onDisable();
            protocolModule.onEnable();
        }
//        cache.reload();
    }

    private void loadCommands(){
        MainCommand command = commandManager.addMainCommand("he")
                .addSubCommand(new ReloadCommand(leaderBoardManager, this))
                .addSubCommand(new MenuCommand(leaderBoardManager))
                .addSubCommand(new InfoCommand(leaderBoardManager));
    }



}
