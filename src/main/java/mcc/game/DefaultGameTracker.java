package mcc.game;

import mcc.MCC;
import mcc.Sounds;
import mcc.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class DefaultGameTracker implements GameTracker {
    private static final String TIME_IDENTIFIER = ":";
    private static final String MCCI_PREFIX = "MCCI: ";

    private final MinecraftClient client = MinecraftClient.getInstance();

    private GameState state = GameState.NONE;
    private Game currentGame;
    private int time;

    private SoundInstance lastSound;

    @Override
    public void onWorldTick(ClientWorld world) {
        /* Game */

        Game oldGame = this.currentGame;
        this.currentGame = null;

        Scoreboard scoreboard = client.player.getScoreboard();
        Optional.ofNullable(scoreboard.getObjectiveForSlot(1)).ifPresent(sidebar -> {
            String sidebarName = sidebar.getDisplayName().getString();
            if (sidebarName.contains(MCCI_PREFIX)) {
                String rawId = sidebarName.substring(MCCI_PREFIX.length());
                Game.fromScoreboard(rawId).ifPresent(game -> {
                    if (game != oldGame) {
                        this.onGameChange(game, oldGame);
                    }

                    this.currentGame = game;
                });
            }
        });

        /* Time */

        if (this.currentGame != null) {
            int lastTime = this.time;
            this.time = -1;

            BossBarHud bossBarHud = this.client.inGameHud.getBossBarHud();
            List<ClientBossBar> bossBarList = ((BossBarHudAccessor) bossBarHud).getBossBars().values().stream().toList();
            if (bossBarList.size() > 0) {
                BossBar bossBar = bossBarList.get(0);
                String name = bossBar.getName().getString();
                if (name.contains(TIME_IDENTIFIER)) {
                    int index = name.indexOf(TIME_IDENTIFIER);
                    String rawMins = name.substring(index - 2, index);
                    String rawSecs = name.substring(index + 1, index + 3);
                    int mins = Integer.parseInt(rawMins);
                    int secs = Integer.parseInt(rawSecs);
                    int time = (mins * 60) + secs;

                    if (time != lastTime) {
                        this.onTimeChange(time, lastTime);
                    }

                    this.time = time;
                }
            }
        } else {
            this.time = -1;
        }

        /* Ticking */

        if (this.currentGame == null) {
            this.state = GameState.NONE;
        } else {
            if (this.state == GameState.NONE) {
                this.state = GameState.WAITING_FOR_GAME;
            }

            if (this.client.currentScreen instanceof DeathScreen && this.state.ordinal() < GameState.POST_ROUND_SELF.ordinal()) {
                this.endGame(GameState.POST_ROUND_SELF);
            }
        }
    }

    @Override
    public void onChatMessage(String message) {
        if (message.endsWith(" over!")) {
            this.endGame(message.contains("Round") ? GameState.POST_ROUND : GameState.POST_GAME);
        } else if (message.contains("you finished the round and came")) {
            this.endGame(GameState.POST_ROUND_SELF);
        } else if (message.contains("you didn't finish the round!")) {
            this.endGame(GameState.POST_ROUND_SELF);
        }
    }

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if (MCC.getConfig().display.debugHud) {
            this.client.textRenderer.draw(matrices, Text.of("" + this.state.name()), 4, 4, 0xFFFFFF);
            if (this.currentGame != null) {
                this.client.textRenderer.draw(matrices, Text.of("" + this.currentGame.asString()), 4, 14, 0xFFFFFF);
                this.client.textRenderer.draw(matrices, Text.of("" + this.time), 4, 24, 0xFFFFFF);
            }
        }
    }

    @Override
    public void onGameChange(Game game, Game oldGame) {
        if (game != oldGame && game != null && oldGame != null) {
            this.state = GameState.WAITING_FOR_GAME;
        }
    }

    @Override
    public void onTimeChange(int time, int lastTime) {
        // overtime
        if (time == 10) {
            this.stopSound();
        }

        if (this.state == GameState.POST_ROUND) {
            if (time == 15) {
                this.state = GameState.WAITING_FOR_GAME;
            }
        }

        // state decay change
        if (lastTime == 1) {
            if (this.state == GameState.WAITING_FOR_GAME) {
                this.onGameStart(this.currentGame);
                this.state = GameState.ACTIVE;
            } else if (this.state == GameState.POST_ROUND) {
                this.state = GameState.WAITING_FOR_GAME;
            } else if (this.state == GameState.POST_GAME) {
                this.state = GameState.NONE;
            }
        }
    }

    protected void onGameStart(Game game) {
        SoundInstance sound = this.lastSound = new PositionedSoundInstance(
            game.getSound(), SoundCategory.VOICE,
            1.0F, 1.0F, SoundInstance.createRandom(),
            false, 0, AttenuationType.NONE,
            0.0, 0.0, 0.0, true
        );
        this.client.getSoundManager().play(sound);
    }

    protected void endGame(GameState state) {
        this.stopSound();
        this.state = state;

        if (state == GameState.POST_ROUND_SELF) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(new SoundEvent(Sounds.EARLY_ELIMINATION), 1.0f, 1.0f), 7);
        }
    }

    protected void stopSound() {
        this.client.getSoundManager().stop(this.lastSound);
        this.lastSound = null;
    }
}
