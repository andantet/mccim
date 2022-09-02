package mcc.game;

import mcc.config.MCCModConfig.ChatConfig.HideMessagesConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mcc.MCC.*;

public enum Game implements StringIdentifiable {
    HOLE_IN_THE_WALL("Hole in the Wall", "HOLE IN THE WALL", () -> getMessagesConfig().holeInTheWall),
    TGTTOS("TGTTOS", "TGTTOS", () -> getMessagesConfig().tgttos),
    SKY_BATTLE("Sky Battle", "SKY BATTLE", () -> getMessagesConfig().skyBattle),
    BATTLE_BOX("Battle Box", "BATTLE BOX", () -> getMessagesConfig().battleBox);

    private static final Map<String, Game> GAMES_FOR_SCOREBOARD = Arrays.stream(Game.values()).collect(Collectors.toMap(Game::getScoreboardName, Function.identity()));

    private final String displayName, scoreboardName;
    private final Identifier sound;
    private final BooleanSupplier hideDeathMessages;

    Game(String displayName, String scoreboardName, BooleanSupplier hideDeathMessages) {
        this.displayName = displayName;
        this.scoreboardName = scoreboardName + " ";
        this.sound = new Identifier("mccim", "state_music." + this.name().toLowerCase(Locale.ROOT));
        this.hideDeathMessages = hideDeathMessages;
    }

    public static HideMessagesConfig.DeathsConfig.In getMessagesConfig() {
        return getHideMessagesConfig().deaths.in;
    }

    public static Optional<Game> fromScoreboard(String scoreboardName) {
        return Optional.ofNullable(GAMES_FOR_SCOREBOARD.getOrDefault(scoreboardName, null));
    }

    public String getScoreboardName() {
        return this.scoreboardName;
    }

    public Identifier getSound() {
        return this.sound;
    }

    public boolean hidesDeathMessages() {
        return this.hideDeathMessages.getAsBoolean();
    }

    @Override
    public String asString() {
        return this.displayName;
    }
}
