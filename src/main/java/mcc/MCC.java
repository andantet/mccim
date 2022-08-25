package mcc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.text.Text;

public class MCC implements ClientModInitializer {
    public static final MusicManager MUSIC_MANAGER = new MusicManager();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            textRenderer.draw(matrices, Text.of(State.getActive().map(State::name).orElse("(TRANSITION)")), 4, 4, 0xFFFFFF);
            textRenderer.draw(matrices, Text.of(State.getTime().orElse("(NO TIME)")), 4, 14, 0xFFFFFF);
        });

        ClientTickEvents.END_CLIENT_TICK.register(MUSIC_MANAGER::clientTick);
    }

    public static boolean doesLog() {
        return MinecraftClient.getInstance().options.getCloudRenderModeValue().equals(CloudRenderMode.FAST);
    }

    public static boolean doesLogLarge() {
        return MinecraftClient.getInstance().options.getCloudRenderModeValue().equals(CloudRenderMode.OFF);
    }
}
