package eu.virtusdevelops.holoextension.leaderboards.modules;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardEntry;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class BalTopModule implements DefaultModule{
    private boolean tickOffline;
    private Economy economy;
    private String name = "baltop";
    private DataStorage storage;
    private int format;


    public BalTopModule(boolean tickOffline, DataStorage storage, int format) {
        setupEconomy();
        this.tickOffline = tickOffline;
        this.storage = storage;
        this.format = format;
    }


    @Override
    public void tick(long tick){

        if(tick%10 == 0){
            List<LeaderBoardEntry> toAdd = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                toAdd.add(new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        economy.getBalance(player),
                        "",
                        ""
                ));
            }
            storage.addMultipleOffline(name, toAdd);
            toAdd.clear();
        }else {
            List<LeaderBoardEntry> toAdd = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                toAdd.add(new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        economy.getBalance(player),
                        PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"),
                        PlaceholderAPI.setPlaceholders(player, "%vault_suffix%")
                ));
            }
            storage.addMultiple(name, toAdd);
            toAdd.clear();
        }
        if(tickOffline){
            List<LeaderBoardEntry> toAdd = new ArrayList<>();
            for(OfflinePlayer player: Bukkit.getOfflinePlayers()){
                toAdd.add(new LeaderBoardEntry(
                        0,
                        player.getUniqueId(),
                        player.getName(),
                        economy.getBalance(player),
                        "",
                        ""
                ));
            }
            storage.addMultiple(name, toAdd);
            toAdd.clear();
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
        return name;
    }

    @Override
    public int getFormat() {
        return format;
    }


    private void setupEconomy() {
        if (VirtusCore.server().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = VirtusCore.server().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

}
