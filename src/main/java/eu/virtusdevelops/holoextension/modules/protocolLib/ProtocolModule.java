package eu.virtusdevelops.holoextension.modules.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleDataType;
import eu.virtusdevelops.holoextension.modules.ModuleType;

import java.util.Arrays;
import java.util.List;

public class ProtocolModule extends Module {
    private final HoloExtension plugin;
    private final long delay;

    public ProtocolModule(boolean updateOffline, String name, HoloExtension plugin, ModuleDataType type, ModuleType type1, long delay, long repeat, int size, boolean isEnabled, String callback) {
        super(updateOffline, name, plugin, type, type1, delay, repeat, size, isEnabled, callback,0);
        this.delay = delay;
        this.plugin = plugin;
    }


    @Override
    public void onEnable() {
        final PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(plugin).types(PacketType.Play.Server.ENTITY_METADATA);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketEditor(params));

        registerPlaceholders(delay);
        super.onEnable();
    }


    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);

        unregisterPlaceholders();
        super.onDisable();
    }

    @Override
    public void registerPlaceholders(double delay){
        final List<String> changingTextImitation = Arrays.asList("&8 &r", "&7 &r");

//        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
//
//        api.registerGlobalPlaceholder("fast", new TimedPlaceholder(2));
//        api.registerGlobalPlaceholder("medium", new TimedPlaceholder(20));
//        api.registerGlobalPlaceholder("slow", new TimedPlaceholder(200));




        HologramsAPI.registerPlaceholder(plugin, "fast", 0.1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(plugin, "medium", 1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(plugin, "slow", 10, new TimePlaceholdersUpdater(changingTextImitation));

    }

    @Override
    public void unregisterPlaceholders(){

//        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
//        api.unregisterPlaceholder("fast");
//        api.unregisterPlaceholder("medium");
//        api.unregisterPlaceholder("slow");


        HologramsAPI.unregisterPlaceholder(plugin, "fast");
        HologramsAPI.unregisterPlaceholder(plugin, "medium");
        HologramsAPI.unregisterPlaceholder(plugin, "slow");

    }
}
