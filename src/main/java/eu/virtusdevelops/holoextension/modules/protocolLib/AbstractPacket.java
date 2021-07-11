package eu.virtusdevelops.holoextension.modules.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPacket
{
    protected PacketContainer handle;

    protected AbstractPacket(final PacketContainer handle, final PacketType type) {
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle cannot be NULL (UNKNOWN).");
        }
        if (!Objects.equal((Object)handle.getType(), (Object)type)) {
            throw new IllegalArgumentException(handle.getHandle() + " is not a packet " + type);
        }
        this.handle = handle;
    }

    public PacketContainer getHandle() {
        return this.handle;
    }

    public void sendPacket(final Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.getHandle());
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }

    public void recievePacket(final Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, this.getHandle());
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot recieve packet. ERROR: ", e);
        }
    }
}
