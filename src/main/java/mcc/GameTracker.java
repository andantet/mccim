package mcc;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public interface GameTracker {
    void onWorldTick(ClientWorld world);
    void onHudRender(MatrixStack matrices, float tickDelta);
    void onTimeChange(int time, int oldTime);
}
