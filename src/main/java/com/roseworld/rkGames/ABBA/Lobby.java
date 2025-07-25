package com.roseworld.rkGames.ABBA;

import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Runnables.CloseLobby;
import com.roseworld.rkGames.RkGames;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Lobby {
    String lobbyName;
    UUID creator;
    HashMap<UUID, Player> players = new HashMap<>();
    HashMap<UUID, Integer> points = new HashMap<>();
    LinkedHashMap<Material, Integer> pointsPerOre = new LinkedHashMap<>();
    Timer timer;
    boolean started = false;

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Lobby(String lobbyName, Player player) {
        this.lobbyName = lobbyName;
        this.creator = player.getUniqueId();
        players.put(player.getUniqueId(), player);

        pointsPerOre.put(Material.DIAMOND_ORE, 6);
        pointsPerOre.put(Material.EMERALD_ORE, 5);
        pointsPerOre.put(Material.LAPIS_ORE, 4);
        pointsPerOre.put(Material.REDSTONE_ORE, 2);
        pointsPerOre.put(Material.GOLD_ORE, 3);
        pointsPerOre.put(Material.IRON_ORE, 2);
        pointsPerOre.put(Material.COPPER_ORE, 1);
        pointsPerOre.put(Material.COAL_ORE, 1);
        pointsPerOre.put(Material.DEEPSLATE_DIAMOND_ORE, 6);
        pointsPerOre.put(Material.DEEPSLATE_EMERALD_ORE, 5);
        pointsPerOre.put(Material.DEEPSLATE_LAPIS_ORE, 4);
        pointsPerOre.put(Material.DEEPSLATE_REDSTONE_ORE, 2);
        pointsPerOre.put(Material.DEEPSLATE_GOLD_ORE, 3);
        pointsPerOre.put(Material.DEEPSLATE_IRON_ORE, 2);
        pointsPerOre.put(Material.DEEPSLATE_COPPER_ORE, 1);
        pointsPerOre.put(Material.DEEPSLATE_COAL_ORE, 1);
    }

    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), player);
        points.put(player.getUniqueId(), 0);
    }

    public boolean hasPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        points.remove(player.getUniqueId());
    }

    public void addPoint(Player player, int point){
        UUID id = player.getUniqueId();
        if(points.containsKey(id)){
            points.compute(id, (k, a) -> a + point);
            return;
        }
        points.put(id, point);
    }

    public int getPlayerPoints(Player player){
        return points.get(player.getUniqueId());
    }

    public Player getPlayer(UUID uuid) {
        for(UUID player : players.keySet()) {
            if(player.equals(uuid)) return players.get(player);
        }
        return null;
    }

    public List<UUID> getPlayersUUIDs() {
        return players.keySet().stream().toList();
    }

    public List<Player> getPlayers() {
        return players.values().stream().toList();
    }

    public String getName() {
        return lobbyName;
    }

    public UUID getCreator() {
        return creator;
    }

    public void addTimer(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() {
        return timer;
    }

    public void stopTimer(){
        getPlayers().forEach(player -> player.sendMessage(Message.Warning("The game was stopped by the lobby creator!")));
        timer.game.cancel();
    }

    public void changeOrePoints(Material ore, int amount){
        pointsPerOre.put(ore, amount);
    }

    public Set<Material> getOres() {
        return pointsPerOre.keySet();
    }

    public int getOrePoints(Material ore){
        return pointsPerOre.get(ore);
    }

    public HashMap<Material, Integer> getPointsSet(){
        return pointsPerOre;
    }

    public void closeLobbyProtocol() {
        List<Map.Entry<UUID, Integer>> entries = new ArrayList<>(points.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        getPlayers().forEach(player -> {
            player.sendMessage(Message.Orange("==========").append(Message.Gold("Leaderboard").append(Message.Orange("=========="))));
            for(Map.Entry<UUID, Integer> entry : entries) {
                Player p = getPlayer(entry.getKey());
                player.sendMessage(p.displayName().append(Message.Gray(" : ").append(Message.Pink(entry.getValue()))));
            }
            player.sendMessage(Message.Orange("==============================="));
        });
        getPlayer(getCreator()).sendMessage(Message.Red("Game Ended! ").append(Message.Orange("[Disband Lobby]").clickEvent(ClickEvent.runCommand("/abba leave"))));
        for (Player player : getPlayers()) {
            if(player.getUniqueId().equals(getCreator())) continue;
            player.sendMessage(Message.Red("Game Ended! ").append(Message.Orange("[Leave lobby]").clickEvent(ClickEvent.runCommand("/abba leave"))));
        }
        Bukkit.getServer().getScheduler().runTaskLater(JavaPlugin.getPlugin(RkGames.class), new CloseLobby(this), 3*60*20);
    }
}
