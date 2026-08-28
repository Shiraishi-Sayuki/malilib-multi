package com.sayuki.malilib.neoforge;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibConfigGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(value = "malilib", dist = Dist.CLIENT)
public class MaLiLibNeoForge {
    public MaLiLibNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        MaLiLib.init();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (java.util.function.Supplier<IConfigScreenFactory>) () -> (IConfigScreenFactory) (minecraft, parent) -> {
            MaLiLibConfigGui gui = new MaLiLibConfigGui();
            gui.setParent(parent);
            return gui;
        });
        modEventBus.addListener((RegisterPayloadHandlersEvent event) -> {
            NeoForgeNetworkHelper.setRegistrar(event.registrar("malilib"));
        });

    }
}
