package eu.virtusdevelops.holoextension.storage.storages;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class MySQLStorage implements DataStorage {

    private HoloExtension plugin;
    private HikariDataSource hikari;
    private String tablePrefix;


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
    public void createTable(String boardName) {
        String SQL = "CREATE TABLE IF NOT EXISTS `" + tablePrefix + boardName + "`(" +
                "name VARCHAR(30)," +
                "prefix VARCHAR(255)," +
                "suffix VARCHAR(255)," +
                "uuid VARCHAR(36)," +
                "value DOUBLE" +
                ")";
        try(Connection connection = hikari.getConnection()){
            connection.prepareStatement(SQL).execute();
        }catch (SQLException exception){

        }
    }

    @Override
    public void addUser(String boardName, LeaderBoardEntry data){

        String SQL = "INSERT INTO  " + tablePrefix + boardName + "(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = ?, prefix = ?, suffix = ?, uuid = ?, value = ?";
        // async execute sql xd
        try(Connection connection = hikari.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, data.getPlayer());
            statement.setString(2, data.getPrefix());
            statement.setString(3, data.getSuffix());
            statement.setString(4, data.getUuidPlayer().toString());
            statement.setDouble(5, data.getValue());

            statement.setString(6, data.getPlayer());
            statement.setString(7, data.getPrefix());
            statement.setString(8, data.getSuffix());
            statement.setString(9, data.getUuidPlayer().toString());
            statement.setDouble(10, data.getValue());

            statement.execute();
        }catch (SQLException error){
            // print error to console
        }
    }

    @Override
    public void addOfflineUser(String boardName, LeaderBoardEntry data){

        String SQL = "INSERT INTO  " + tablePrefix + boardName + "(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = ?, uuid = ?, value = ?";
        // async execute sql xd
        try(Connection connection = hikari.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, data.getPlayer());
            statement.setString(2, data.getPrefix());
            statement.setString(3, data.getSuffix());
            statement.setString(4, data.getUuidPlayer().toString());
            statement.setDouble(5, data.getValue());

            statement.setString(6, data.getPlayer());
            statement.setString(7, data.getUuidPlayer().toString());
            statement.setDouble(8, data.getValue());

            statement.execute();
        }catch (SQLException error){
            // print error to console
        }
    }


    @Override
    public void addMultipleOffline(String boardName, List<LeaderBoardEntry> data){
        String SQL = "INSERT INTO `" + tablePrefix + boardName + "`(name, prefix, suffix, uuid, value) VALUES (?, ?, ?, ?, ?)" +
                "ON CONFLICT(uuid) DO UPDATE SET value = ?";

        try(Connection connection = hikari.getConnection()){
            for(LeaderBoardEntry entry : data){
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setString(1, entry.getPlayer());
                statement.setString(2, entry.getPrefix());
                statement.setString(3, entry.getSuffix());
                statement.setString(4, entry.getUuidPlayer().toString());
                statement.setDouble(5, entry.getValue());
                statement.setDouble(6, entry.getValue());

                statement.execute();
            }
        }catch (SQLException error){
            // print error to console
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
            return new LeaderBoardEntry(position, UUID.randomUUID(), "----", 0.0, "", "");
        }

        return null;
    }

}
