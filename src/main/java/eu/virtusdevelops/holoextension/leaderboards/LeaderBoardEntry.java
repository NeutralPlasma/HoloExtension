package eu.virtusdevelops.holoextension.leaderboards;

import eu.virtusdevelops.holoextension.HoloExtension;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaderBoardEntry {
    private String playerName;
    private HoloExtension plugin;
    private int position;
    private UUID uuidPlayer;
    private String boardName;

    private String prefix;
    private String suffix;

    double value;


    public LeaderBoardEntry(HoloExtension holoExtension, int position, UUID uuid, String playerName, String boardName, double value,
                            String prefix, String suffix){
        this.playerName = playerName;
        this.position = position;
        this.plugin = holoExtension;
        this.uuidPlayer = uuid;
        this.boardName = boardName;
        this.value = value;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    public String getPlayer(){
        return playerName;
    }

    public double getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public UUID getUuidPlayer() {
        return uuidPlayer;
    }

    public String getFormated(){
        // return formats blabla..
        return "";
    }
}
