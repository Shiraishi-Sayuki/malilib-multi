package com.sayuki.malilib;

import fi.dy.masa.malilib.MaLiLibConfigs;
import fi.dy.masa.malilib.MaLiLibInitHandler;
import fi.dy.masa.malilib.data.MaLiLibTag;
import fi.dy.masa.malilib.event.InitializationHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// MaLiLib共通クラス - MOD全体を管理するメインクラス、FabricとNeoForgeどっちでも同じ初期化を呼ぶ
public class MaLiLibCommon {
    // ロガー - デバッグとかエラー出力で使う
    public static final Logger LOGGER = LogManager.getLogger("malilib");
    // MOD ID - どこでも使い回す定数
    public static final String MOD_ID = "malilib";
    // MOD名 - 表示用
    public static final String MOD_NAME = "MaLiLib";

    // 共通初期化 - どっちのローダーからもここを呼ぶ、設定とかイベント登録とかやる、コンフィグ画面は各ローダーが登録する
    public static void init() {
        try {
            LOGGER.info("MaLiLib共通初期化始めるよ - platform: {}", com.sayuki.malilib.platform.Services.PLATFORM.getPlatformName());
        } catch (Throwable t) {
            LOGGER.info("MaLiLib共通初期化始めるよ");
        }
        try {
            InitializationHandler.getInstance().registerInitializationHandler(new MaLiLibInitHandler());
        } catch (Throwable t) {
            LOGGER.error("Failed to register MaLiLibInitHandler", t);
        }
        try {
            MaLiLibTag.register();
        } catch (Throwable t) {
            LOGGER.error("MaLiLibTag.register failed", t);
        }
    }

    // デバッグログ - デバッグモードのときだけ出す、うるさくならないようにする
    public static void debugLog(String msg, Object... args) {
        try {
            if (fi.dy.masa.malilib.MaLiLibReference.DEBUG_MODE || MaLiLibConfigs.Debug.DEBUG_MESSAGES.getBooleanValue()) {
                LOGGER.info(msg, args);
            }
        } catch (Throwable t) {
            if (fi.dy.masa.malilib.MaLiLibReference.DEBUG_MODE) {
                LOGGER.info(msg, args);
            }
        }
    }
}
