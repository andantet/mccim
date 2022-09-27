package mcc.mixin;

import mcc.MCC;
import mcc.MCCChatConstants;
import mcc.Rank;
import mcc.config.MCCModConfig.ChatConfig.HideMessagesConfig;
import mcc.game.Game;
import mcc.game.GameTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Shadow @Final private MinecraftClient client;

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
        GameTracker gameTracker = MCC.GAME_TRACKER;

        gameTracker.onChatMessage(content);

        HideMessagesConfig config = MCC.getConfig().chat.hideMessages;

        if (config.keepMessagesWithUsername && content.contains(this.client.getSession().getUsername())) {
            return;
        }

        // hide deaths
        HideMessagesConfig.DeathsConfig deathMessages = config.deaths;
        if (deathMessages.enabled) {
            Game game = gameTracker.getGame();
            if (game != null && game.hidesDeathMessages()) {
                if (content.startsWith("[%s]".formatted(MCCChatConstants.DEATH_SKULL))) {
                    ci.cancel();
                    return;
                }
            }
        }

        // hide chats
        if (config.ranks.enabled) {
            char ch = content.charAt(0);
            Rank rank = Rank.ofIcon(ch);
            if (rank != null && rank.hidesMessages()) {
                ci.cancel();
            }
        }
    }
}
