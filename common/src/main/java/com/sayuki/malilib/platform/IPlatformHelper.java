package com.sayuki.malilib.platform;

// プラットフォーム抽象化インターフェース - ローダー固有の処理を共通コードから隠すための窓口
public interface IPlatformHelper {
    // プラットフォーム名 - "Fabric"とか"Forge"とか返す、ログ出力用
    String getPlatformName();

    // MODバージョン取得 - 指定modIdのバージョン文字列を返す、見つからなければ"?"を返す
    String getModVersion(String modId);

    // MODロード確認 - 指定modIdが読み込まれてるかどうか
    boolean isModLoaded(String modId);
}
