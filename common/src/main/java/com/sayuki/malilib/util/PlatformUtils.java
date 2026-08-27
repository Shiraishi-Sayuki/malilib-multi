package com.sayuki.malilib.util;

import com.sayuki.malilib.platform.Services;
import java.nio.file.Path;

// プラットフォームユーティリティ - 今どのローダーで動いてるか判定したり、パス取得を楽にする
public class PlatformUtils {
    // Fabricで動いてるかチェック - Fabric特有の処理するときに使う
    public static boolean isFabric() {
        return Services.PLATFORM.getPlatformName().equalsIgnoreCase("fabric");
    }

    // NeoForgeで動いてるかチェック - NeoForge特有の処理するときに使う
    public static boolean isNeoForge() {
        return Services.PLATFORM.getPlatformName().equalsIgnoreCase("neoforge");
    }

    // 開発環境かチェック - デバッグ表示とかテストコマンド出すか決める
    public static boolean isDevelopmentEnvironment() {
        return Services.PLATFORM.isDevelopmentEnvironment();
    }

    // ゲームディレクトリ取得 - ワールドデータとかある場所
    public static Path getGameDir() {
        return Services.PLATFORM.getGameDir();
    }

    // コンフィグディレクトリ取得 - 設定ファイル置き場
    public static Path getConfigDir() {
        return Services.PLATFORM.getConfigDir();
    }

    // MODバージョン取得 - 表示とか互換性チェックで使う
    public static String getModVersion(String modId) {
        return Services.PLATFORM.getModVersion(modId);
    }
}
