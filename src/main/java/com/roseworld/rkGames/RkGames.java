package com.roseworld.rkGames;

import com.roseworld.rkGames.ABBA.Events.OreListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RkGames extends JavaPlugin {

    @Override
    public void onEnable() {
        new CommandManager(this);

        getServer().getPluginManager().registerEvents(new OreListener(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling RoseKingdom Games!");
    }
}
