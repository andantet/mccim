package mcc;

import mcc.config.MCCModConfig;
import mcc.game.DefaultGameTracker;
import mcc.game.GameTracker;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MCC implements ClientModInitializer {
    private static MCC INSTANCE;

    public static final ConfigHolder<MCCModConfig> CONFIG = MCCModConfig.initialize();
    public final GameTracker gameTracker = new DefaultGameTracker();

    public MCC() {
        INSTANCE = this;
    }

    public static MCC getInstance() {
        return INSTANCE;
    }

    public static MCCModConfig getConfig() {
        return CONFIG.getConfig();
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(this.gameTracker::onWorldTick);
        HudRenderCallback.EVENT.register(this.gameTracker::onHudRender);
    }
}
