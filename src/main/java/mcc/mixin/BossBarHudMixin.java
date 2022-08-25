package mcc.mixin;

import com.mojang.logging.LogUtils;
import mcc.MCC;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Unique private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "renderBossBar(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V", at = @At("HEAD"))
    private void onRenderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar, CallbackInfo ci) {
        if (MCC.doesLogLarge()) {
            LOGGER.info("{} - {}", bossBar.getName().getString(), bossBar.getColor().name());
        }
    }
}
