package com.sayuki.malilib.forge;

import com.sayuki.malilib.MaLiLibCommon;
import fi.dy.masa.malilib.MaLiLibConfigGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkConstants;

// Forgeエントリポイント - mods.tomlから呼ばれる、クライアント限定で共通初期化と設定画面を登録する
@Mod(MaLiLibForge.MOD_ID)
public class MaLiLibForge {
    // MOD ID - mods.tomlのmodIdと合わせておく
    public static final String MOD_ID = "malilib";

    public MaLiLibForge() {
        // クライアント限定 - サーバー側では何もしない、ライブラリだから表示互換だけ確保する
        if (FMLLoader.getDist().isClient()) {
            ModContainer modContainer = ModLoadingContext.get().getActiveContainer();

            // サーバー無視マーク - このMODがサーバー側に無くても接続拒否にならないようにする
            modContainer.registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                    () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isLocal) -> true));

            // 共通初期化 - Fabric側と同じ流れでmasaのコードを呼ぶ
            MaLiLibCommon.init();

            // 設定画面登録 - ModMenuの代わりにForgeの拡張ポイントで出す
            modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> {
                        MaLiLibConfigGui gui = new MaLiLibConfigGui();
                        gui.setParent(parent);
                        return gui;
                    }));
        }
    }
}
