package fi.dy.masa.malilib.compat.iris;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.compat.ModIds;
import fi.dy.masa.malilib.render.MaLiLibPipelines;
import com.sayuki.malilib.platform.Services;

public class IrisCompat
{
    private static boolean isSodiumLoaded = false;
    private static boolean isIrisLoaded = false;
    private static String sodiumVersion = "";
    private static String irisVersion = "";

    static
    {
	    if (Services.PLATFORM.getAllModVersions().containsKey(ModIds.sodium))
	    {
			sodiumVersion = Services.PLATFORM.getAllModVersions().get(ModIds.sodium);
			isSodiumLoaded = true;
	    }
		if (Services.PLATFORM.getAllModVersions().containsKey(ModIds.iris))
		{
			irisVersion = Services.PLATFORM.getAllModVersions().get(ModIds.iris);
			isIrisLoaded = true;
		}

        MaLiLib.LOGGER.info("Sodium: [{}], Iris: [{}]", isSodiumLoaded ? sodiumVersion : "N/F", isIrisLoaded ? irisVersion : "N/F");
    }

	public static boolean hasSodium()
	{
		return isSodiumLoaded;
	}

	public static boolean hasIris()
    {
        return isSodiumLoaded && isIrisLoaded;
    }

	public static boolean isShaderActive()
	{
		if (hasIris())
		{
			try
			{
				Class<?> irisApi = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
				Object instance = irisApi.getMethod("getInstance").invoke(null);
				return (boolean) irisApi.getMethod("isShaderPackInUse").invoke(instance);
			}
			catch (Exception e)
			{
				MaLiLib.LOGGER.debug("Iris API not available: {}", e.getMessage());
			}
		}

		return false;
	}

	public static boolean isShadowPassActive()
	{
		if (hasIris())
		{
			try
			{
				Class<?> irisApi = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
				Object instance = irisApi.getMethod("getInstance").invoke(null);
				return (boolean) irisApi.getMethod("isRenderingShadowPass").invoke(instance);
			}
			catch (Exception e)
			{
				MaLiLib.LOGGER.debug("Iris API not available: {}", e.getMessage());
			}
		}

		return false;
	}

	public static void registerPipelines()
    {
        if (hasIris())
        {
            MaLiLib.LOGGER.info("Assigning MaLiLib Pipelines to Iris Programs: (skipped - Iris API requires Fabric)");
        }
    }
}
