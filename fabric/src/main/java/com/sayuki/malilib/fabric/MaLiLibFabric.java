package com.sayuki.malilib.fabric;

import net.fabricmc.api.ModInitializer;
import fi.dy.masa.malilib.MaLiLib;

// Fabricエントリポイント - fabric.mod.jsonから呼ばれる、共通初期化に橋渡しするだけ
public class MaLiLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MaLiLib.onInitialize();
    }
}
