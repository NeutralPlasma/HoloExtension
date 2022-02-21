package eu.virtusdevelops.holoextension.storage;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoard;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
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


    void createTable(String boardName);

    void addUser(String boardName, LeaderBoardEntry data);
    void addMultiple(String boardName, List<LeaderBoardEntry> data);

    void updateUser(String boardName, LeaderBoardEntry data);
    void updateMultiple(String boardName, List<LeaderBoardEntry> data);

    LeaderBoardEntry getData(String boardName, int position);
}
