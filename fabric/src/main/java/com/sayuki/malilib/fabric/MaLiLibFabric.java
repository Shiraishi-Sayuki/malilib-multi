package com.sayuki.malilib.fabric;

import net.fabricmc.api.ModInitializer;
import com.sayuki.malilib.MaLiLibCommon;

// Fabricエントリポイント - fabric.mod.jsonから呼ばれる、共通初期化(MaLiLibCommon)に橋渡しするだけ
public class MaLiLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MaLiLibCommon.init();
    }
}
