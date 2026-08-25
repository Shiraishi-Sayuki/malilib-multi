package com.sayuki.malilib.neoforge.datagen;

// NeoForge用ブロックタグプロバイダー - FabricのFabricTagProviderの代わりに、ここではスタブとして置いてる
// 実際のタグ生成はCommonのデータを使うから、ここはとりあえず空でOK
public class NeoForgeBlockTagProvider {
    // スタブ - 実際は何もしない、コンパイルを通すためだけ
    public NeoForgeBlockTagProvider(Object output, Object lookupProvider, Object existingFileHelper) {
    }

    protected void configure(Object wrapper) {
        // Fabric版と中身はほぼ同じだが、NeoForgeでは別途GatherDataEventで登録される
        // ここでは省略
    }
}
