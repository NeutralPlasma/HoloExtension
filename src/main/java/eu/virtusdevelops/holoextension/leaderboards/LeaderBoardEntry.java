package eu.virtusdevelops.holoextension.leaderboards;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.utils.TextUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaderBoardEntry {
    private String playerName;
    private int position;
    private UUID uuidPlayer;

    private String prefix;
    private String suffix;

    double value;


    public LeaderBoardEntry(int position, UUID uuid, String playerName, double value,
                            String prefix, String suffix){
        this.playerName = playerName;
        this.position = position;
        this.uuidPlayer = uuid;
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

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public UUID getUuidPlayer() {
        return uuidPlayer;
    }

    public String getFormated(int format){
        // return formats blabla..
        if(format == 4){
            return TextUtils.formatTime(value);
        }
        return TextUtils.formatValue(format, value);
    }
}
