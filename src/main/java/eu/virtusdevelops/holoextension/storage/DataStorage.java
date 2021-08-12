package eu.virtusdevelops.holoextension.storage;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataStorage {


    String getType();

    void setup();
    void setup(List<String> storages);
    void save();
    void startSaver();
    void reload();
    void addDataStorage(String name);


    void add(String storage, Player player, double value);
    double get(String storage, Player player);

    void update(String storage, Player player, double value);

    void add(String storage, UUID uuid, double value);
    double get(String storage, UUID uuid);
    void update(String storage, UUID uuid, double value);

    CompletableFuture<Double> get(UUID uuid, String storage);


    CompletableFuture<HashMap<UUID, Double>> getAll(String storage);
}
