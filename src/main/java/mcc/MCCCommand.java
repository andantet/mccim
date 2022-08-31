package mcc;

import com.mojang.brigadier.CommandDispatcher;
import mcc.config.MCCModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class MCCCommand {
    private static final String RELOAD_CONFIG_KEY = "text.mcc.reload_config";

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(
            literal("mcc")
                .then(
                    literal("config")
                        .then(
                            literal("reload")
                                .executes(context -> {
                                    if (!MCC.CONFIG.load()) throw new RuntimeException("Failed to load config");
                                    context.getSource().sendFeedback(Text.translatable(RELOAD_CONFIG_KEY));
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            MinecraftClient client = context.getSource().getClient();
                            client.send(() -> client.setScreen(MCCModConfig.createScreen(null)));
                            return 1;
                        })
                )
        );
    }
}
