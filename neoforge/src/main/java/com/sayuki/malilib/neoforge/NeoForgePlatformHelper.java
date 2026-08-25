package com.sayuki.malilib.neoforge;

import com.sayuki.malilib.platform.IPlatformHelper;
import java.nio.file.Path;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

// NeoForge用プラットフォームヘルパー
public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "neoforge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        try {
            return ModList.get().isLoaded(modId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        try {
            return !FMLLoader.isProduction();
        } catch (Exception e) {
            return Boolean.getBoolean("neoforge.development");
        }
    }

    @Override
    public Path getGameDir() {
        try {
            return FMLPaths.GAMEDIR.get();
        } catch (Exception e) {
            return Path.of(".");
        }
    }

    @Override
    public Path getConfigDir() {
        try {
            return FMLPaths.CONFIGDIR.get();
        } catch (Exception e) {
            return Path.of("config");
        }
    }

    @Override
    public String getModVersion(String modId) {
        try {
            return ModList.get().getModContainerById(modId)
                    .map(c -> c.getModInfo().getVersion().toString())
                    .orElse("?");
        } catch (Exception e) {
            return "?";
        }
    }
}
