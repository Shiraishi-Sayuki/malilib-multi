package fi.dy.masa.malilib.compat.mixin;

import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import fi.dy.masa.malilib.MaLiLib;

public class MaLiLibMixinConfigPlugin implements IMixinConfigPlugin
{
	@Override
	public void onLoad(String mixinPackage)
	{
	}

	@Override
	public String getRefMapperConfig()
	{
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
	}

	@Override
	public List<String> getMixins()
	{
		return null;
	}

	@Override
	public void preApply(String mixinClassName, org.objectweb.asm.tree.ClassNode targetClass, String targetClassName, IMixinInfo mixinInfo)
	{
	}

	@Override
	public void postApply(String mixinClassName, org.objectweb.asm.tree.ClassNode targetClass, String targetClassName, IMixinInfo mixinInfo)
	{
	}
}
