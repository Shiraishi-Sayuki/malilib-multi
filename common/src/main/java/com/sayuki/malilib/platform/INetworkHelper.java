package com.sayuki.malilib.platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

// ネットワークヘルパー - FabricとNeoForgeでパケット登録の仕方が違うのをまとめる
public interface INetworkHelper {
    // ペイロードを登録する - チャンネルIDとCodec渡して、送受信できるようにする
    <T extends CustomPacketPayload> void registerPayload(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, boolean toServer, boolean toClient);

    // サーバーにパケット送る - クライアントからサーバーへデータ送るときに使う
    <T extends CustomPacketPayload> boolean sendToServer(T payload);

    // 登録済みかチェック - 二重登録しないように確認する
    boolean isRegistered(CustomPacketPayload.Type<?> id);
}
