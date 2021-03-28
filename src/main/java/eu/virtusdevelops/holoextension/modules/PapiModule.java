package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.virtuscore.VirtusCore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PapiModule extends Module {

    private HoloExtension plugin;

    private HashMap<UUID, Double> values = new HashMap<>();
    private HashMap<UUID, Double> sorted = new HashMap<>();
    private List<UUID> users = new ArrayList<>();


    private int counter = 10;
    private boolean updateOffline;

    private static String RETURN_ON_NULL = "";


    public PapiModule(boolean updateOffline, String name, HoloExtension plugin, ModuleType type, long delay, long repeat, int size){
        super(updateOffline, name, plugin, type, delay, repeat, size);
        this.plugin = plugin;
        this.updateOffline = updateOffline;
    }

    @Override
    public void onEnable() {
        this.runTaskTimerAsynchronously(this.plugin, delay, repeat);
        registerPlaceholders(10);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.cancel();
        values.clear();
        sorted.clear();
        users.clear();

        unregisterPlaceholders();
        super.onDisable();
    }

    @Override
    public void run() {
        // Load the online players
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                values.put(player.getUniqueId(), Double.valueOf(PlaceholderAPI.setPlaceholders(player, name)));
            }catch (Exception ignored){ }
        }

        // Load the offline players  if enabled.
        if(updateOffline) {
            if (counter >= 10) {
                counter = 0;
                VirtusCore.console().sendMessage("Updating offline.");
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    try{
                        values.put(player.getUniqueId(), Double.valueOf(PlaceholderAPI.setPlaceholders(player, name)));
                        VirtusCore.console().sendMessage("Updating: " + player.getName());
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
            Player player = Bukkit.getPlayer(users.get(position-1));
            if(player != null){ return Double.parseDouble(PlaceholderAPI.setPlaceholders(player, name));}
            else{ Double.parseDouble(PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(users.get(position-1)), name));}

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
