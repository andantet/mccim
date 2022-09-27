package mcc;

import mcc.config.MCCModConfig.ChatConfig.HideMessagesConfig;

import java.util.function.BooleanSupplier;

import static mcc.MCC.*;

// TODO fix icon locations, reference texture?
public enum Rank {
    NONE('\uE0A7', () -> getMessagesConfig().none),
    CHAMP('\uE0AA', () -> getMessagesConfig().champ),
    GRAND_CHAMP('\uE0AB', () -> getMessagesConfig().grandChamp),
    GRAND_CHAMP_ROYALE('\uE0AC', () -> getMessagesConfig().grandChampRoyale),
    COMPETITOR('\uE0AD', () -> getMessagesConfig().competitor),
    MODERATOR('\uE0A8', () -> getMessagesConfig().moderator),
    NOXCREW('\uE082', () -> getMessagesConfig().noxcrew);

    private final char icon;
    private final BooleanSupplier hidesMessages;

    Rank(char icon, BooleanSupplier hidesMessages) {
        this.icon = icon;
        this.hidesMessages = hidesMessages;
    }

    public static HideMessagesConfig.RanksConfig.From getMessagesConfig() {
        return getHideMessagesConfig().ranks.from;
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
