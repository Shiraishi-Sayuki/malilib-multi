package com.sayuki.malilib.platform;

import java.nio.file.Path;

// プラットフォーム抽象化インターフェース - ローダー固有の処理を共通コードから隠すための窓口
// multi/1.21と同じAPI形状に合わせてある
public interface IPlatformHelper {
    // プラットフォーム名 - "Fabric"とか"Forge"とか返す、ログ出力用
    String getPlatformName();

    // MODロード確認 - 指定modIdが読み込まれてるかどうか
    boolean isModLoaded(String modId);

    // 開発環境かどうか判定 - デバッグ機能を有効にするか決めるときに使う
    boolean isDevelopmentEnvironment();

    // ゲームディレクトリ取得 - ワールドとかリソース置き場の基準になる
    Path getGameDir();

    // コンフィグディレクトリ取得 - 設定ファイル置き場
    Path getConfigDir();

    // MODバージョン取得 - 指定modIdのバージョン文字列を返す、見つからなければ"?"を返す
    String getModVersion(String modId);
}
