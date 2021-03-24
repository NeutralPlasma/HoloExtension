package eu.virtusdevelops.holoextension.modules.baltops;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class BaltopV1 extends Module {

    private HoloExtension plugin;

    private HashMap<UUID, Double> values = new HashMap<>();
    private HashMap<UUID, Double> sorted = new HashMap<>();
    private List<UUID> users = new ArrayList<>();

    private int counter = 0;
    private boolean updateOffline;


    public BaltopV1(boolean updateOffline){
        super(updateOffline);
        this.updateOffline = updateOffline;
    }

    @Override
    public void onEnable(HoloExtension plugin, long delay, long schedule) {
        this.plugin = plugin;
        this.runTaskTimerAsynchronously(plugin, delay, schedule);

        // TODO: Register the placeholders

        super.onEnable(plugin, delay, schedule);
    }

    @Override
    public void onDisable() {
        this.cancel();
        values.clear();
        sorted.clear();
        users.clear();

        // TODO: Unregister the placeholders.

        super.onDisable();
    }

    @Override
    public void run() {
        Economy economy = plugin.getEcon();
        // Load the online players balances
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                values.put(player.getUniqueId(), economy.getBalance(player));
            }catch (Exception ignored){}
        }

        // Load the offline players balance if enabled.
        if(updateOffline) {
            if (counter >= 10) {
                counter = 0;
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    try{
                        values.put(player.getUniqueId(), economy.getBalance(player));
                    }catch (Exception ignored){}
                }
            }
            counter++;
        }


        // Sort everything and add those top 10 thingies.
        sorted = values
                .entrySet()
                .stream()
                .filter(name -> !name.getKey().equals(UUID.randomUUID()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        users = new ArrayList<>(sorted.keySet());
    }

    @Override
    public double getValue(int positon) {
        return plugin.getEcon().getBalance(Bukkit.getOfflinePlayer(users.get(positon)));
    }

    @Override
    public String getPlayer(int position){
        return Bukkit.getOfflinePlayer(users.get(position)).getName();
    }
}
