package mcc.mixin;

import mcc.MCC;
import mcc.State;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyArg(method = "onResourcePackSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V"), index = 0)
    private Runnable lieToServer(Runnable par1) {
        if (!MCC.doesLog()) return par1;
        return () -> {
            this.client.setScreen(null);
            this.client.getNetworkHandler().getConnection().send(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
        };
    }

    @Inject(method = "onDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;showsDeathScreen()Z", shift = At.Shift.BEFORE))
    private void onOnDeathMessage(CallbackInfo ci) {
        if (State.getActive().map(State::stopsOnDeath).orElse(false)) MCC.MUSIC_MANAGER.stopNextTick();
    }

    @Inject(method = "onTitle", at = @At("TAIL"))
    private void onOnTitle(TitleS2CPacket packet, CallbackInfo ci) {
        Text text = packet.getTitle();
        String str = text.getString();
        if (str.equals("Round Over!") || str.equals("Game Over!")) {
            MCC.MUSIC_MANAGER.stopNextTick();
        }
    }
}
