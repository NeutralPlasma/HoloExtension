package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.modules.ModuleType;
import eu.virtusdevelops.holoextension.modules.PapiModule;
import eu.virtusdevelops.holoextension.modules.baltops.BaltopV1;
import eu.virtusdevelops.holoextension.storage.Cache;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
