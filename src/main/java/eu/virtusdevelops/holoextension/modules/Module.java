package eu.virtusdevelops.holoextension.modules;

import eu.virtusdevelops.holoextension.HoloExtension;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Module extends BukkitRunnable {

    public Module(boolean updateOffline){}

    public void onEnable(HoloExtension plugin, long delay, long repeat){}
    public void onDisable(){}
    public double getValue(int positon){return -1;}
    public String getPlayer(int position){return "";}

    public void registerPlaceholders(){}
}
