package eu.virtusdevelops.holoextension;

import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.baltops.BaltopV1;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class HoloExtension extends JavaPlugin {

    private Economy econ;
    private List<Module> moduleList = new ArrayList<>();

    @Override
    public void onEnable() {
        // The enable stuff.
        setupEconomy();

        Module test = new BaltopV1(false);
        test.onEnable(this, 0L, 200L);
        moduleList.add(test);

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
