package fi.dy.masa.malilib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sayuki.malilib.MaLiLibCommon;

public class MaLiLib
{
    public static final Logger logger = LogManager.getLogger(MaLiLibReference.MOD_ID);

    // 後方互換 - 依存MODから直接呼ばれてもいいように残してある、中身はMaLiLibCommon.init()に委譲
    public static void onInitialize()
    {
        MaLiLibCommon.init();
    }
}
