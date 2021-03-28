package eu.virtusdevelops.holoextension.modules;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.utils.TextUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class Module extends BukkitRunnable {
    public final String name;
    public long delay;
    public long repeat;
    public int size;
    private HoloExtension plugin;
    private ModuleType type;


    private List<String> placeholders = new ArrayList<>();

    public Module(boolean updateOffline, String name, HoloExtension plugin, ModuleType type, long delay, long repeat, int size){
        this.type = type;
        this.name = name;
        this.plugin = plugin;
        this.delay = delay;
        this.repeat = repeat;
        this.size = size;
    }

    public void onEnable(){ }
    public void onDisable(){}
    public double getValue(int positon){return -1;}
    public String getPlayer(int position){return "";}


    // Placeholders
    public void registerPlaceholders(double delay){
        for(int i = 1; i <= size; i++){
            int finalI = i;
            String placeholder = "{he-" + name + "-" + i + "-value}";
            HologramsAPI.registerPlaceholder(plugin, placeholder, delay, () -> {
                // TODO: Add balance formaters.
                if(type == ModuleType.NUMBER){
                    return TextUtils.formatValue(2, getValue(finalI));
                }else{
                    return TextUtils.formatTime(getValue(finalI) * 1000);
                }
            });
            placeholders.add(placeholder);
            String placeholder2 = "{he-" + name + "-" + i + "-user}";
            HologramsAPI.registerPlaceholder(plugin, placeholder2, delay, () -> getPlayer(finalI));
            placeholders.add(placeholder2);
        }
    }

    public void unregisterPlaceholders(){
        for(String placeholder : placeholders){
            HologramsAPI.unregisterPlaceholder(plugin, placeholder);
        }
    }

    // TODO: add heads and signs implementation


    // FEATURE: Maybe add NPC handler..

}
