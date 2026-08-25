package com.sayuki.malilib.platform;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

// ネットワークヘルパー - FabricとNeoForgeでパケット登録の仕方が違うのをまとめる
public interface INetworkHelper {
    // ペイロードを登録する - チャンネルIDとCodec渡して、送受信できるようにする
    <T extends CustomPayload> void registerPayload(CustomPayload.Id<T> id, PacketCodec<? super RegistryByteBuf, T> codec, boolean toServer, boolean toClient);

    // サーバーにパケット送る - クライアントからサーバーへデータ送るときに使う
    <T extends CustomPayload> boolean sendToServer(T payload);

    // 登録済みかチェック - 二重登録しないように確認する
    boolean isRegistered(CustomPayload.Id<?> id);
}
