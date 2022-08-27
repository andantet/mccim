package mcc.config;

import mcc.MCC;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.gui.screen.Screen;

@Background(Background.TRANSPARENT)
@Config(name = "mcc")
public class MCCModConfig implements ConfigData {
    @CollapsibleObject(startExpanded = true)
    public Display display = new Display();

    public static class Display {
        @Comment("Whether or not to display the debug HUD at the top-left")
        public boolean debugHud = false;

        @Comment("Whether entities render in lobbies")
        public LobbyEntityRenderMode lobbyEntityRenderMode = LobbyEntityRenderMode.OFF;
    }

    public static ConfigHolder<MCCModConfig> initialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, manager) -> MCC.CONFIG.load());
        return AutoConfig.register(MCCModConfig.class, JanksonConfigSerializer::new);
    }

    public static Screen createScreen(Screen parent) {
        return AutoConfig.getConfigScreen(MCCModConfig.class, parent).get();
    }
}
