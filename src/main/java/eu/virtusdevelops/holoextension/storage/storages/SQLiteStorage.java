package eu.virtusdevelops.holoextension.storage.storages;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLiteStorage implements DataStorage {

    private HoloExtension plugin;
    private HikariDataSource hikari;
    private String tablePrefix;



    public SQLiteStorage(HoloExtension plugin){

        HikariConfig config = new HikariConfig();

        File file = new File(plugin.getDataFolder(), "storage.sqlite");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException error){
                error.printStackTrace();
            }
        }
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + file);
        config.setPoolName("Storage");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(25000);
        hikari = new HikariDataSource(config);

        this.tablePrefix = "holoextension_";


        this.plugin = plugin;
    }

    @Override
    public void createTable(String boardName) {
        String SQL = "CREATE TABLE IF NOT EXISTS `" + tablePrefix + boardName + "`(" +
                "name VARCHAR(30)," +
                "prefix VARCHAR(255)," +
                "suffix VARCHAR(255)," +
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
                "value DOUBLE" +
                ")";
        try(Connection connection = hikari.getConnection()){
            connection.prepareStatement(SQL).execute();
        }catch (SQLException exception){

        }
    }

    @Override
    public void addUser(String boardName, LeaderBoardEntry data){

        String SQL = "REPLACE INTO  " + tablePrefix + boardName + "(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?)";
        // async execute sql xd
        try(Connection connection = hikari.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, data.getPlayer());
            statement.setString(2, data.getPrefix());
            statement.setString(3, data.getSuffix());
            statement.setString(4, data.getUuidPlayer().toString());
            statement.setDouble(5, data.getValue());

            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
            // print error to console
        }
    }

    @Override
    public void addOfflineUser(String boardName, LeaderBoardEntry data){

        String SQL = "REPLACE INTO  " + tablePrefix + boardName + "(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?)";
        // async execute sql xd
        try(Connection connection = hikari.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, data.getPlayer());
            statement.setString(2, data.getPrefix());
            statement.setString(3, data.getSuffix());
            statement.setString(4, data.getUuidPlayer().toString());
            statement.setDouble(5, data.getValue());

            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }


    @Override
    public void addMultiple(String boardName, List<LeaderBoardEntry> data){
        String SQL = "INSERT INTO  " + tablePrefix + boardName + "(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = ?, prefix = ?, suffix = ?, uuid = ?, value = ?";

        try(Connection connection = hikari.getConnection()){
            for(LeaderBoardEntry entry : data){
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setString(1, entry.getPlayer());
                statement.setString(2, entry.getPrefix());
                statement.setString(3, entry.getSuffix());
                statement.setString(4, entry.getUuidPlayer().toString());
                statement.setDouble(5, entry.getValue());

                statement.setString(6, entry.getPlayer());
                statement.setString(7, entry.getPrefix());
                statement.setString(8, entry.getSuffix());
                statement.setString(9, entry.getUuidPlayer().toString());
                statement.setDouble(10, entry.getValue());

                statement.execute();
            }
        }catch (SQLException error){
            // print error to console
        }

    }



    @Override
    public LeaderBoardEntry getData(String boardName, int position){
        try(Connection connection = hikari.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `" + tablePrefix + boardName + "` ORDER BY value DESC LIMIT " + (position-1) + ", 1");
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return new LeaderBoardEntry(
                        position,
                        UUID.fromString(set.getString("uuid")),
                        set.getString("name"),
                        set.getDouble("value"),
                        set.getString("prefix"),
                        set.getString("suffix")
                );
            }

        }catch(SQLException error){
            error.printStackTrace();
            return new LeaderBoardEntry(position, UUID.randomUUID(), "----", 0.0, "", "");
        }

        return new LeaderBoardEntry(position, UUID.randomUUID(), "----", 0.0, "", "");
    }

}
