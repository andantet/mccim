package mcc;

import mcc.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public enum State {
    MAIN_ISLAND("MAIN ISLAND"),

    HOLE_IN_THE_WALL_LOBBY("SLIME FACTORY"),
    TGTTOS_LOBBY("TGTTOS"),
    SKY_BATTLE_LOBBY("SKY BATTLE"),
    BATTLE_BOX_LOBBY("BACK ALLEY"),

    HOLE_IN_THE_WALL("HOLE IN THE WALL ", "00:10", "04:00"),
    TGTTOS("TGTTOS ", "00:10", "01:30", "00:45"),
    SKY_BATTLE("SKY BATTLE ", "00:30", false, "05:00"),
    BATTLE_BOX("BATTLE BOX ", "00:10", "01:00");

    private static final String MCCI_PREFIX = "MCCI: ";

    private final String sidebarName, overtimeAt;
    private final boolean stopOnDeath;
    private final List<String> playMusicAt;

    State(String sidebarName, String overtimeAt, boolean stopOnDeath, String... playMusicAt) {
        this.sidebarName = sidebarName;
        this.overtimeAt = overtimeAt;
        this.stopOnDeath = stopOnDeath;
        this.playMusicAt = List.of(playMusicAt);
    }

    State(String sidebarName, String overtimeAt, String... playMusicAt) {
        this(sidebarName, overtimeAt, true, playMusicAt);
    }

    State(String sidebarName) {
        this(sidebarName, null);
    }

    public boolean doesPlayMusicAt(String string) {
        return this.playMusicAt.contains(string);
    }

    public boolean stopsOnDeath() {
        return this.stopOnDeath;
    }

    public String getOvertimeAt() {
        return this.overtimeAt;
    }

    public Identifier createSoundId() {
        return new Identifier("mccim", "state_music." + this.name().toLowerCase(Locale.ROOT));
    }

    public static Optional<State> getActive() {
        MinecraftClient client = MinecraftClient.getInstance();
        Scoreboard scoreboard = client.player.getScoreboard();
        ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(1);
        if (sidebar == null) {
            return Optional.empty();
        }

        String sidebarName = sidebar.getDisplayName().getString();
        if (!sidebarName.contains(MCCI_PREFIX)) {
            return Optional.empty();
        }
        String rawId = sidebarName.substring(MCCI_PREFIX.length());
        return Arrays.stream(values())
                     .filter(state -> Objects.equals(state.sidebarName, rawId))
                     .findAny();
    }

    public static Optional<String> getTime() {
        MinecraftClient client = MinecraftClient.getInstance();
        BossBarHud bossBarHud = client.inGameHud.getBossBarHud();
        BossBarHudAccessor accessor = (BossBarHudAccessor) bossBarHud;
        Collection<ClientBossBar> bossBarList = accessor.getBossBars().values();
        return bossBarList.stream().filter(bar -> bar.getColor().equals(Color.WHITE)).findFirst().map(bar -> {
            String name = bar.getName().getString();
            if (!name.contains(":")) return null;
            int index = name.indexOf(":");
            return name.substring(index - 2, index + 2 + 1);
        });
    }
}
