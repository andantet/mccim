package mcc;

import mcc.config.MCCModConfig;
import mcc.game.DefaultGameTracker;
import mcc.game.Game;
import mcc.game.GameTracker;
import mcc.skeleton.SkeletonCommand;
import mcc.tetris.TetrisCommand;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.shedaniel.autoconfig.util.Utils.*;

public class MCC implements ClientModInitializer {
    private static MCC INSTANCE;

    public static final ConfigHolder<MCCModConfig> CONFIG = MCCModConfig.create();
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

        AutoConfig.getGuiRegistry(MCCModConfig.class).registerTypeProvider(MCC::gameArrayConfigGuiProvider, Game[].class);

        Event<ClientCommandRegistrationCallback> event = ClientCommandRegistrationCallback.EVENT;
        event.register(SkeletonCommand::register);
        event.register(TetrisCommand::register);
        event.register(MCCCommand::register);
    }

    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    @SuppressWarnings("rawtypes")
    private static List<AbstractConfigListEntry> gameArrayConfigGuiProvider(String translationKey, Field field, Object config, Object defaults, GuiRegistryAccess guiProvider) {
        return Collections.singletonList(
            ENTRY_BUILDER.startStrList(
                             Text.translatable(translationKey),
                             Arrays.stream(getUnsafely(field, config, Game.values()))
                                   .map(Game::name)
                                   .toList()
                         )
                         .setDefaultValue(() -> Arrays.stream(getUnsafely(field, defaults, Game.values()))
                                                      .map(Game::name)
                                                      .toList()
                         )
                         .setSaveConsumer(value -> setUnsafely(field, config, Arrays.stream(value.toArray(String[]::new))
                                                                                    .map(str -> {
                                                                                        try {
                                                                                            return Game.valueOf(str);
                                                                                        } catch (IllegalArgumentException ignored) {}
                                                                                        return null;
                                                                                    })
                                                                                    .filter(Objects::nonNull)
                                                                                    .toArray(Game[]::new)
                         ))
                         .build()
        );
    }
}
