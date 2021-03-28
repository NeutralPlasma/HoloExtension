package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.baltops.BaltopV1;
import eu.virtusdevelops.holoextension.storage.Cache;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private HoloExtension plugin;
    private List<Module> moduleList = new ArrayList<>();
    private Cache cache;

    public ModuleManager(HoloExtension plugin, Cache cache){
        this.plugin = plugin;
        this.cache = cache;
    }

    public void reload(){
        // Stop all the modules..
        for (Module module : moduleList){
            module.onDisable();
        }
        moduleList.clear();

        // Re register all modules.
        ConfigurationSection config = plugin.getConfig();
        BaltopV1 baltopV1 = new BaltopV1(
                config.getBoolean("modules.baltop.updateOffline"),
                "baltop",
                plugin,
                ModuleType.NUMBER,
                0,
                config.getLong("modules.baltop.interval"),
                config.getInt(("modules.baltop.size"))
        );
        moduleList.add(baltopV1);

        // Register all papi modules
        for(String module : config.getConfigurationSection("papi").getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection("papi." + module);
            PapiModule papiModule = new PapiModule(
                    section.getBoolean("updateOffline"),
                    module,
                    plugin,
                    ModuleType.valueOf(section.getString("type").toUpperCase()),
                    0L,
                    section.getLong("interval"),
                    section.getInt("size")
            );
            moduleList.add(papiModule);
        }





        for (Module module : moduleList){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&8[&bHE&8] &aEnabling " + module.name + "... " + module.repeat));
            module.onEnable();
        }
    }
}
