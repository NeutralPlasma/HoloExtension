package eu.virtusdevelops.holoextension.storage.storages;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FlatFileStorage implements DataStorage {

    private FileConfiguration fileConfiguration;
    private File file;
    private HoloExtension plugin;
    private BukkitTask task;

    public FlatFileStorage(HoloExtension plugin){
        this.plugin = plugin;
    }

    @Override
    public void addDataStorage(String name) {}

    @Override
    public void setup(List<String> storages){
        setup();
    }

    @Override
    public String getType(){
        return "FLAT";
    }

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
        startSaver();
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
        if(!task.isCancelled()){
            task.cancel();
        }
        save();
        task = null;

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        startSaver();
    }

    @Override
    public void startSaver() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 0L, 800L);
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



    @Override
    public CompletableFuture<Double> get(UUID uuid, String storage) {
        return CompletableFuture.supplyAsync(() -> get(storage, uuid));
    }

    @Override
    public CompletableFuture<HashMap<UUID, Double>> getAll(String storage) {
        return null;
    }

}
