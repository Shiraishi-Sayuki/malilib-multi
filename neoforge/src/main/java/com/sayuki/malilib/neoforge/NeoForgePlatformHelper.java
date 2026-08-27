package com.sayuki.malilib.neoforge;

import com.sayuki.malilib.platform.IPlatformHelper;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// NeoForge用プラットフォームヘルパー - NeoForgeのAPI経由でパスとかバージョン取る
public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "neoforge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return net.neoforged.fml.ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return Boolean.getBoolean("neoforge.debugGamePath");
    }

    @Override
    public Path getGameDir() {
        return net.neoforged.fml.loading.FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigDir() {
        return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get();
    }

    @Override
    public String getModVersion(String modId) {
        return net.neoforged.fml.ModList.get().getModContainerById(modId)
                .map(c -> c.getModInfo().getVersion().toString())
                .orElse("?");
    }

    @Override
    public Map<String, String> getAllModVersions() {
        final HashMap<String, String> map = new HashMap<>();
        net.neoforged.fml.ModList.get().forEachModContainer((modId, container) ->
                map.put(modId, container.getModInfo().getVersion().toString())
        );
        return map;
    }
}
