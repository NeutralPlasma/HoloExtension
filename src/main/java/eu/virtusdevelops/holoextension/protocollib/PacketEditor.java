package eu.virtusdevelops.holoextension.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import eu.virtusdevelops.virtuscore.compatibility.ServerVersion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;


public class PacketEditor extends PacketAdapter{
    private boolean useOptional;

    public PacketEditor(final AdapterParameteters params){
        super(params);

        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
            this.useOptional = true;
        }
    }

    @Override
    @SuppressWarnings("all")
    public void onPacketSending(PacketEvent event) {
        try {
            if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                PacketContainer packet = event.getPacket().deepClone();
                final WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
                final List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getMetadata();
                for (final WrappedWatchableObject watchableObject : dataWatcherValues) {
                    if (watchableObject.getIndex() == 2) {
                        if (updateText(watchableObject, event.getPlayer())) {
                            event.setPacket(entityMetadataPacket.getHandle());
                        }
                    }
                }
            }
        }catch (Exception error){

        }
    }

    public boolean updateText(WrappedWatchableObject customNameWatchableObject, Player player){
        if (customNameWatchableObject == null) {
            return true;
        }
        final Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
        String newName;
        if (this.useOptional) {
            if (!(customNameWatchableObjectValue instanceof final Optional<?> customNameOptional)) {
                return false;
            }
            if (customNameOptional.isEmpty()) {
                return false;
            }
            final WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
            newName = componentWrapper.getJson();
        } else {
            newName = (String)customNameWatchableObjectValue;
        }
        newName = PlaceholderAPI.setPlaceholders(player, newName);

        if (this.useOptional) {
            customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(newName).getHandle()));
        } else {
            customNameWatchableObject.setValue(newName);
        }
        return true;
    }
}