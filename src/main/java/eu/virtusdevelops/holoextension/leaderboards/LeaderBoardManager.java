package eu.virtusdevelops.holoextension.leaderboards;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.storage.DataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderBoardManager {
    private final HoloExtension plugin;
    private final DataStorage storage;

    private HashMap<String, HashMap<Integer, LeaderBoardEntry>> leaderboards = new HashMap<>(); // cache for leaderboards.
    private HashMap<String, HashMap<Integer, Double>> refreshes = new HashMap<>(); // store values when each slot was last refreshed.

    private List<CacheItem> toCache = new ArrayList<>();

    public LeaderBoardManager(HoloExtension plugin, DataStorage storage) {
        this.plugin = plugin;
        this.storage = storage;
    }



    public LeaderBoardEntry getData(String board, int position){
        if(!leaderboards.containsKey(board)){ // if leaderboard doesnt exist
            return null;
        }

        if(!leaderboards.get(board).containsKey(position)){
            // add new
            leaderboards.get(board).put(position, new LeaderBoardEntry(
                    position, null, "---", 0.0, "", ""
            ));
            refreshes.get(board).put(position, 0.0);
        }

        if(refreshes.get(board).containsKey(position)){
            if(refreshes.get(board).get(position) > 5000){
                toCache.add(new CacheItem(position, board));
            }
        }

        return leaderboards.get(board).get(position);
    }


    public void registerLeaderboard(){

    }
}
