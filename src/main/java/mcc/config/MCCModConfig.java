package mcc.config;

import mcc.game.Game;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.client.gui.screen.Screen;

import java.util.Arrays;

@Background(Background.TRANSPARENT)
@Config(name = "mcc")
public class MCCModConfig implements ConfigData {
    @CollapsibleObject(startExpanded = true)
    public DisplayConfig display = new DisplayConfig();

    public static class DisplayConfig {
        @Comment("Whether entities render in lobbies")
        public LobbyEntityRenderMode lobbyEntityRenderMode = LobbyEntityRenderMode.OFF;

        @Comment("Spooky scary")
        public boolean skeleton = false;
    }

    @CollapsibleObject(startExpanded = true)
    public ChatConfig chat = new ChatConfig();

    public static class ChatConfig {
        @CollapsibleObject(startExpanded = true)
        public HideDeathMessagesConfig hideDeathMessages = new HideDeathMessagesConfig();

        public static class HideDeathMessagesConfig {
            public boolean enabled = false;

            @Comment("Whether or not to hide death messages in a game mode")
            public Game[] in = Game.values();

            public boolean contains(Game game) {
                return game != null && Arrays.asList(this.in).contains(game);
            }
        }
    }

    @CollapsibleObject
    public DebugConfig debug = new DebugConfig();

    public static class DebugConfig {
        @Comment("Whether or not to display the debug HUD at the top-left")
        public boolean debugHud = false;

        @Comment("Whether or not to only display every tetris piece possible")
        public boolean tetrisPieces = false;
    }

    public static ConfigHolder<MCCModConfig> create() {
        return AutoConfig.register(MCCModConfig.class, JanksonConfigSerializer::new);
    }

    public static Screen createScreen(Screen parent) {
        return AutoConfig.getConfigScreen(MCCModConfig.class, parent).get();
    }
}
