package com.sayuki.malilib;

// 定数クラス - MODの基本定数を保持、MOD_IDとかバージョンとかまとめて置く
public class Constants {
    // MOD ID - どこからでも参照できるようにしとく
    public static final String MOD_ID = "malilib";
    // MOD名 - 表示名
    public static final String MOD_NAME = "MaLiLib";
    // 設定ファイル名とかで使うプレフィックス
    public static final String CONFIG_PREFIX = "malilib";
    // デバッグモード - 開発中だけtrueにする
    public static final boolean DEBUG = false;
    // 実験的機能フラグ - 試したい機能があるときだけtrue
    public static final boolean EXPERIMENTAL = false;

    // インスタンス化させない - 定数だけだから
    private Constants() {}
}
