package com.sayuki.malilib.platform;

import java.util.ServiceLoader;

// サービスを読み込む - 指定クラスの実装を取得、ローダーごとの実装はServiceLoaderで探す
public class Services {
    // プラットフォーム実装 - FabricかForgeのどっちかが入る
    public static final IPlatformHelper PLATFORM = loadPlatform();

    private static IPlatformHelper loadPlatform() {
        for (IPlatformHelper helper : ServiceLoader.load(IPlatformHelper.class)) {
            return helper;
        }
        throw new IllegalStateException("No IPlatformHelper implementation found - services file missing?");
    }

    // インスタンス化させない - staticだけのクラスだから
    private Services() {}
}
