package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.storage.Cache;
import eu.virtusdevelops.simpleholograms.listeners.PlayerJoinEvent;
import eu.virtusdevelops.virtuscore.VirtusCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.plugin.PluginManager;
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
