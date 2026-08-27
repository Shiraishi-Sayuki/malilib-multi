package com.sayuki.malilib.fabric;

import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.api.ModInitializer;

// Fabric用エントリーポイント - fabric.mod.jsonから呼ばれる、共通初期化に丸投げする
public class MaLiLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // 共通初期化を呼ぶ - ローダーに依存しない処理は全部ここでやる
        MaLiLib.init();
    }
}
