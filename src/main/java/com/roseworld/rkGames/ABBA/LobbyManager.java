package com.roseworld.rkGames.ABBA;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface LobbyManager {
    List<Lobby> lobbies = new ArrayList<>();
    HashMap<String, Lobby> lobbiesInvites = new HashMap<>();

    static void addInvite(Lobby lobby, String invite){
        lobbiesInvites.put(invite, lobby);
    }

    static void acceptInvite(String invite, Player player){
        lobbiesInvites.get(invite).addPlayer(player);
        lobbiesInvites.remove(invite);
    }

    static List<Lobby> getLobbies() {
        return lobbies;
    }

    static Collection<String> getInvites() {
        return lobbiesInvites.keySet();
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

    default boolean isLobbyCreator(Player player){
        for(Lobby lobby : lobbies){
            if(lobby.getCreator().equals(player.getUniqueId())) return true;
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
        List<Player> players = lobby.getPlayers();
        lobby.skipProtocol();
        lobbiesInvites.values().remove(lobby);
        lobbies.remove(lobby);
        players.forEach(Player::updateCommands);
    }
}
