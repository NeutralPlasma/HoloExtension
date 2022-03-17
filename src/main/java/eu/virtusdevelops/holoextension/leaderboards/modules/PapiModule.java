package eu.virtusdevelops.holoextension.leaderboards.modules;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PapiModule implements DefaultModule{
    private boolean tickOffline;
    private String name;
    private DataStorage storage;
    private int format;


    public PapiModule(boolean tickOffline, String name, DataStorage storage, int format) {
        this.tickOffline = tickOffline;
        this.storage = storage;
        this.name = name;
        this.format = format;
    }


    @Override
    public void tick(long tick) {
        if(tick%10 == 0){
            for(Player player : Bukkit.getOnlinePlayers()){
                storage.addOfflineUser(name, new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        Double.parseDouble(PlaceholderAPI.setPlaceholders(player, name)),
                        "",
                        ""
                ));
            }
        }else{
            for(Player player : Bukkit.getOnlinePlayers()){
                storage.addUser(name, new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        Double.parseDouble(PlaceholderAPI.setPlaceholders(player, name)),
                        PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"),
                        PlaceholderAPI.setPlaceholders(player, "%vault_suffix%")
                ));
            }
        }




        if(tickOffline){
            for(OfflinePlayer player: Bukkit.getOfflinePlayers()){
                double value = parseStringToDouble(PlaceholderAPI.setPlaceholders(player, name));
                storage.addOfflineUser(name, new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        value,
                        "",
                        ""
                ));
            }
        }
    }



    @Override
    public void init(){
        storage.createTable(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameFormated() {
        return name.replace("%", "");
    }

    @Override
    public int getFormat() {
        return format;
    }

    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }
}
