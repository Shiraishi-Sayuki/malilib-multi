package com.sayuki.malilib.fabric;

import com.sayuki.malilib.platform.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

// Fabric用プラットフォーム実装 - FabricLoaderに処理を投げるだけの薄いラッパー
public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public String getModVersion(String modId) {
        try {
            return FabricLoader.getInstance().getModContainer(modId)
                    .map(container -> container.getMetadata().getVersion().getFriendlyString())
                    .orElse("?");
        } catch (Throwable t) {
            return "?";
        }
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
