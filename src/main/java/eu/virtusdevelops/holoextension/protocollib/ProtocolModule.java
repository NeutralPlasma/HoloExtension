package eu.virtusdevelops.holoextension.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.virtusdevelops.holoextension.HoloExtension;

import java.util.Arrays;
import java.util.List;

public class ProtocolModule {

    private HoloExtension plugin;

    public ProtocolModule(HoloExtension plugin){
        this.plugin = plugin;
    }

    public void onEnable() {
        final PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(plugin).types(PacketType.Play.Server.ENTITY_METADATA);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketEditor(params));

        registerPlaceholders();

    }

    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);

        unregisterPlaceholders();
    }

    private void registerPlaceholders(){
        final List<String> changingTextImitation = Arrays.asList("&8 &r", "&7 &r");


        HologramsAPI.registerPlaceholder(plugin, "{fast}", 0.1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(plugin, "{medium}", 1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(plugin, "{slow}", 10, new TimePlaceholdersUpdater(changingTextImitation));

    }

    private void unregisterPlaceholders(){

        HologramsAPI.unregisterPlaceholder(plugin, "{fast}");
        HologramsAPI.unregisterPlaceholder(plugin, "{medium}");
        HologramsAPI.unregisterPlaceholder(plugin, "{slow}");

    }
}
