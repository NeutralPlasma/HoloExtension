package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.storage.Cache;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PapiModule extends Module {

    private HoloExtension plugin;
    private DataStorage cache;

    private HashMap<UUID, Double> values = new HashMap<>();
    private HashMap<UUID, Double> sorted = new HashMap<>();
    private List<UUID> users = new ArrayList<>();


    private int counter = 10;
    private boolean updateOffline;

    private static String RETURN_ON_NULL = "";


    public PapiModule(boolean updateOffline, String name, HoloExtension plugin, ModuleDataType type, long delay, long repeat, int size, boolean enabled, DataStorage cache, String callback){
        super(updateOffline, name, plugin, type, ModuleType.PAPI, delay, repeat, size, enabled, callback);
        this.plugin = plugin;
        this.updateOffline = updateOffline;
        this.cache = cache;
        RETURN_ON_NULL = callback;
    }

    @Override
    public void onEnable() {
        setTask(new BukkitRunnable() {
            @Override
            public void run() {
                PapiModule.this.run();
            }
        });
        getTask().runTaskTimerAsynchronously(plugin, getDelay(), getRepeat());

        registerPlaceholders(10);
        cache.addDataStorage(this.getName());


        cache();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        getTask().cancel();
        values.clear();
        sorted.clear();
        users.clear();

        unregisterPlaceholders();
        super.onDisable();
    }

    public void cache(){
        // load all the cached stuff
        if(cache.getType().equals("FLAT")){
            for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                values.put(player.getUniqueId() ,cache.get(this.getName(), player.getUniqueId()));
            }

        }else{
            try {
                values =  cache.getAll(this.getName()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }


    public void run() {
        // Load the online players
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                double value = Double.parseDouble(PlaceholderAPI.setPlaceholders(player, getName()));
                if(values.containsKey(player.getUniqueId())){
                    cache.update(this.getName(), player, value);
                }else{
                    cache.add(this.getName(), player, value);
                }
                values.put(player.getUniqueId(), value);
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }

        // Load the offline players  if enabled.
        if(updateOffline) {
            if (counter >= 10) {
                counter = 0;
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    try{
                        double value = Double.parseDouble(PlaceholderAPI.setPlaceholders(player, getName()));
                        if(values.containsKey(player.getUniqueId())){
                            cache.update(this.getName(), player.getUniqueId(), value);
                        }else{
                            cache.add(this.getName(), player.getUniqueId(), value);
                        }
                        values.put(player.getUniqueId(), value);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
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
            return values.get(users.get(position-1));
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
