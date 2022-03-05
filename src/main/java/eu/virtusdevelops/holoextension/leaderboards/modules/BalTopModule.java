package eu.virtusdevelops.holoextension.leaderboards.modules;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BalTopModule implements DefaultModule{
    private boolean tickOffline;
    private Economy economy;

    private String name;
    private DataStorage storage;


    public BalTopModule(boolean tickOffline, String name, DataStorage storage, Economy economy) {
        this.tickOffline = tickOffline;
        this.storage = storage;
        this.name = name;
        this.economy = economy;
    }


    @Override
    public void tick(){
        for(Player player : Bukkit.getOnlinePlayers()){
            storage.addUser(name, new LeaderBoardEntry(
                    0,
                    player.getUniqueId(),
                    player.getName(),
                    1,
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
                        1,
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
}
