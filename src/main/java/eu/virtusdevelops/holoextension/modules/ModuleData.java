package eu.virtusdevelops.holoextension.modules;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.utils.TextUtils;
import eu.virtusdevelops.virtuscore.VirtusCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ModuleData {
    private String name;
    private String placeholder;
    private ModuleDataType type;
    private long refresh;
    private int size;
    private boolean updateOffline;
    private String extensionName;
    private int format;


    public ModuleData(String name, String placeholder, String type, long refresh, int size, boolean updateOffline, String extensionName, int format){
        this.name = name;
        this.placeholder = placeholder;
        this.type = ModuleDataType.valueOf(type);
        this.refresh = refresh;
        this.size = size;
        this.updateOffline = updateOffline;
        this.extensionName = extensionName;
        this.format = format;
    }

    public ModuleData(String name, String placeholder, ModuleDataType type, long refresh, int size, boolean updateOffline, String extensionName, int format){
        this.name = name;
        this.placeholder = placeholder;
        this.type = type;
        this.refresh = refresh;
        this.size = size;
        this.updateOffline = updateOffline;
        this.extensionName = extensionName;
        this.format = format;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean isUpdateOffline() {
        return updateOffline;
    }

    public long getRefresh() {
        return refresh;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public ModuleDataType getType() {
        return type;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setRefresh(long refresh) {
        this.refresh = refresh;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(ModuleDataType type) {
        this.type = type;
    }

    public void setUpdateOffline(boolean updateOffline) {
        this.updateOffline = updateOffline;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getFormat() {
        return format;
    }
}

