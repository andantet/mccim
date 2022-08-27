package mcc.game;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Game implements StringIdentifiable {
    HOLE_IN_THE_WALL("Hole in the Wall", "HOLE IN THE WALL"),
    TGTTOS("TGTTOS", "TGTTOS"),
    SKY_BATTLE("Sky Battle", "SKY BATTLE"),
    BATTLE_BOX("Battle Box", "BATTLE BOX");

    private static final Map<String, Game> GAMES_FOR_SCOREBOARD = Arrays.stream(Game.values()).collect(Collectors.toMap(Game::getScoreboardName, Function.identity()));

    private final String displayName, scoreboardName;
    private final Identifier sound;

    Game(String displayName, String scoreboardName) {
        this.displayName = displayName;
        this.scoreboardName = scoreboardName + " ";
        this.sound = new Identifier("mccim", "state_music." + this.name().toLowerCase(Locale.ROOT));
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

    @Override
    public String asString() {
        return this.displayName;
    }
}
