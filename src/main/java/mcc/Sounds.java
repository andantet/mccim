package mcc;

import net.minecraft.util.Identifier;

public interface Sounds {
    Identifier EARLY_ELIMINATION = create("early_elimination");

    static Identifier create(String id) {
        return new Identifier("mccim", id);
    }
}
