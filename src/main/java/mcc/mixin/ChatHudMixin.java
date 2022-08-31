package mcc.mixin;

import mcc.MCC;
import mcc.config.MCCModConfig.ChatConfig.HideDeathMessagesConfig;
import mcc.game.GameTracker;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(
        method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/ChatHud;logChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void onAddMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        String content = message.getString();
        GameTracker gameTracker = MCC.getInstance().gameTracker;

        gameTracker.onChatMessage(content);

        HideDeathMessagesConfig config = MCC.getConfig().chat.hideDeathMessages;
        if (config.enabled && config.contains(gameTracker.getGame())) {
            if (content.startsWith("[\uE0AF]") && !content.contains("you were eliminated")) {
                ci.cancel();
            }
        }
    }
}
