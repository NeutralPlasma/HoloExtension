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
import java.util.UUID;

public interface DataStorage {



    void setup();
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


}
