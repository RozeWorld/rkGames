package com.roseworld.rkGames.ABBA.Runnables;

import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Lobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    int time = 3;
    Lobby lobby;

    public Countdown(Lobby lobby, int time){
        this.lobby = lobby;
        this.time = time;
    }

    @Override
    public void run() {
        lobby.getPlayers().forEach(player -> {
            switch (time) {
                case 0 -> {
                    player.showTitle(Title.title(Message.Lime("Start!"), Component.empty()));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 1, 1);
                    lobby.setStarted(true);
                }
                case 1 -> {
                    player.showTitle(Title.title(Message.Lime(1), Component.empty()));
                    player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(2, Note.Tone.F));
                }
                case 2 -> {
                    player.showTitle(Title.title(Message.Gold(2), Component.empty()));
                    player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.B));
                }
                case 3 -> {
                    player.showTitle(Title.title(Message.Red(3), Component.empty()));
                    player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.E));
                }
            }
        });
        if(time == 0){
            this.cancel();
        }
        time--;
    }
}
