package com.sayuki.malilib.fabric;

import com.sayuki.malilib.MaLiLibCommon;
import fi.dy.masa.malilib.MaLiLibConfigGui;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.fabricmc.api.ModInitializer;

// Fabric用エントリーポイント - fabric.mod.jsonから呼ばれる、共通初期化に丸投げする
public class MaLiLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // 共通初期化を呼ぶ - ローダーに依存しない処理は全部ここでやる
        MaLiLibCommon.init();
        // コンフィグ画面登録 - ModMenuで開くやつ、Registryに登録する
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new ModInfo(MaLiLibCommon.MOD_ID, MaLiLibCommon.MOD_NAME, MaLiLibConfigGui::new)
        );
    }
}
