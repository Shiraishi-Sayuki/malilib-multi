package com.sayuki.malilib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fi.dy.masa.malilib.MaLiLibInitHandler;
import fi.dy.masa.malilib.event.InitializationHandler;

// MaLiLib共通クラス - MOD全体を管理するメインクラス、FabricとForgeどっちでも同じ初期化を呼ぶ
// multi/1.21と同じ構造に合わせてある
public class MaLiLibCommon {
    // ロガー - デバッグとかエラー出力で使う
    public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_ID);
    // MOD ID - どこでも使い回す定数
    public static final String MOD_ID = Constants.MOD_ID;
    // MOD名 - 表示用
    public static final String MOD_NAME = Constants.MOD_NAME;

    // 共通初期化 - どっちのローダーからもここを呼ぶ、設定とかイベント登録とかやる、設定画面は各ローダーが登録する
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
    }
}
