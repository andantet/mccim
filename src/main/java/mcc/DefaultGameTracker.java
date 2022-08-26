package mcc;

import mcc.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;

import java.util.List;

public class DefaultGameTracker implements GameTracker {
    private static final String TIME_IDENTIFIER = ":";
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int time, lastTime;

    @Override
    public void onWorldTick(ClientWorld world) {
        BossBarHud bossBarHud = this.client.inGameHud.getBossBarHud();
        List<ClientBossBar> bossBarList = ((BossBarHudAccessor) bossBarHud).getBossBars().values().stream().toList();
        if (bossBarList.size() > 0) {
            BossBar bossBar = bossBarList.get(0);
            String name = bossBar.getName().getString();
            if (name.contains(TIME_IDENTIFIER)) {
                int index = name.indexOf(TIME_IDENTIFIER);
                String rawMins = name.substring(index - 2, index - 1);
                String rawSecs = name.substring(index + 1, index + 2);
                int mins = Integer.parseInt(rawMins);
                int secs = Integer.parseInt(rawSecs);

                int time = (mins * 60) + secs;
                if (time != this.lastTime) this.onTimeChange(time, this.lastTime);

                this.time = lastTime;
                this.lastTime = time;
            }
        }
    }

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        this.client.textRenderer.draw(matrices, Text.of("" + this.time), 4, 4, 0xFFFFFF);
    }

    @Override
    public void onTimeChange(int time, int lastTime) {
    }
}
