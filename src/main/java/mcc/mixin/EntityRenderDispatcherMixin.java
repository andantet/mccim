package mcc.mixin;

import mcc.MCC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onShouldRender(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (entity == client.player) {
            return;
        }

        if (!MCC.getInstance().gameTracker.isInGame()) {
            switch (MCC.getConfig().display.lobbyEntityRenderMode) {
                case ALL_ENTITIES -> cir.setReturnValue(false);
                case PLAYERS -> {
                    if (entity instanceof PlayerEntity) cir.setReturnValue(false);
                }
            }
        }
    }
}
