package mcc.game;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public interface GameTracker {
    void onWorldTick(ClientWorld world);
    void onHudRender(MatrixStack matrices, float tickDelta);

    void onGameChange(Game game, Game oldGame);
    void onTimeChange(int time, int oldTime);

    void onChatMessage(String message);
}
