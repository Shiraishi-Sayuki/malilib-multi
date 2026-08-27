package fi.dy.masa.malilib.data;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import fi.dy.masa.malilib.MaLiLib;

// タグ登録 - ブロックタグとかアイテムタグをmalilibで使うときのデータクラス
public class MaLiLibTag
{
    public static class Blocks
    {
        // 置き換え可能なブロックのタググループ
        public static final TagKey<Block> REPLACEABLE_GROUPS = TagKey.create(
            net.minecraft.core.registries.Registries.BLOCK,
            Identifier.fromNamespaceAndPath("malilib", "replaceable_groups")
        );
    }

    // 登録処理 - タグを inicializa するときに呼ぶ
    public static void register()
    {
        MaLiLib.LOGGER.info("MaLiLibTag: Registered tags");
    }
}
