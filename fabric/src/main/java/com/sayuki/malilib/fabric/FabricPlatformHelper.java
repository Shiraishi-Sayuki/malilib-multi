package com.sayuki.malilib.fabric;

import com.sayuki.malilib.platform.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// Fabric用プラットフォームヘルパー - FabricLoader経由でパスとかバージョン取る、実際の処理はここにまとめる
public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public String getModVersion(String modId) {
        for (net.fabricmc.loader.api.ModContainer container : FabricLoader.getInstance().getAllMods()) {
            if (container.getMetadata().getId().equals(modId)) {
                return container.getMetadata().getVersion().getFriendlyString();
            }
        }
        return "?";
    }

    @Override
    public Map<String, String> getAllModVersions() {
        final HashMap<String, String> map = new HashMap<>();
        FabricLoader.getInstance().getAllMods()
            .stream().toList()
            .forEach(mc -> {
                ModMetadata meta = mc.getMetadata();
                map.put(meta.getId(), meta.getVersion().getFriendlyString());
            });
        return map;
    }
}
