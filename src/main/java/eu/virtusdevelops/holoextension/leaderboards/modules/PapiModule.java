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


    public PapiModule(boolean tickOffline, String name, DataStorage storage) {
        this.tickOffline = tickOffline;
        this.storage = storage;
        this.name = name;
    }


    @Override
    public void tick() {
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


        if(tickOffline){
            for(OfflinePlayer player: Bukkit.getOfflinePlayers()){
                storage.addOfflineUser(name, new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        Double.parseDouble(PlaceholderAPI.setPlaceholders(player, name)),
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
}
