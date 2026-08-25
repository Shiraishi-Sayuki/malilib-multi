package com.sayuki.malilib.neoforge.gui;

import fi.dy.masa.malilib.MaLiLibConfigGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

// NeoForge用コンフィグ画面
public class NeoForgeConfigScreen extends Screen {
    private final Screen parent;
    public NeoForgeConfigScreen(Screen parent) {
        super(Text.literal("MaLiLib Config"));
        this.parent = parent;
    }
    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> close()).dimensions(width / 2 - 100, height - 30, 200, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Open MaLiLib Settings"), btn -> {
            MaLiLibConfigGui gui = new MaLiLibConfigGui();
            gui.setParent(parent);
            MinecraftClient.getInstance().setScreen(gui);
        }).dimensions(width / 2 - 100, height / 2 - 10, 200, 20).build());
    }
    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("NeoForge Config - Click Open to edit MaLiLib"), width / 2, height / 2 - 30, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
}
