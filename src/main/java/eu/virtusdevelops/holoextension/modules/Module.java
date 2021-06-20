package eu.virtusdevelops.holoextension.modules;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.utils.TextUtils;
import eu.virtusdevelops.virtuscore.VirtusCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private BukkitRunnable task;
    private final String name;
    private long delay;
    private long repeat;
    private int size;
    private boolean isEnabled;
    private boolean updateOffline;
    private String noplayer;
    private HoloExtension plugin;
    private ModuleType type;



    private List<String> placeholders = new ArrayList<>();

    public Module(boolean updateOffline, String name, HoloExtension plugin, ModuleType type, long delay, long repeat, int size, boolean isEnabled, String callback){
        this.type = type;
        this.name = name;
        this.plugin = plugin;
        this.delay = delay;
        this.repeat = repeat;
        this.size = size;
        this.updateOffline = updateOffline;
        this.isEnabled = isEnabled;
        this.noplayer = callback;
    }

    public void onEnable(){
        VirtusCore.console().sendMessage("enable");
    }
    public void onDisable(){
        VirtusCore.console().sendMessage("Disable");
    }
    public void reload(){
        onDisable();
        onEnable();
        VirtusCore.console().sendMessage("Reload");
    }

    public BukkitRunnable getTask() {
        return task;
    }
    public void setTask(BukkitRunnable task) {
        this.task = task;
    }

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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isUpdateOffline() {
        return updateOffline;
    }

    public void setUpdateOffline(boolean updateOffline) {
        this.updateOffline = updateOffline;
    }

    public int getSize() {
        return size;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public long getDelay() {
        return delay;
    }

    public long getRepeat() {
        return repeat;
    }

    public ModuleType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNoplayer() {
        return noplayer;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setNoplayer(String noplayer) {
        this.noplayer = noplayer;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    public void setPlugin(HoloExtension plugin) {
        this.plugin = plugin;
    }

    public void setRepeat(long repeat) {
        this.repeat = repeat;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public void addPlaceholder(String placeholder){
        this.placeholders.add(placeholder);
    }

    // TODO: add heads and signs implementation


    // FEATURE: Maybe add NPC handler..

}
