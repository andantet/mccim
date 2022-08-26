package mcc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MCC implements ClientModInitializer {
    public final GameTracker gameTracker = new DefaultGameTracker();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(gameTracker::onHudRender);
        ClientTickEvents.START_WORLD_TICK.register(gameTracker::onWorldTick);
    }
}
