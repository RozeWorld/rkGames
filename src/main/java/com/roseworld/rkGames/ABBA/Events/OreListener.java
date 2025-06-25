package com.roseworld.rkGames.ABBA.Events;

import com.rosekingdom.rosekingdom.Core.Utils.Message;
import com.roseworld.rkGames.ABBA.Lobby;
import com.roseworld.rkGames.ABBA.LobbyManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OreListener implements Listener, LobbyManager {
    HashMap<Location, Block> placedBlocks = new HashMap<>();
    final List<Material> ores = new ArrayList<>(List.of(
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.COAL_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.COPPER_ORE));

    @EventHandler
    public void onMinedOre(BlockBreakEvent e){
        Player p = e.getPlayer();
        Lobby lobby = getPlayerLobby(p);
        if(lobby == null) return;
        if(lobby.isStarted() && placedBlocks.containsKey(e.getBlock().getLocation())){
            p.sendMessage(Message.Warning("You can't mine placed ore blocks!"));
            placedBlocks.remove(e.getBlock().getLocation());
            return;
        }
        if(lobby.isStarted() && ores.contains(e.getBlock().getType())){
            int points = lobby.getOrePoints(e.getBlock().getType());
            lobby.addPoint(p, points);
        }
    }

    @EventHandler
    public void onPlacedOre(BlockPlaceEvent e){
        if(ores.contains(e.getBlock().getType()) && isPlayerInLobby(e.getPlayer())){
            placedBlocks.put(e.getBlock().getLocation(), e.getBlock());
        }
    }
}
