package com.sayuki.malilib.neoforge;

import com.sayuki.malilib.platform.INetworkHelper;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

// NeoForge用ネットワークヘルパー - リフレクションでPayloadRegistrarを扱いYarn/MojMap差異を吸収
public class NeoForgeNetworkHelper implements INetworkHelper {
    private static Object registrar;

    public static void setRegistrar(Object reg) {
        registrar = reg;
    }

    @Override
    public <T extends CustomPayload> void registerPayload(CustomPayload.Id<T> id, PacketCodec<? super RegistryByteBuf, T> codec, boolean toServer, boolean toClient) {
        if (registrar == null) {
            System.err.println("NeoForgeNetworkHelper: registrar not set for " + id.id());
            return;
        }
        try {
            Class<?> regClass = registrar.getClass();
            Object dummyHandler = createDummyHandler();
            String methodName;
            if (toServer && toClient) methodName = "playBidirectional";
            else if (toServer) methodName = "playToServer";
            else if (toClient) methodName = "playToClient";
            else methodName = "playBidirectional";
            // Try 2-arg version first
            boolean invoked = false;
            for (java.lang.reflect.Method m : regClass.getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == 2) {
                    m.invoke(registrar, id, codec);
                    invoked = true;
                    break;
                }
            }
            if (!invoked && dummyHandler != null) {
                for (java.lang.reflect.Method m : regClass.getMethods()) {
                    if (m.getName().equals(methodName) && m.getParameterCount() == 3) {
                        m.invoke(registrar, id, codec, dummyHandler);
                        invoked = true;
                        break;
                    }
                }
            }
            if (!invoked) {
                System.err.println("NeoForgeNetworkHelper: no suitable method found for " + methodName);
            }
        } catch (Exception e) {
            System.err.println("NeoForgeNetworkHelper: payload登録失敗 " + id.id() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Object createDummyHandler() {
        try {
            Class<?> handlerClass = Class.forName("net.neoforged.neoforge.network.handling.IPayloadHandler");
            return java.lang.reflect.Proxy.newProxyInstance(handlerClass.getClassLoader(), new Class[]{handlerClass}, (proxy, method, args) -> null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T extends CustomPayload> boolean sendToServer(T payload) {
        try {
            Class<?> distributorClass = Class.forName("net.neoforged.neoforge.network.PacketDistributor");
            distributorClass.getMethod("sendToServer", CustomPayload.class).invoke(null, payload);
            return true;
        } catch (Exception e1) {
            try {
                // Try MojMap name
                Class<?> distributorClass = Class.forName("net.neoforged.neoforge.network.PacketDistributor");
                Class<?> payloadClass = Class.forName("net.minecraft.network.protocol.common.custom.CustomPacketPayload");
                distributorClass.getMethod("sendToServer", payloadClass).invoke(null, payload);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    @Override
    public boolean isRegistered(CustomPayload.Id<?> id) {
        return true;
    }
}
