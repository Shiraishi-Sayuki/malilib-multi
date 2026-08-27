package com.sayuki.malilib.fabric;

import com.sayuki.malilib.platform.INetworkHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.util.HashSet;
import java.util.Set;

// Fabric用ネットワークヘルパー - PayloadTypeRegistry経由でパケット登録する
public class FabricNetworkHelper implements INetworkHelper {
    private final Set<CustomPacketPayload.Type<?>> registered = new HashSet<>();

    @Override
    public <T extends CustomPacketPayload> void registerPayload(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, boolean toServer, boolean toClient) {
        if (toServer) {
            PayloadTypeRegistry.serverboundPlay().register(id, codec);
        }
        if (toClient) {
            PayloadTypeRegistry.clientboundPlay().register(id, codec);
        }
        registered.add(id);
    }

    @Override
    public <T extends CustomPacketPayload> boolean sendToServer(T payload) {
        // Fabric用の送信処理 - ClientPlayNetworking経由で送る
        // マルチローダー対応のため、ここではSimplePacketPayloadSenderを使う
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
        return true;
    }

    @Override
    public boolean isRegistered(CustomPacketPayload.Type<?> id) {
        return registered.contains(id);
    }
}
