package com.roseworld.rkGames.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Lobby;
import com.roseworld.rkGames.ABBA.LobbyManager;
import com.roseworld.rkGames.ABBA.Timer;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class abba implements LobbyManager {
    Player player;
    Lobby lobby;

//Saved for the leaderboard thingy
//    Audience audience = Audience.audience(lobby.getPlayers());


    public void register(Commands cm) {
        cm.register(Commands.literal("abba")
                .requires(source -> {
                    if(source.getExecutor() instanceof Player sender){
                        this.player = sender;
                        this.lobby = getPlayerLobby(sender);
                        return true;
                    }
                    return false;
                })
                .then(Commands.literal("create")
                        .then(Commands.argument("name",  StringArgumentType.greedyString())
                                .executes(context -> {
                                    if(isPlayerInLobby(player)) {
                                        player.sendMessage(Message.Warning("You're already in a lobby! ").append(Message.Orange("["+lobby.getName()+"]")));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    String name = StringArgumentType.getString(context, "name");
                                    addLobby(new Lobby(name, player));
                                    player.sendMessage(Message.Lime("ABBA lobby "+ name +" has been created!"));
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            player.sendMessage(Message.Info("You need to name the lobby to create one!"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("lobby")
                        .executes(context -> {
                            if(!isPlayerInLobby(player)){
                                player.sendMessage(Message.Warning("You're not in a lobby!"));
                                return Command.SINGLE_SUCCESS;
                            }
                            player.sendMessage(Message.Gray("============Players============"));
                            for(Player p : lobby.getPlayers()){
                                player.sendMessage(p.displayName());
                            }
                            player.sendMessage(Message.Gray("==============================="));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("points")
                        .executes(context -> {
                            if(!isPlayerInLobby(player)) {
                                player.sendMessage(Message.Warning("You're not in a lobby!"));
                                return Command.SINGLE_SUCCESS;
                            }
                            player.sendMessage(Message.Orange("=============Points============="));
                            for(Material material : lobby.getOres()){
                                player.sendMessage(Message.Gold(material.name()).append(Message.Gray(" : ").append(Message.LightBlue(lobby.getOrePoints(material)))));
                            }
                            player.sendMessage(Message.Orange("================================"));

                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("ore", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    if(lobby == null) return builder.buildFuture();
                                    lobby.getOres().forEach(ore -> builder.suggest(ore.name()));
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            if(!lobby.getCreator().equals(player.getUniqueId())) {
                                                player.sendMessage(Message.Warning("Only the lobby creator can change the points!"));
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            Material ore = Material.matchMaterial(StringArgumentType.getString(context, "ore"));
                                            int amount = IntegerArgumentType.getInteger(context, "amount");
                                            lobby.changeOrePoints(ore, amount);
                                            Audience audience = Audience.audience(lobby.getPlayers());
                                            audience.sendMessage(Message.Info(ore.name() + " has been changed to " + amount + " points"));
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("join")
                        .executes(context -> {
                            player.sendMessage(Message.Info("To join a lobby you need to select or create one!"));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("name",StringArgumentType.greedyString())
                                .suggests((context, builder) -> {
                                    for (String name: getLobbyNames()){
                                        builder.suggest(name);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    if(isPlayerInLobby(player)){
                                        player.sendMessage(Message.Warning("You're already in a lobby! ").append(Message.Orange("["+lobby.getName()+"]")));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    String name = StringArgumentType.getString(context, "name");
                                    for(Lobby lobby : lobbies){
                                        if(lobby.getName().equals(name)){
                                            lobby.addPlayer(player);
                                            player.sendMessage(Message.Lime("You joined the "+ lobby.getName() +" lobby!"));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                    }
                                    player.sendMessage(Message.Warning("There is no lobby with that name!"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("leave")
                        .executes(context -> {
                            if(!isPlayerInLobby(player)){
                                player.sendMessage(Message.Warning("You're not in a lobby!"));
                                return Command.SINGLE_SUCCESS;
                            }
                            //test if this actually removes the lobby data
                            if(lobby.getCreator().equals(player.getUniqueId())) {
                                lobby.getPlayers().forEach(player -> player.sendMessage(Message.Info("The ABBA lobby was disbanded")));
                                removeLobby(lobby);
                                return Command.SINGLE_SUCCESS;
                            }
                            lobby.removePlayer(player);
                            player.sendMessage(Message.Info("You left the ABBA lobby("+lobby.getName()+")"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("start")
                        .executes(context -> {
                            if(!isPlayerInLobby(player)){
                                player.sendMessage(Message.Warning("You're not in a lobby!"));
                                return Command.SINGLE_SUCCESS;
                            }
                            if(lobby != null && lobby.getCreator().equals(player.getUniqueId())){
                                lobby.addTimer(new Timer(lobby, 5));
                                player.sendMessage(Message.Info("The ABBA caving game is starting..."));
                                return Command.SINGLE_SUCCESS;
                            }
                            player.sendMessage(Message.Warning("Only the lobby creator can start the game!"));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("time", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int time =  IntegerArgumentType.getInteger(context, "time");
                                    if(!isPlayerInLobby(player)){
                                        player.sendMessage(Message.Warning("You're not in a lobby!"));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if(lobby != null && lobby.getCreator().equals(player.getUniqueId())){
                                        lobby.addTimer(new Timer(lobby, time));
                                        player.sendMessage(Message.Info("The ABBA caving game is starting..."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    player.sendMessage(Message.Warning("Only the lobby creator can start the game!"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("stop")
                        .executes(context -> {
                            if(!isPlayerInLobby(player)){
                                player.sendMessage(Message.Warning("You're not in a lobby!"));
                                return Command.SINGLE_SUCCESS;
                            }
                            if(lobby != null && !lobby.isStarted()) {
                                if (!lobby.getCreator().equals(player.getUniqueId())) {
                                    player.sendMessage(Message.Warning("Only the lobby creator can stop the game!"));
                                    return Command.SINGLE_SUCCESS;
                                }
                                lobby.setStarted(false);
                                lobby.stopTimer();
                                return Command.SINGLE_SUCCESS;
                            }
                            player.sendMessage(Message.Info("The game hasn't started!"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build(), "Commands for starting and creating ABBA caving games");
    }
}
