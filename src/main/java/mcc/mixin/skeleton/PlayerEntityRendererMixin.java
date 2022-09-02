package mcc.mixin.skeleton;

import mcc.MCC;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private SkeletonEntityRenderer skeletonEntityRenderer;
    private SkeletonEntity fakeSkeletonEntity;

    private PlayerEntityRendererMixin(EntityRendererFactory.Context context, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererFactory.Context context, boolean slim, CallbackInfo ci) {
        this.skeletonEntityRenderer = new SkeletonEntityRenderer(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void onRender(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light, CallbackInfo ci) {
        if (!MCC.getConfig().debug.skeleton) return;

        if (this.fakeSkeletonEntity == null) {
            this.fakeSkeletonEntity = new SkeletonEntity(EntityType.SKELETON, player.world);
        }
        this.fakeSkeletonEntity.setYaw(player.getYaw());
        this.fakeSkeletonEntity.setPitch(player.getPitch());
        this.fakeSkeletonEntity.headYaw = player.headYaw;
        this.fakeSkeletonEntity.bodyYaw = player.bodyYaw;
        this.fakeSkeletonEntity.limbAngle = player.limbAngle;
        this.fakeSkeletonEntity.limbDistance = player.limbDistance;
        this.fakeSkeletonEntity.lastLimbDistance = player.lastLimbDistance;
        this.fakeSkeletonEntity.prevYaw = player.prevYaw;
        this.fakeSkeletonEntity.prevBodyYaw = player.prevBodyYaw;
        this.fakeSkeletonEntity.prevHeadYaw = player.prevHeadYaw;
        this.fakeSkeletonEntity.prevPitch = player.prevPitch;
        this.fakeSkeletonEntity.handSwinging = player.handSwinging;
        this.fakeSkeletonEntity.handSwingTicks = player.handSwingTicks;
        this.fakeSkeletonEntity.handSwingProgress = player.handSwingProgress;
        this.fakeSkeletonEntity.lastHandSwingProgress = player.lastHandSwingProgress;
        this.fakeSkeletonEntity.setLeftHanded(player.getMainArm() != Arm.LEFT);

        SkeletonEntityModel<?> model = this.skeletonEntityRenderer.getModel();
        PlayerEntityModel<?> playerModel = this.getModel();
        model.setVisible(false);
        model.leftLeg.visible = !playerModel.leftLeg.visible;
        model.rightLeg.visible = !playerModel.rightLeg.visible;
        model.rightArm.visible = !playerModel.rightArm.visible;
        model.leftArm.visible = !playerModel.leftArm.visible;
        model.head.visible = !playerModel.head.visible;
        model.body.visible = !playerModel.body.visible;
        this.skeletonEntityRenderer.render(this.fakeSkeletonEntity, yaw, tickDelta, matrices, vertices, light);
    }
}
