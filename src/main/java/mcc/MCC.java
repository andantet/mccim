package mcc;

import mcc.game.DefaultGameTracker;
import mcc.game.GameTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MCC implements ClientModInitializer {
    private static MCC INSTANCE;
    public final GameTracker gameTracker = new DefaultGameTracker();

    public MCC() {
        INSTANCE = this;
    }

    public static MCC getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(this.gameTracker::onWorldTick);
        HudRenderCallback.EVENT.register(this.gameTracker::onHudRender);
    }
}
