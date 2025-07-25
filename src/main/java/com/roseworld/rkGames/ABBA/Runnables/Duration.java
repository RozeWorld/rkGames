package com.roseworld.rkGames.ABBA.Runnables;

import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Lobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class Duration extends BukkitRunnable {
    int time;
    Lobby lobby;

    public Duration(Lobby lobby, int time) {
        this.lobby = lobby;
        this.time = time;
    }

    @Override
    public void run() {
        switch (time){
            case 0 -> {
                lobby.setStarted(false);
                lobby.getPlayers().forEach(player -> {
                    player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER,  1.0f, 1.2f);
                    player.showTitle(Title.title(Message.Gold("Time!"), Message.LightBlue("Points " + lobby.getPlayerPoints(player))));
                });
                lobby.closeLobbyProtocol();
                this.cancel();
            }
            case 1 -> {
                lobby.getPlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(2, Note.Tone.F)));
                lobby.getPlayers().forEach(player -> player.showTitle(Title.title(Message.Red("1"), Component.empty())));
            }
            case 2 -> {
                lobby.getPlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(2, Note.Tone.F)));
                lobby.getPlayers().forEach(player -> player.showTitle(Title.title(Message.Orange("2"), Component.empty())));
            }
            case 3 -> {
                lobby.getPlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.B)));
                lobby.getPlayers().forEach(player -> player.showTitle(Title.title(Message.Gold("3"), Component.empty())));
            }
            case 4 -> {
                lobby.getPlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.B)));
                lobby.getPlayers().forEach(player -> player.showTitle(Title.title(Message.Lime("4"), Component.empty())));
            }
            case 5 -> {
                lobby.getPlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E)));
                lobby.getPlayers().forEach(player -> player.showTitle(Title.title(Message.Lime("5"), Component.empty())));
            }
            default -> lobby.getPlayers().forEach(player -> player.sendActionBar(Component.text(formatSeconds(time))));
        }
        time--;
    }

    public static String formatSeconds(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        if(hours > 0) return String.format("%dh %dm %ds", hours, minutes, seconds);
        return String.format("%dm %ds", minutes, seconds);
    }
}
