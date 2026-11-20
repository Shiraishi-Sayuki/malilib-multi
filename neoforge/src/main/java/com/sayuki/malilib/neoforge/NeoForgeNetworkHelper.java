package com.sayuki.malilib.neoforge;

import com.sayuki.malilib.platform.INetworkHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
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
            Object target = registrar;

            // optional() を付けないと NeoForge はチャンネルを「必須」とみなし、
            // 該当 MOD の無いサーバーへ接続しただけで mod 不一致で切断される。
            // optional() は新しい registrar を返すので、以降はそのコピーに登録する。
            try {
                target = target.getClass().getMethod("optional").invoke(target);
            } catch (NoSuchMethodException ignored) {
                // optional() が無い古い NeoForge ではそのまま登録する
            }

            Class<?> regClass = target.getClass();
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
                    m.invoke(target, id, codec);
                    invoked = true;
                    break;
                }
            }
            if (!invoked && dummyHandler != null) {
                for (java.lang.reflect.Method m : regClass.getMethods()) {
                    if (m.getName().equals(methodName) && m.getParameterCount() == 3) {
                        m.invoke(target, id, codec, dummyHandler);
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
            ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

            // サーバーがこのチャンネルをネゴシエートしていない場合は送らない。
            // 送ってしまうと NeoForge が UnsupportedOperationException で切断する。
            if (handler == null || handler.hasChannel(payload.getId()) == false) {
                return false;
            }

            handler.send(payload);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRegistered(CustomPayload.Id<?> id) {
        return true;
    }
}
