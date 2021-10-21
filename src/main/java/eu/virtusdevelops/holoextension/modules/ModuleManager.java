package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.baltops.BaltopV1;
import eu.virtusdevelops.holoextension.modules.protocolLib.ProtocolModule;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private HoloExtension plugin;
    private List<Module> moduleList = new ArrayList<>();
    //private Cache cache;
    private DataStorage cache;

    public ModuleManager(HoloExtension plugin, DataStorage cache){
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
                config.getString("modules.baltop.noplayer"),
                config.isSet("modules.baltop.format") ? config.getInt("modules.baltop.format") : 0
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
                    config.getBoolean("modules.ProtocolLib.enabled"),
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
                    section.getString("noplayer"),
                    section.isSet("format") ? section.getInt("format") : 0
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
        plugin.getConfig().set(path + ".updateOffline", module.isUpdateOffline());
        plugin.getConfig().set(path + ".type", module.getType().toString());
        plugin.getConfig().set(path + ".interval", module.getRepeat());
        plugin.getConfig().set(path + ".size", module.getSize());
        plugin.getConfig().set(path + ".enabled", module.isEnabled());
        plugin.getConfig().set(path + ".noplayer", module.getNoplayer());
        plugin.getConfig().set(path + ".format", module.getFormat());
        plugin.saveConfig();
    }

    public void createNewPapiModule(String name, ModuleDataType type){
        String path = "papi." + name;
        plugin.getConfig().set(path + ".updateOffline", true);
        plugin.getConfig().set(path + ".type", type.toString());
        plugin.getConfig().set(path + ".interval", 200);
        plugin.getConfig().set(path + ".size", 10);
        plugin.getConfig().set(path + ".enabled", true);
        plugin.getConfig().set(path + ".noplayer", "NULL");
        plugin.getConfig().set(path + ".format", 1);
        plugin.saveConfig();
        Module module = new PapiModule(true, name, plugin, type ,0L, 200L, 10, true, cache, "NULL", 1);
        moduleList.add(module);
        module.onEnable();
    }


    public Module createNewPapiModule(ModuleData data){
        String path = "papi." + data.getPlaceholder();
        plugin.getConfig().set(path + ".updateOffline", data.isUpdateOffline());
        plugin.getConfig().set(path + ".type", data.getType().toString());
        plugin.getConfig().set(path + ".interval", data.getRefresh());
        plugin.getConfig().set(path + ".size", data.getSize());
        plugin.getConfig().set(path + ".enabled", true);
        plugin.getConfig().set(path + ".noplayer", "NULL");
        plugin.getConfig().set(path + ".format", data.getFormat());
        plugin.saveConfig();

        Module module = new PapiModule(data.isUpdateOffline(),
                data.getPlaceholder(),
                plugin,
                data.getType(),
                0L,
                data.getRefresh(),
                data.getSize(),
                true,
                cache,
                "NULL" ,
                data.getFormat());

        moduleList.add(module);
        module.onEnable();
        return module;
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
