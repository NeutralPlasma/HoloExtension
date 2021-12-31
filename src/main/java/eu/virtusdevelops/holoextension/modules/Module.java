package eu.virtusdevelops.holoextension.modules;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.utils.TextUtils;
import eu.virtusdevelops.virtuscore.VirtusCore;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.internal.HolographicDisplaysAPIProvider;
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
    private ModuleDataType type;
    private ModuleType moduleType;
    private int format;



    private List<String> placeholders = new ArrayList<>();

    public Module(boolean updateOffline, String name, HoloExtension plugin, ModuleDataType type, ModuleType type1, long delay, long repeat, int size, boolean isEnabled, String callback, int format){
        this.type = type;
        this.name = name;
        this.plugin = plugin;
        this.delay = delay;
        this.repeat = repeat;
        this.size = size;
        this.updateOffline = updateOffline;
        this.isEnabled = isEnabled;
        this.noplayer = callback;
        this.moduleType = type1;
        this.format = format;
    }

    public void onEnable(){
        VirtusCore.console().sendMessage(eu.virtusdevelops.virtuscore.utils.TextUtils.colorFormat("&8[&bHE&8] &aEnabling " + getName()));
    }
    public void onDisable(){
        VirtusCore.console().sendMessage(eu.virtusdevelops.virtuscore.utils.TextUtils.colorFormat("&8[&bHE&8] &cDisabling " + getName()));
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

    public String getValueFormated(int positon){return "-1";}
    public String getPlayer(int position){return "";}


    // Placeholders
    public void registerPlaceholders(double delay){
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        String tempname = name.replace("%", "");

        for(int i = 1; i <= size; i++){
            int finalI = i;
            String placeholder = "he-" + tempname + "-" + i + "-value";

//            HologramsAPI.registerPlaceholder(plugin, placeholder, delay, () -> {
//                // TODO: Add balance formaters.
//                if(type == ModuleDataType.NUMBER){
//                    return TextUtils.formatValue(format, getValue(finalI));
//                }else{
//                    return TextUtils.formatTime(getValue(finalI) * 1000);
//                }
//            });

            api.registerGlobalPlaceholder(placeholder, (int) delay * 20, (da) -> {
                if(type == ModuleDataType.NUMBER){
                    return TextUtils.formatValue(format, getValue(finalI));
                }else{
                    return TextUtils.formatTime(getValue(finalI) * 1000);
                }
            });


            placeholders.add(placeholder);
            String placeholder2 = "he-" + tempname + "-" + i + "-user";

            api.registerGlobalPlaceholder(placeholder2, (int) delay * 20, (da) -> getPlayer(finalI));

//            HologramsAPI.registerPlaceholder(plugin, placeholder2, delay, () -> getPlayer(finalI));

            placeholders.add(placeholder2);
        }
    }

    public void unregisterPlaceholders(){
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        for(String placeholder : placeholders){
            api.unregisterPlaceholder(placeholder);
//            HologramsAPI.unregisterPlaceholder(plugin, placeholder);
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

    public ModuleDataType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNoplayer() {
        return noplayer;
    }

    public ModuleType getModuleType() {
        return moduleType;
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

    public void setType(ModuleDataType type) {
        this.type = type;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void addPlaceholder(String placeholder){
        this.placeholders.add(placeholder);
    }

    // TODO: add heads and signs implementation


    // FEATURE: Maybe add NPC handler..

}
