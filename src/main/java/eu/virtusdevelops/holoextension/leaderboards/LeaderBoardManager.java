package eu.virtusdevelops.holoextension.leaderboards;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.leaderboards.modules.BalTopModule;
import eu.virtusdevelops.holoextension.leaderboards.modules.DefaultModule;
import eu.virtusdevelops.holoextension.leaderboards.modules.PapiModule;
import eu.virtusdevelops.holoextension.storage.DataStorage;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderBoardManager {
    private final HoloExtension plugin;
    private final DataStorage storage;

    private List<DefaultModule> modules = new ArrayList<>();
    private HashMap<String, HashMap<Integer, LeaderBoardEntry>> leaderboards = new HashMap<>(); // cache for leaderboards.
    private HashMap<String, HashMap<Integer, Long>> refreshes = new HashMap<>(); // store values when each slot was last refreshed.

    private List<CacheItem> toCache = new ArrayList<>();

    private BukkitTask task;

    public LeaderBoardManager(HoloExtension plugin, DataStorage storage) {
        this.plugin = plugin;
        this.storage = storage;


        load();
    }



    public LeaderBoardEntry getData(String board, int position){
        if(!leaderboards.containsKey(board)){ // if leaderboard doesnt exist
            return new LeaderBoardEntry(0, null, "INVALID LEADERBOARD", 0.0, "", "");
        }

        if(!leaderboards.get(board).containsKey(position)){
            // add new
            leaderboards.get(board).put(position, new LeaderBoardEntry(
                    position, null, "----", 0.0, "", ""
            ));
            refreshes.get(board).put(position, 0L);
        }

        if(refreshes.get(board).containsKey(position)){
            if(System.currentTimeMillis() - refreshes.get(board).get(position) > 25000){
                //VirtusCore.console().sendMessage("Caching new user");
                refreshes.get(board).put(position, System.currentTimeMillis());
                toCache.add(new CacheItem(position, board));
            }
        }

        return leaderboards.get(board).get(position);
    }

    public void load() {
        // load modules
        registerLeaderboard(new BalTopModule(
                plugin.getConfig().getBoolean("modules.baltop.updateOffline"),
                storage,
                plugin.getConfig().getInt("modules.baltop.format")
        ));

        // load papi modules
        for(String moduleName : plugin.getConfig().getConfigurationSection("papi").getKeys(false)){
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("papi." + moduleName);
            registerLeaderboard(new PapiModule(
                    section.getBoolean("updateOffline"),
                    moduleName,
                    storage,
                    section.getInt("format")
            ));
        }

        // start updating task
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, 10L, 400L);
    }

    public void reload(){
        task.cancel();
        for(DefaultModule module : modules){
            unregisterPlaceholders(module.getName());
        }
        modules.clear();
        refreshes.clear();
        leaderboards.clear();
        toCache.clear();

        // load modules
        load();
    }

    // tick
    public void tick(){
        // go thru cache and cache it
        List<CacheItem> temp = new ArrayList<>(toCache);
        toCache.clear();
        for(CacheItem item : temp){
            leaderboards.get(item.getBoard()).put(item.getPosition(),storage.getData(item.getBoard(), item.getPosition()));
            refreshes.get(item.getBoard()).put(item.getPosition(), System.currentTimeMillis());
            //VirtusCore.console().sendMessage("Caching pos: " + item.getPosition() + " on board: " + item.getBoard());
        }
        temp.clear();

        // tick every module
        modules.forEach(DefaultModule::tick);
    }



    public void registerLeaderboard(DefaultModule module){
        module.init();
        // finish
        modules.add(module);
        leaderboards.put(module.getName(), new HashMap<>());
        refreshes.put(module.getName(), new HashMap<>());
        // init placeholders
        for(int i = 1; i <= 10; i++){
            int finalI = i;
            String namePlaceholder = "he_" + module.getNameFormated() + "_" + i + "_name";
            HologramsAPI.registerPlaceholder(plugin, namePlaceholder, 10, () -> getData(module.getName(), finalI).getPlayer());

            String prefixPlaceholder = "he_" + module.getNameFormated() + "_" + i + "_prefix";
            HologramsAPI.registerPlaceholder(plugin, prefixPlaceholder, 10, () -> getData(module.getName(), finalI).getPrefix());

            String suffixPlaceholder = "he_" + module.getNameFormated() + "_" + i + "_suffix";
            HologramsAPI.registerPlaceholder(plugin, suffixPlaceholder, 10, () -> getData(module.getName(), finalI).getSuffix());

            String valuePlaceholder = "he_" + module.getNameFormated() + "_" + i + "_value";
            HologramsAPI.registerPlaceholder(plugin, valuePlaceholder, 10, () -> getData(module.getName(), finalI).getFormated(module.getFormat()));

        }

    }

    public void unregisterPlaceholders(String name){
        for(int i = 1; i <= 10; i++){
            String namePlaceholder = "he_" + name + "_" + i + "_name";
            HologramsAPI.unregisterPlaceholder(plugin, namePlaceholder);

            String prefixPlaceholder = "he_" + name + "_" + i + "_prefix";
            HologramsAPI.unregisterPlaceholder(plugin, prefixPlaceholder);

            String suffixPlaceholder = "he_" + name + "_" + i + "_suffix";
            HologramsAPI.unregisterPlaceholder(plugin, suffixPlaceholder);

            String valuePlaceholder = "he_" + name + "_" + i + "_value";
            HologramsAPI.unregisterPlaceholder(plugin, valuePlaceholder);
        }
    }
}
