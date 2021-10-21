package eu.virtusdevelops.holoextension.modules.baltops;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleDataType;
import eu.virtusdevelops.holoextension.modules.ModuleType;
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

    private int counter = 10;
    private boolean updateOffline;

    private static String RETURN_ON_NULL = "";


    public BaltopV1(boolean updateOffline, String name, HoloExtension plugin, ModuleDataType type, long delay, long repeat, int size, boolean enabled, String callback, int format){
        super(updateOffline, name, plugin, type, ModuleType.OTHER, delay, repeat, size, enabled, callback, format);
        this.plugin = plugin;
        this.updateOffline = updateOffline;
        RETURN_ON_NULL = callback;
    }

    @Override
    public void onEnable() {

        values = new HashMap<>();
        sorted = new HashMap<>();
        users = new ArrayList<>();

        setTask(new BukkitRunnable() {
            @Override
            public void run() {
                BaltopV1.this.run();
            }
        });
        getTask().runTaskTimerAsynchronously(plugin, getDelay(), getRepeat());

        registerPlaceholders(10);
        super.onEnable();
    }


    @Override
    public void onDisable() {
        getTask().cancel();
        unregisterPlaceholders();

        values = null;
        sorted = null;
        users = null;

        super.onDisable();
    }

    public void run() {
        Economy economy = plugin.getEcon();
        // Load the online players balances
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                values.put(player.getUniqueId(), economy.getBalance(player));
            }catch (Exception ignored){ }
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
                //.filter(name -> !name.getKey().equals(UUID.randomUUID()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        users = new ArrayList<>(sorted.keySet());
    }


    @Override
    public double getValue(int position) {
        if(users.size() >= position){

            return plugin.getEcon().getBalance(Bukkit.getOfflinePlayer(users.get(position-1) ));
        }
        return 0.0;
    }

    @Override
    public String getPlayer(int position){
        if(users.size() >= position){
            return Bukkit.getOfflinePlayer(users.get(position-1)).getName();
        }
        return RETURN_ON_NULL;
    }




}
