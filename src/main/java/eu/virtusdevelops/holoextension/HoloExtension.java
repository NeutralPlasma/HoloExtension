package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.commands.TemporaryCommand;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.storage.Cache;
import eu.virtusdevelops.holoextension.utils.Metrics;
import eu.virtusdevelops.virtuscore.gui.GuiListener;
import eu.virtusdevelops.virtuscore.gui.Handler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class HoloExtension extends JavaPlugin {

    private Economy econ;
    private ModuleManager moduleManager;
    private Cache cache;

    @Override
    public void onEnable() {
        // The enable stuff.
        setupEconomy();
        this.saveDefaultConfig();

        // Cache
        cache = new Cache(this);
        cache.setup();

        // Module manager
        moduleManager = new ModuleManager(this, cache);
        moduleManager.reload();

        // Load commands
        getCommand("he").setExecutor(new TemporaryCommand(this, moduleManager));

        new GuiListener(new Handler(this), this);

        // metrics
        Metrics metrics = new Metrics(this, 5834);
        metrics.addCustomChart(new Metrics.SimplePie("amount_of_modules", () -> ""+moduleManager.getModuleList().size()));
    }


    // Reload
    public void reload(){
        this.reloadConfig();
        cache.reload();
        moduleManager.reload();
    }


    // Vault stuff down here..
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon(){
        return econ;
    }
}
