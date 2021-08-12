package eu.virtusdevelops.holoextension.storage.storages;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLiteStorage implements DataStorage {

    private List<String> updateList = new ArrayList<>();
    private HoloExtension plugin;
    private HikariDataSource hikari;
    private BukkitTask task;



    public SQLiteStorage(HoloExtension plugin){

        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + plugin.getDataFolder().getPath() + "/storage.sql" );
        config.setPoolName("Storage");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(25000);
        config.setConnectionTestQuery("SELECT 1");

        hikari = new HikariDataSource(config);



        this.plugin = plugin;
    }

    @Override
    public String getType(){
        return "SQL";
    }

    @Override
    public void setup(){ }

    // region: system
    @Override
    public void setup(List<String> storages) {
        // parse everything from databases
    }

    @Override
    public void save() {
        try(Connection connection = hikari.getConnection()){
            for(String line : updateList){
                connection.prepareStatement(line).execute();
            }
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&a[HE] &7Saving data.."));
        }catch (SQLException exception){
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&c[HE] &7Error occurred while saving data..."));
            exception.printStackTrace();
            // errors.
        }
    }


    @Override
    public void reload() {
        if(!task.isCancelled()){
            task.cancel();
        }

        task = null;

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
        try (Connection connection = hikari.getConnection()) {
            connection.prepareStatement(("CREATE TABLE IF NOT EXISTS `{name}`(" +
                    "ID VARCHAR(36) PRIMARY KEY," +
                    "value double)").replace("{name}", name)).execute();
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&a[HE] &7Adding new table for storage: " + name));
        } catch (SQLException throwables) {
            VirtusCore.console().sendMessage(TextUtils.colorFormat("&c[HE] &7Error occurred while creating table: "  + name));

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
        updateList.add("INSERT INTO " + storage + " VALUES ( " + uuid + ", " + value + "  )");
    }

    @Override
    public double get(String storage, UUID uuid) {
        return 0;
    }

    @Override
    public CompletableFuture<Double> get(UUID uuid, String storage) {

        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = hikari.getConnection()){

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + storage + "WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                var set = statement.executeQuery();
                if( set.next()){
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
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + storage);

                var set = statement.executeQuery();
                while(set.next()){
                    data.put(
                            UUID.fromString(set.getString("id")),
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
        updateList.add("UPDATE " + storage + " SET value = " + value + " WHERE id = '" + uuid + "'");
    }

    // endregion

}
