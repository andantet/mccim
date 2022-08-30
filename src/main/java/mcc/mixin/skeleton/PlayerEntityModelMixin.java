package mcc.mixin.skeleton;

import mcc.MCC;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
    private PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void onSetAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (!MCC.getConfig().display.skeleton) return;

        if (entity instanceof PlayerEntity player) {
            this.leftLeg.visible = player.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
            this.rightLeg.visible = player.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
            this.rightArm.visible = player.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
            this.leftArm.visible = player.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
            this.head.visible = player.isPartVisible(PlayerModelPart.HAT);
            this.body.visible = player.isPartVisible(PlayerModelPart.JACKET);
        }
    }
}
