package mcc;

import mcc.config.MCCModConfig;
import mcc.config.MCCModConfig.ChatConfig.HideMessagesConfig;
import mcc.game.DefaultGameTracker;
import mcc.game.GameTracker;
import mcc.skeleton.SkeletonCommand;
import mcc.tetris.TetrisCommand;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.Event;

public class MCC implements ClientModInitializer {
    public static final GameTracker GAME_TRACKER = new DefaultGameTracker();
    public static final ConfigHolder<MCCModConfig> CONFIG = MCCModConfig.create();

    public static MCCModConfig getConfig() {
        return CONFIG.getConfig();
    }

    public static HideMessagesConfig getHideMessagesConfig() {
        return getConfig().chat.hideMessages;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(GAME_TRACKER::onWorldTick);
        HudRenderCallback.EVENT.register(GAME_TRACKER::onHudRender);

        Event<ClientCommandRegistrationCallback> event = ClientCommandRegistrationCallback.EVENT;
        event.register(SkeletonCommand::register);
        event.register(TetrisCommand::register);
        event.register(MCCCommand::register);
    }
}
