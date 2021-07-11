package eu.virtusdevelops.holoextension.storage.storages;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MySQLStorage implements DataStorage {

    private HashMap<String, HashMap<UUID, Double>> cache = new HashMap<>();
    private List<String> updateList = new ArrayList<>();
    private HoloExtension plugin;
    private HikariDataSource hikari;
    private BukkitTask task;


    public MySQLStorage(HoloExtension plugin, String username, String password, String dbname, String ip, String port, boolean useSSL){

        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        config.setPoolName("Storage");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(25000);
        config.addDataSourceProperty("serverName", ip);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", dbname);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.addDataSourceProperty("useSSL", useSSL);

        hikari = new HikariDataSource(config);



        this.plugin = plugin;
    }


    // region: system
    @Override
    public void setup() {

    }

    @Override
    public void save() {

    }


    @Override
    public void reload() {
        if(!task.isCancelled()){
            task.cancel();
        }

        task = null;
        cache.clear();

        setup();
        startSaver();

    }

    @Override
    public void startSaver() {
        // run all SQL statements.
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 0L, 1500L);
    }

    @Override
    public void addDataStorage(String name) {
        try {
            hikari.getConnection().prepareStatement(("CREATE TABLE IF NOT EXISTS{name}(" +
                    "ID VARCHAR(36) PRIMARY KEY," +
                    "value double)").replace("{name}", name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // ADD SQL statement
    }
    // endregion




    // region: getters/setters
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
        if(!cache.get(storage).containsKey(uuid)){

            // add SQL statement update.
            cache.get(storage).put(uuid, value);
        }else{
            update(storage, uuid, value);
        }

    }

    @Override
    public double get(String storage, UUID uuid) {
        if(!cache.get(storage).containsKey(uuid)){
            return cache.get(storage).get(uuid);
        }else{
            add(storage, uuid, 0);
            return -1;
        }
    }

    @Override
    public void update(String storage, UUID uuid, double value) {
        // ADD SQL statement
        cache.get(storage).put(uuid, value);
    }

    // endregion

}
