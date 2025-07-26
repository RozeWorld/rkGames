package com.roseworld.rkGames.ABBA.Runnables;

import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Lobby;
import com.roseworld.rkGames.ABBA.LobbyManager;

public class CloseLobby implements Runnable,LobbyManager {

    private final Lobby lobby;

    public CloseLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        lobby.getPlayers().forEach(player -> {
            player.sendMessage(Message.Warning("The ABBA caving lobby was automatically disbanded!"));
            player.updateCommands();
        });
        removeLobby(lobby);
    }
}
