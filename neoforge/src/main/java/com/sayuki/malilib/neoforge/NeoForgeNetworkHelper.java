package com.sayuki.malilib.neoforge;

import com.sayuki.malilib.platform.INetworkHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.util.HashSet;
import java.util.Set;

// NeoForge用ネットワークヘルパー - NeoForgeのPayloadRegistrar経由でパケット登録する
public class NeoForgeNetworkHelper implements INetworkHelper {
    private final Set<CustomPacketPayload.Type<?>> registered = new HashSet<>();
    private static Object registrar;

    public static void setRegistrar(Object reg) {
        registrar = reg;
    }

    @Override
    public <T extends CustomPacketPayload> void registerPayload(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, boolean toServer, boolean toClient) {
        if (registrar == null) {
            return;
        }
        try {
            Class<?> regClass = registrar.getClass();
            String methodName;
            if (toServer && toClient) methodName = "playBidirectional";
            else if (toServer) methodName = "playToServer";
            else methodName = "playToClient";
            for (java.lang.reflect.Method m : regClass.getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == 2) {
                    m.invoke(registrar, id, codec);
                    registered.add(id);
                    return;
                }
            }
        } catch (Exception e) {
            // log error
        }
    }

    @Override
    public <T extends CustomPacketPayload> boolean sendToServer(T payload) {
        try {
            Class<?> distributorClass = Class.forName("net.neoforged.neoforge.network.PacketDistributor");
            Class<?> payloadClass = Class.forName("net.minecraft.network.protocol.common.custom.CustomPacketPayload");
            distributorClass.getMethod("sendToServer", payloadClass).invoke(null, payload);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRegistered(CustomPacketPayload.Type<?> id) {
        return registered.contains(id);
    }
}
