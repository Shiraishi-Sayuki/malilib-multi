package fi.dy.masa.malilib.network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.MaLiLib;
import com.sayuki.malilib.platform.Services;

// ネットワークハンドラ抽象 - FabricとNeoForgeどっちでも同じインターフェースで扱えるようにする
public interface IPluginClientPlayHandler<T extends CustomPayload>
{
    int FROM_SERVER = 1;
    int TO_SERVER = 2;
    int BOTH_SERVER = 3;
    int TO_CLIENT = 4;
    int FROM_CLIENT = 5;
    int BOTH_CLIENT = 6;

    Identifier getPayloadChannel();
    boolean isPlayRegistered(Identifier channel);
    void setPlayRegistered(Identifier channel);
    void reset(Identifier channel);

    // ペイロード登録 - プラットフォームヘルパーに丸投げ、どっちのローダーでも同じ呼び出しでOK
    default void registerPlayPayload(@Nonnull CustomPayload.Id<T> id, @Nonnull PacketCodec<? super RegistryByteBuf,T> codec, int direction)
    {
        if (this.isPlayRegistered(this.getPayloadChannel()) == false)
        {
            try
            {
                boolean toServer = direction == TO_SERVER || direction == FROM_CLIENT || direction == BOTH_SERVER || direction == BOTH_CLIENT;
                boolean toClient = direction == FROM_SERVER || direction == TO_CLIENT || direction == BOTH_SERVER || direction == BOTH_CLIENT;
                // INetworkHelperに登録を委譲 - 中でPayloadTypeRegistryとかNeoForgeの登録やる
                Services.NETWORK.registerPayload(id, codec, toServer, toClient);
            }
            catch (IllegalArgumentException e)
            {
                MaLiLib.LOGGER.error("registerPlayPayload: channel ID [{}] is is already registered", this.getPayloadChannel());
            }

            this.setPlayRegistered(this.getPayloadChannel());
            return;
        }

        MaLiLib.LOGGER.error("registerPlayPayload: channel ID [{}] is invalid, or it is already registered", this.getPayloadChannel());
    }

    // レシーバー登録 - ヘルパー経由じゃなくて、直接やる必要あるかもだけど、とりあえず共通処理
    default boolean registerPlayReceiver(@Nonnull CustomPayload.Id<T> id, @Nullable Object receiver)
    {
        if (this.isPlayRegistered(this.getPayloadChannel()))
        {
            try
            {
                // プラットフォーム側で登録 - FabricはClientPlayNetworking、NeoForgeはevent busとか
                // ここでは仮でtrue返す、実際は各プラットフォームのヘルパーがやる
                // 下流MODが自分でregisterGlobalReceiver呼ぶ形でもOK
                return true;
            }
            catch (IllegalArgumentException e)
            {
                MaLiLib.LOGGER.error("registerPlayReceiver: Channel ID [{}] payload has not been registered", this.getPayloadChannel());
                return false;
            }
        }

        MaLiLib.LOGGER.error("registerPlayReceiver: Channel ID [{}] is invalid, or not registered", this.getPayloadChannel());
        return false;
    }

    default void unregisterPlayReceiver()
    {
        // プラットフォーム側で解除 - 必要ならServices.NETWORKに委譲
    }

    // ペイロード受信 - コンテキスト付き、FabricのContextとかNeoForgeのIPayloadContextとか抽象化したObjectで受ける
    default void receivePlayPayload(T payload, Object context) {}

    // レガシー受信用 - Mixinから直接呼ばれるやつ、ハンドラとCallbackInfo渡される
    default void receivePlayPayload(T payload, ClientPlayNetworkHandler handler, CallbackInfo ci) {}

    default void decodeNbtCompound(Identifier channel, NbtCompound data) {}
    default void decodeByteBuf(Identifier channel, MaLiLibBuf data) {}
    default <D> void decodeObject(Identifier channel, D data1) {}
    default <P extends IClientPayloadData> void decodeClientData(Identifier channel, P data) {}

    default void encodeNbtCompound(NbtCompound data) {}
    default void encodeByteBuf(MaLiLibBuf data) {}
    default <D> void encodeObject(D data1) {}
    default <P extends IClientPayloadData> void encodeClientData(P data) {}

    void encodeWithSplitter(PacketByteBuf buf, ClientPlayNetworkHandler handler);

    // ペイロード送信 - 共通ヘルパー経由で送る、FabricならClientPlayNetworking、NeoForgeならPacketDistributor
    default boolean sendPlayPayload(@Nonnull T payload)
    {
        if (payload.getId().id().equals(this.getPayloadChannel()) && this.isPlayRegistered(this.getPayloadChannel()))
        {
            return Services.NETWORK.sendToServer(payload);
        }
        else
        {
            MaLiLib.LOGGER.warn("sendPlayPayload: [NetworkHelper] error sending payload for channel: {}, check if channel is registered", payload.getId().id().toString());
        }

        return false;
    }

    default boolean sendPlayPayload(@Nonnull ClientPlayNetworkHandler handler, @Nonnull T payload)
    {
        if (payload.getId().id().equals(this.getPayloadChannel()) && this.isPlayRegistered(this.getPayloadChannel()))
        {
            Packet<?> packet = new CustomPayloadC2SPacket(payload);

            if (handler.accepts(packet))
            {
                // Yarn: sendPacket, Official/MojMap: send - resolve reflectively for cross-loader compatibility
                try
                {
                    try
                    {
                        handler.getClass().getMethod("sendPacket", Packet.class).invoke(handler, packet);
                    }
                    catch (NoSuchMethodException e)
                    {
                        handler.getClass().getMethod("send", Packet.class).invoke(handler, packet);
                    }
                }
                catch (ReflectiveOperationException e)
                {
                    MaLiLib.LOGGER.warn("sendPlayPayload: Failed to send packet via handler", e);
                    return false;
                }

                return true;
            }
        }
        else
        {
            MaLiLib.LOGGER.warn("sendPlayPayload: [NetworkHandler] error sending payload for channel: {}, check if channel is registered", payload.getId().id().toString());
        }

        return false;
    }
}
