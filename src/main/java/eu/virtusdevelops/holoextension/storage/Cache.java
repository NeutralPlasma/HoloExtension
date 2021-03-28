package eu.virtusdevelops.holoextension.storage;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Cache {
    private HoloExtension plugin;

    private FileConfiguration fileConfiguration;
    private File file;


    public Cache(HoloExtension holoExtension){
        plugin = holoExtension;
    }

    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        file = new File(plugin.getDataFolder(), "cache.yml");
        if(!file.exists()){
            try{
                file.createNewFile();
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
                plugin.saveResource("cache.yml", true);
            }catch (IOException e){
                VirtusCore.console().sendMessage(TextUtils.colorFormat("&c" + e.getMessage()));
            }
        }else{
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }
        startSaver();
    }

    public FileConfiguration get(){ return fileConfiguration;}
    public void save(){
        try{
            fileConfiguration.save(file);
        }catch (IOException e){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&c" + e.getMessage()));
        }
    }
    // Cache saver
    public void startSaver(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 0L, 1500L);
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
