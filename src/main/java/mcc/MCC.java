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
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MCC implements ClientModInitializer {
    private static MCC INSTANCE;

    public static final ConfigHolder<MCCModConfig> CONFIG = MCCModConfig.create();
    public final GameTracker gameTracker = new DefaultGameTracker();

    public static final HideMessagesConfig.DeathsConfig.In HIDE_DEATH_MESSAGES_IN = getConfig().chat.hideMessages.deaths.in;
    public static final HideMessagesConfig.RanksConfig.From HIDE_MESSAGES_FROM = getConfig().chat.hideMessages.ranks.from;

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

        Event<ClientCommandRegistrationCallback> event = ClientCommandRegistrationCallback.EVENT;
        event.register(SkeletonCommand::register);
        event.register(TetrisCommand::register);
        event.register(MCCCommand::register);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            long handle = MinecraftClient.getInstance().getWindow().getHandle();
            if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_P)) {
                for (Text line : lines) {
                    System.out.println(line.getString());
                }
            }
        });
    }
}
