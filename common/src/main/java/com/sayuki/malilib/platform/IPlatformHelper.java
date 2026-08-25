package com.sayuki.malilib.platform;

import java.nio.file.Path;

// プラットフォームヘルパー - FabricとNeoForgeの違いを吸収して、パスとかバージョン取得を同じ感じで呼べるようにする
public interface IPlatformHelper {
    // プラットフォーム名を返す - "fabric"とか"neoforge"とか、ログで見分けるときに使う
    String getPlatformName();

    // 指定したMODが入ってるかチェック - 互換性確認とかで使う
    boolean isModLoaded(String modId);

    // 開発環境かどうか判定 - デバッグ機能を有効にするか決めるときに使う
    boolean isDevelopmentEnvironment();

    // ゲームディレクトリを取得 - ワールドとかリソース置き場の基準になる
    Path getGameDir();

    // コンフィグディレクトリを取得 - 設定ファイル置き場、ここにmalilibの設定も置く
    Path getConfigDir();

    // 指定MODのバージョン文字列を取得 - タイトル画面とかで表示するときに使う
    String getModVersion(String modId);
}
