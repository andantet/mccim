package mcc;

import java.util.function.BooleanSupplier;

import static mcc.MCC.*;

public enum Rank {
    NONE('\uE0A7', () -> HIDE_MESSAGES_FROM.none),
    CHAMP('\uE0AA', () -> HIDE_MESSAGES_FROM.champ),
    GRAND_CHAMP('\uE0AB', () -> HIDE_MESSAGES_FROM.grandChamp),
    GRAND_CHAMP_ROYALE('\uE0AC', () -> HIDE_MESSAGES_FROM.grandChampRoyale),
    COMPETITOR('\uE0AD', () -> HIDE_MESSAGES_FROM.competitor),
    MODERATOR('\uE0A8', () -> HIDE_MESSAGES_FROM.moderator),
    NOXCREW('\uE082', () -> HIDE_MESSAGES_FROM.noxcrew);

    private final char icon;
    private final BooleanSupplier hidesMessages;

    Rank(char icon, BooleanSupplier hidesMessages) {
        this.icon = icon;
        this.hidesMessages = hidesMessages;
    }

    public char getIcon() {
        return this.icon;
    }

    public static Rank ofIcon(char icon) {
        for (Rank rank : values()) {
            if (rank.getIcon() == icon) return rank;
        }

        return null;
    }

    public boolean hidesMessages() {
        return this.hidesMessages.getAsBoolean();
    }
}
