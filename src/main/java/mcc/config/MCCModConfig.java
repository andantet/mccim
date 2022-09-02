package mcc.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.client.gui.screen.Screen;

@Background(Background.TRANSPARENT)
@Config(name = "mcc")
public class MCCModConfig implements ConfigData {
    /* Chat */

    @CollapsibleObject(startExpanded = true)
    public ChatConfig chat = new ChatConfig();

    public static class ChatConfig {
        @CollapsibleObject(startExpanded = true)
        public HideMessagesConfig hideMessages = new HideMessagesConfig();

        public static class HideMessagesConfig {
            @Comment("Whether or not to keep any message containing your client username")
            public boolean keepMessagesWithUsername = true;

            @CollapsibleObject
            public DeathsConfig deaths = new DeathsConfig();

            public static class DeathsConfig {
                public boolean enabled = false;

                @CollapsibleObject(startExpanded = true)
                public In in = new In();

                public static class In {
                    public boolean
                        holeInTheWall,
                        tgttos,
                        skyBattle,
                        battleBox;
                }
            }

            @CollapsibleObject
            public RanksConfig ranks = new RanksConfig();

            public static class RanksConfig {
                public boolean enabled = false;

                @CollapsibleObject(startExpanded = true)
                public From from = new From();

                public static class From {
                    public boolean
                        none,
                        champ,
                        grandChamp,
                        grandChampRoyale,
                        competitor,
                        moderator,
                        noxcrew;
                }
            }
        }
    }

    /* Display */

    @CollapsibleObject(startExpanded = true)
    public DisplayConfig display = new DisplayConfig();

    public static class DisplayConfig {
        @Comment("Whether entities render in lobbies")
        public LobbyEntityRenderMode lobbyEntityRenderMode = LobbyEntityRenderMode.OFF;
    }

    /* Debug */

    @CollapsibleObject
    public DebugConfig debug = new DebugConfig();

    public static class DebugConfig {
        @Comment("Whether or not to display the debug HUD at the top-left")
        public boolean debugHud = false;

        @Comment("Whether or not to only display every tetris piece possible")
        public boolean tetrisPieces = false;

        @Comment("Spooky scary")
        public boolean skeleton = false;
    }

    public static ConfigHolder<MCCModConfig> create() {
        return AutoConfig.register(MCCModConfig.class, JanksonConfigSerializer::new);
    }

    public static Screen createScreen(Screen parent) {
        return AutoConfig.getConfigScreen(MCCModConfig.class, parent).get();
    }
}
