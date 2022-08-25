package mcc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

public class MusicManager {
    private PositionedSoundInstance currentMusic;
    private boolean playedMusic;
    private boolean stopNextTick;

    public void clientTick(MinecraftClient client) {
        if (client.player == null) return;

        State.getActive().ifPresent(state -> {
            State.getTime().ifPresentOrElse(
                time -> {
                    if (state.doesPlayMusicAt(time) && this.currentMusic == null) {
                        if (!this.playedMusic) {
                            PositionedSoundInstance sound = PositionedSoundInstance.master(new SoundEvent(state.createSoundId()), 1.0f);
                            client.getSoundManager().play(sound);
                            this.currentMusic = sound;
                            this.playedMusic = true;
                        }
                    }

                    if ((this.stopNextTick || time.equals(state.getOvertimeAt())) && this.currentMusic != null) {
                        this.stopPrematurely();
                    }

                    if (time.equals("00:00") && this.currentMusic != null) {
                        this.stopPrematurely();
                    }
                },
                () -> {
                    this.playedMusic = false;
                    this.currentMusic = null;
                }
            );
        });

        this.stopNextTick = false;
    }

    private void stopPrematurely() {
        MinecraftClient.getInstance().getSoundManager().stop(this.currentMusic);
        this.playedMusic = false;
        this.currentMusic = null;
    }

    public void stopNextTick() {
        this.stopNextTick = true;
    }
}
