package com.roseworld.rkGames.ABBA;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface LobbyManager {
    List<Lobby> lobbies = new ArrayList<>();

    static List<Lobby> getLobbies() {
        return lobbies;
    }

    default Lobby getLobby(String name){
        for(Lobby lobby : lobbies){
            if(lobby.getName().equals(name)) return lobby;
        }
        return null;
    }

    default boolean isPlayerInLobby(Player player){
        for(Lobby lobby : lobbies){
            if(lobby.hasPlayer(player)) return true;
        }
        return false;
    }

    default Lobby getPlayerLobby(Player player){
        for(Lobby lobby : lobbies){
            if(lobby.hasPlayer(player)) return lobby;
        }
        return null;
    }

    default List<String> getLobbyNames(){
        List<String> names = new ArrayList<>();
        for(Lobby lobby : lobbies){
            names.add(lobby.getName());
        }
        return names;
    }

    default void addLobby(Lobby lobby){
        lobbies.add(lobby);
    };

    default void removeLobby(Lobby lobby){
        lobbies.remove(lobby);
    }
}
