package eu.virtusdevelops.holoextension.storage.storages;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FlatFileStorage implements DataStorage {

    private FileConfiguration fileConfiguration;
    private File file;
    private HoloExtension plugin;

    public FlatFileStorage(HoloExtension plugin){
        this.plugin = plugin;
    }

    @Override
    public void addDataStorage(String name) {}

    @Override
    public void setup() {
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
    }

    @Override
    public void save() {
        try{
            fileConfiguration.save(file);
        }catch (IOException e){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&c" + e.getMessage()));
        }
    }



    @Override
    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void startSaver() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 0L, 1500L);
    }



    @Override
    public void add(String storage, Player player, double value) {
        add(storage, player.getUniqueId(), value);
    }

    @Override
    public double get(String storage, Player player) {
        return get(storage, player.getUniqueId());
    }

    @Override
    public void update(String storage, Player player, double value) {
        update(storage, player.getUniqueId(), value);
    }

    @Override
    public void add(String storage, UUID uuid, double value) {
        fileConfiguration.set("data." + storage + "." + uuid, value);
    }

    @Override
    public double get(String storage, UUID uuid) {
        return fileConfiguration.getDouble("data." + storage + "." + uuid);
    }

    @Override
    public void update(String storage, UUID uuid, double value) {
        fileConfiguration.set("data." + storage + "." + uuid, value);
    }

}
