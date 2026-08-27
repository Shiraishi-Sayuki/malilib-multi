package fi.dy.masa.malilib.network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fi.dy.masa.malilib.MaLiLib;
import com.sayuki.malilib.platform.Services;

/**
 * Interface for ClientPlayHandler, for downstream mods.
 * @param <T> (Payload)
 */
public interface IPluginClientPlayHandler<T extends CustomPacketPayload>
{
    int FROM_SERVER = 1;
    int TO_SERVER = 2;
    int BOTH_SERVER = 3;
    int TO_CLIENT = 4;
    int FROM_CLIENT = 5;
    int BOTH_CLIENT = 6;
    int MAX_FAILURES = 2;

    /**
     * Returns your HANDLER's CHANNEL ID
     * @return (Channel ID)
     */
    Identifier getPayloadChannel();

    /**
     * Returns if your Channel ID has been registered to your Play Payload.
     * @param channel (Your Channel ID)
     * @return (true / false)
     */
    boolean isPlayRegistered(Identifier channel);

    /**
     * Sets your HANDLER as registered.
     * @param channel (Your Channel ID)
     */
    void setPlayRegistered(Identifier channel);

    /**
     * Send your HANDLER a global reset() event, such as when the client is shutting down, or logging out.
     * @param channel (Your Channel ID)
     */
    void reset(Identifier channel);

    /**
     * Register your Payload with the platform networking system.
     * @param id (Your Payload Id<T>)
     * @param codec (Your Payload's CODEC)
     * @param direction (Payload Direction)
     */
    default void registerPlayPayload(@Nonnull CustomPacketPayload.Type<T> id, @Nonnull StreamCodec<? super RegistryFriendlyByteBuf, T> codec, int direction)
    {
        if (!this.isPlayRegistered(this.getPayloadChannel()))
        {
            try
            {
                boolean toServer = (direction == TO_SERVER || direction == BOTH_SERVER || direction == FROM_CLIENT);
                boolean toClient = (direction == FROM_SERVER || direction == BOTH_CLIENT || direction == TO_CLIENT);
                Services.NETWORK.registerPayload(id, codec, toServer, toClient);
            }
            catch (Exception e)
            {
                MaLiLib.LOGGER.error("registerPlayPayload: channel ID [{}] is already registered", this.getPayloadChannel());
            }

            this.setPlayRegistered(this.getPayloadChannel());
            return;
        }

        MaLiLib.LOGGER.error("registerPlayPayload: channel ID [{}] is invalid, or it is already registered", this.getPayloadChannel());
    }

    /**
     * Unregisters your Packet Receiver function.
     */
    default void unregisterPlayReceiver()
    {
        // Platform-specific implementation in fabric/neoforge modules
    }

    /**
     * Receive Payload by pointing static receive() method to this to convert Payload to its data decode() function.
     * @param payload (Payload to decode)
     * @param handler (Client Packet Listener)
     */
    void receivePlayPayload(T payload, ClientPacketListener handler);

    /**
     * Receive Payload via the legacy "onCustomPayload" from a Network Handler Mixin interface.
     * @param payload (Payload to decode)
     * @param handler (Network Handler that received the data)
     * @param ci (Callbackinfo for sending ci.cancel(), if wanted)
     */
    default void receivePlayPayload(T payload, ClientPacketListener handler, CallbackInfo ci) {}

    /**
     * Payload Decoder wrapper function.
     * @param channel (Channel)
     * @param data (Data Codec)
     */
    default <P extends IClientPayloadData> void decodeClientData(Identifier channel, P data) {}

    /**
     * Payload Encoder wrapper function.
     * @param data (Data Codec)
     */
    default <P extends IClientPayloadData> void encodeClientData(P data) {}

    /**
     * Used as an iterative "wrapper" for Payload Splitter to send individual Packets
     * @param buf (Sliced Buffer to send)
     * @param handler (Network Handler as a fail-over option)
     */
    void encodeWithSplitter(FriendlyByteBuf buf, ClientPacketListener handler);

    /**
     * Sends the Payload to the server using the platform networking system.
     * @param payload (The Payload to send)
     * @return (true/false --> for error control)
     */
    default boolean sendPlayPayload(@Nonnull T payload)
    {
        if (payload.type().id().equals(this.getPayloadChannel()) &&
            this.isPlayRegistered(this.getPayloadChannel()) &&
            this.checkFailures())
        {
            return Services.NETWORK.sendToServer(payload);
        }
        else
        {
            MaLiLib.LOGGER.warn("sendPlayPayload: error sending payload for channel: {}, check if channel is registered", payload.type().id().toString());
        }

        return false;
    }

    /**
     * Sends the Payload to the player using the ClientPlayNetworkHandler interface.
     * @param handler (ClientPlayNetworkHandler)
     * @param payload (The Payload to send)
     * @return (true/false --> for error control)
     */
    default boolean sendPlayPayload(@Nonnull ClientPacketListener handler, @Nonnull T payload)
    {
        if (payload.type().id().equals(this.getPayloadChannel()) &&
            this.isPlayRegistered(this.getPayloadChannel()) &&
            this.checkFailures())
        {
            Packet<?> packet = new ServerboundCustomPayloadPacket(payload);

            if (handler.shouldHandleMessage(packet))
            {
                handler.send(packet);
                return true;
            }
        }
        else
        {
            MaLiLib.LOGGER.warn("sendPlayPayload: [NetworkHandler] error sending payload for channel: {}, check if channel is registered", payload.type().id().toString());
        }

        return false;
    }

    /**
     * Max Failures
     * @return -
     */
    default int maxFailures() { return MAX_FAILURES; }

    /**
     * Tick the Failure Counter
     */
    void tickFailures();

    /**
     * Return if it is safe to proceed processing packets.
     * @return True for safe; False for unsafe.
     */
    boolean checkFailures();
}
