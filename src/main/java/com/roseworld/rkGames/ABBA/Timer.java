package com.roseworld.rkGames.ABBA;

import com.roseworld.rkGames.ABBA.Runnables.Countdown;
import com.roseworld.rkGames.ABBA.Runnables.Duration;
import com.roseworld.rkGames.RkGames;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    Lobby lobby;
    int time;
    public BukkitTask game;

    public Timer(Lobby lobby, int time) {
        this.lobby = lobby;
        this.time = time;

        new Countdown(lobby, 3).runTaskTimer(RkGames.getPlugin(RkGames.class), 0L, 20L);
        game = new Duration(lobby, time * 60).runTaskTimer(RkGames.getPlugin(RkGames.class), 60L, 20L);
    }
}
