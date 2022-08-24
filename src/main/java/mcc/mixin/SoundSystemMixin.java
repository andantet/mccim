package mcc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Sound;getLocation()Lnet/minecraft/util/Identifier;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPlay(SoundInstance sound, CallbackInfo ci, WeightedSoundSet soundSet, Identifier id, Sound rawSound) {
        if (MinecraftClient.getInstance().getSession().getUsername().equals("andantet")) LOGGER.info("{} - {}", sound.getCategory(), rawSound.getLocation());
    }
}
