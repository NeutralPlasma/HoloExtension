package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.baltops.BaltopV1;
import eu.virtusdevelops.holoextension.modules.protocolLib.ProtocolModule;
import eu.virtusdevelops.holoextension.storage.Cache;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
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
                ModuleDataType.NUMBER,
                0,
                config.getLong("modules.baltop.interval"),
                config.getInt(("modules.baltop.size")),
                config.getBoolean("modules.baltop.enabled"),
                config.getString("modules.baltop.noplayer")
        );
        moduleList.add(baltopV1);


        if(VirtusCore.plugins().isPluginEnabled("ProtocolLib")){
            ProtocolModule protocolModule = new ProtocolModule(
                    false,
                    "ProtocolLib",
                    plugin,
                    ModuleDataType.NUMBER,
                    ModuleType.OTHER,
                    0,
                    0,
                    0,
                    config.getBoolean("modules.protocollib.enabled"),
                    ""
            );
            moduleList.add(protocolModule);
        }


        // Register all papi modules
        for(String module : config.getConfigurationSection("papi").getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection("papi." + module);
            PapiModule papiModule = new PapiModule(
                    section.getBoolean("updateOffline"),
                    module,
                    plugin,
                    ModuleDataType.valueOf(section.getString("type").toUpperCase()),
                    0L,
                    section.getLong("interval"),
                    section.getInt("size"),
                    section.getBoolean("enabled"),
                    cache,
                    section.getString("noplayer")
            );
            moduleList.add(papiModule);

        }

        for (Module module : moduleList){
            if(module.isEnabled()){
                module.onEnable();
            }
        }
    }

    public void updateModule(Module module){
        String path = "modules." + module.getName();
        if(module.getModuleType() == ModuleType.PAPI){
            path = "papi." + module.getName();
        }
        plugin.getConfig().set(path + ".updateOffline", true);
        plugin.getConfig().set(path + ".type", module.getType().toString());
        plugin.getConfig().set(path + ".interval", 200L);
        plugin.getConfig().set(path + ".size", 10);
        plugin.getConfig().set(path + ".enabled", true);
        plugin.getConfig().set(path + ".noplayer", "NULL");
        plugin.saveConfig();
    }

    public void createNewPapiModule(String name, ModuleDataType type){
        String path = "papi." + name;
        plugin.getConfig().set(path + ".updateOffline", true);
        plugin.getConfig().set(path + ".type", type.toString());
        plugin.getConfig().set(path + ".interval", 200L);
        plugin.getConfig().set(path + ".size", 10);
        plugin.getConfig().set(path + ".enabled", true);
        plugin.getConfig().set(path + ".noplayer", "NULL");
        plugin.saveConfig();
        Module module = new PapiModule(true, name, plugin, type ,0L, 200L, 10, true, cache, "NULL" );
        moduleList.add(module);
        module.onEnable();
    }

    public List<Module> getModuleList() {
        return moduleList;
    }



    public Module getModule(String name){
        for(Module module: moduleList){
            if(module.getName().equals(name)){
                return module;
            }
        }
        return null;
    }
}
