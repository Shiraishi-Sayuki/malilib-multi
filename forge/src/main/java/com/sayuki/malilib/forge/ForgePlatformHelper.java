package com.sayuki.malilib.forge;

import com.sayuki.malilib.platform.IPlatformHelper;
import net.minecraftforge.fml.ModList;

// Forge用プラットフォーム実装 - ModListに処理を投げるだけの薄いラッパー、失敗しても"?"とかfalseで落とさない
public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public String getModVersion(String modId) {
        try {
            return ModList.get().getModContainerById(modId)
                    .map(container -> container.getModInfo().getVersion().toString())
                    .orElse("?");
        } catch (Throwable t) {
            return "?";
        }
    }

    @Override
    public boolean isModLoaded(String modId) {
        try {
            return ModList.get().isLoaded(modId);
        } catch (Throwable t) {
            return false;
        }
    }
}
