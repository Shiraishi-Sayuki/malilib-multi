package com.sayuki.malilib.fabric;

import com.sayuki.malilib.platform.INetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

// Fabric用ネットワークヘルパー - FabricのPayloadTypeRegistryとClientPlayNetworkingでパケット登録とかやる
public class FabricNetworkHelper implements INetworkHelper {
    @Override
    public <T extends CustomPayload> void registerPayload(CustomPayload.Id<T> id, PacketCodec<? super RegistryByteBuf, T> codec, boolean toServer, boolean toClient) {
        // Fabricは方向別に登録する必要がある、両方向なら両方登録する
        if (toServer && toClient) {
            PayloadTypeRegistry.playC2S().register(id, codec);
            PayloadTypeRegistry.playS2C().register(id, codec);
        } else if (toServer) {
            PayloadTypeRegistry.playC2S().register(id, codec);
        } else if (toClient) {
            PayloadTypeRegistry.playS2C().register(id, codec);
        } else {
            // デフォルトは両方
            PayloadTypeRegistry.playC2S().register(id, codec);
            PayloadTypeRegistry.playS2C().register(id, codec);
        }
    }

    @Override
    public <T extends CustomPayload> boolean sendToServer(T payload) {
        if (ClientPlayNetworking.canSend(payload.getId())) {
            ClientPlayNetworking.send(payload);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(CustomPayload.Id<?> id) {
        // Fabricでは直接チェックするAPIがないから、とりあえずtrue返す
        // 実際には登録済みかは呼び出し側で管理する
        return true;
    }
}
