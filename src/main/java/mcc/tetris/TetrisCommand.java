package mcc.tetris;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class TetrisCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(literal("tetris").executes(TetrisCommand::execute));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(TetrisScreen.INSTANCE));
        return 1;
    }
}
