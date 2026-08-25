package fi.dy.masa.malilib;

import fi.dy.masa.malilib.data.MaLiLibTag;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fi.dy.masa.malilib.event.InitializationHandler;
import com.sayuki.malilib.MaLiLibCommon;

// MaLiLibクラス - MOD全体を管理するメインクラス、共通初期化を呼ぶだけの薄いラッパー
public class MaLiLib
{
    public static final Logger LOGGER = LogManager.getLogger(MaLiLibReference.MOD_ID);

    // 共通初期化を呼ぶ - ローダー側からここを叩いてもらう
    public static void onInitialize() {
        MaLiLibCommon.init();
    }

    // 後方互換用 - 旧来の new MaLiLib().onInitialize() 形式でも動くように
    public void init() {
        onInitialize();
    }

    public static void debugLog(String key, Object... args)
    {
        try {
            if (MaLiLibReference.DEBUG_MODE || MaLiLibConfigs.Debug.DEBUG_MESSAGES.getBooleanValue())
            {
                LOGGER.info(key, args);
            }
        } catch (Throwable t) {
            if (MaLiLibReference.DEBUG_MODE) {
                LOGGER.info(key, args);
            }
        }
    }
}
