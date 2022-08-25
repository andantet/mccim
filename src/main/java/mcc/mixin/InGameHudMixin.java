package mcc.mixin;

import com.mojang.logging.LogUtils;
import mcc.MCC;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique private String lastSidebarName;
    @Unique private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"))
    private void onRenderScoreboardSidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        if (!MCC.doesLog()) return;

        Text displayName = objective.getDisplayName();
        String str = displayName.getString();
        if (!Objects.equals(str, this.lastSidebarName)) LOGGER.info(str);
        this.lastSidebarName = str;
    }
}
