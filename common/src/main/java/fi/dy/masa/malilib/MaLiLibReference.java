package fi.dy.masa.malilib;

import java.nio.file.Path;

import com.sayuki.malilib.platform.Services;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    public static final String MC_VERSION = getMcVersionSafe();
    public static final int MC_DATA_VERSION = getMcDataVersionSafe();
    private static String getMcVersionSafe() {
        try { return net.minecraft.SharedConstants.getGameVersion().getId(); } catch (Throwable t) { return "1.21"; }
    }
    private static int getMcDataVersionSafe() {
        try { return net.minecraft.SharedConstants.getGameVersion().getSaveVersion().getId(); } catch (Throwable t) { return 4435; }
    }
    // バージョン取得 - プラットフォームヘルパー経由でやる、後で初期化されるから遅延取得にする
    public static String getModVersion() { return Services.PLATFORM.getModVersion(MOD_ID); }
    public static final String MOD_VERSION = getVersionSafe();
    private static String getVersionSafe() {
        try { return Services.PLATFORM.getModVersion(MOD_ID); } catch (Exception e) { return "?"; }
    }
    // ゲームディレクトリ - 起動時のパスをヘルパーから取る
    public static Path getGameDir() { return Services.PLATFORM.getGameDir(); }
    public static Path getConfigDir() { return Services.PLATFORM.getConfigDir(); }
    // 後方互換用に直接アクセスも残すけど、中身はヘルパー経由
    public static final Path GAME_DIR = getGameDirSafe();
    public static final Path CONFIG_DIR = getConfigDirSafe();
    private static Path getGameDirSafe() {
        try { return Services.PLATFORM.getGameDir(); } catch (Exception e) { return Path.of("."); }
    }
    private static Path getConfigDirSafe() {
        try { return Services.PLATFORM.getConfigDir(); } catch (Exception e) { return Path.of("config"); }
    }
    public static final boolean DEBUG_MODE = false;
    public static final boolean ANSI_MODE = DEBUG_MODE;
    public static final boolean EXPERIMENTAL_MODE = false;
}
