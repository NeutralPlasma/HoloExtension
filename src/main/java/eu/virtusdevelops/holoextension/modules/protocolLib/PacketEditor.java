package eu.virtusdevelops.holoextension.modules.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import eu.virtusdevelops.virtuscore.compatibility.ServerVersion;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Hex;
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
            PacketContainer packet = event.getPacket();

            if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {


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
            if (!(customNameWatchableObjectValue instanceof Optional)) {
                return false;
            }
            final Optional<?> customNameOptional = (Optional<?>)customNameWatchableObjectValue;

            if (customNameOptional.isEmpty()) {
                return false;
            }
            final WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
            newName = componentWrapper.getJson();

        } else {
            newName = (String)customNameWatchableObjectValue;
        }

        newName = PlaceholderAPI.setPlaceholders(player, newName);
        //newName = HexUtil.colorify(newName, false);


        if (this.useOptional) {
            customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(newName).getHandle()));
        } else {
            customNameWatchableObject.setValue(newName);
        }
        return true;
    }
}
