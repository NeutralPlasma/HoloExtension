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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class MySQLStorage implements DataStorage {

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

    @Override
    public String getType(){
        return "SQL";
    }

    @Override
    public void setup(){
        startSaver();
    }

    // region: system
    @Override
    public void setup(List<String> storages) {
        // parse everything from databases
    }

    @Override
    public void save() {
        List<String> lines = new ArrayList<>(updateList);
        updateList.clear();
        try(Connection connection = hikari.getConnection()){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&a[HE] &7Saving data.."));
            for(String line : lines){
                connection.prepareStatement(line).execute();
            }
        }catch (SQLException exception){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&c[HE] &7Error occurred while saving data..."));
            exception.printStackTrace();
        }
    }


    @Override
    public void reload() {
        if(!task.isCancelled()){
            task.cancel();
        }
        save();
        task = null;

        setup();
        startSaver();

    }

    @Override
    public void startSaver() {
        // run all SQL statements.
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 0L, 800L);
    }

    @Override
    public void addDataStorage(String name) {
        try (Connection connection = hikari.getConnection()) {
            connection.prepareStatement(("CREATE TABLE IF NOT EXISTS `{name}`(" +
                    "ID VARCHAR(36) PRIMARY KEY," +
                    "value double)").replace("{name}", name)).execute();
        } catch (SQLException throwable) {
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&8[&bHE&8] &cError occurred while creating table: "  + name));
            throwable.printStackTrace();
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
//        VirtusCore.console().sendMessage("Adding player");
        updateList.add("INSERT INTO `" + storage + "` VALUES ( '" + uuid + "', " + value + "  )");
    }

    @Override
    public double get(String storage, UUID uuid) {
        return 0;
    }

    @Override
    public CompletableFuture<Double> get(UUID uuid, String storage) {

        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = hikari.getConnection()){

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + storage + "` WHERE ID = ?");
                statement.setString(1, uuid.toString());
                var set = statement.executeQuery();
                if(set.next()){
                    return set.getDouble("value");
                }
                return -1.0;
            }catch(SQLException error){
                return 0.0;
            }
        });
    }


    public CompletableFuture<HashMap<UUID, Double>> getAll(String storage){
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = hikari.getConnection()){
                HashMap<UUID, Double> data = new HashMap<>();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + storage + "`");

                var set = statement.executeQuery();
                while(set.next()){
                    data.put(
                            UUID.fromString(set.getString("ID")),
                            set.getDouble("value")
                    );
                }
                return data;
            }catch(SQLException error){
                return new HashMap<>();
            }
        });
    }

    @Override
    public void update(String storage, UUID uuid, double value) {
        updateList.add("UPDATE `" + storage + "` SET value = " + value + " WHERE ID = '" + uuid + "'");
    }

    // endregion

}
