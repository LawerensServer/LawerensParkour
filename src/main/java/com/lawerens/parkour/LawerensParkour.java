package com.lawerens.parkour;

import com.lawerens.parkour.commands.EventCommand;
import com.lawerens.parkour.commands.EventSetupCommand;
import com.lawerens.parkour.listeners.GameListener;
import com.lawerens.parkour.listeners.PreGameListener;
import com.lawerens.parkour.listeners.QuitListener;
import com.lawerens.parkour.model.GameManager;
import com.lawerens.parkour.model.ParkourConfig;
import com.lawerens.parkour.model.ParkourInfo;
import com.lawerens.parkour.model.Rollback;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.lawerens.parkour.utils.CommonsUtils.sendMessageWithPrefix;

@Getter
public final class LawerensParkour extends JavaPlugin {

    private static LawerensParkour INSTANCE;
    private ParkourInfo parkourInfo;
    private final Map<UUID, Rollback> rollbacks = new HashMap<>();
    private ParkourConfig parkourConfig;
    private GameManager gameManager;

    public static LawerensParkour get() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fHabilitando plugin...");

        INSTANCE = this;

        parkourInfo = new ParkourInfo();
        parkourConfig = new ParkourConfig(this);
        sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fCargando configuración...");

        parkourConfig.registerConfig();
        parkourConfig.load();

        getCommand("lesetup").setExecutor(new EventSetupCommand());
        getCommand("evento").setExecutor(new EventCommand());

        getServer().getPluginManager().registerEvents(new PreGameListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        gameManager = new GameManager();
    }

    public static boolean checkPlayer(Player player){
        return get().getGameManager().getPlayers().contains(player);
    }

    @Override
    public void onDisable() {
        sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fDeshabilitando plugin...");

        if(getGameManager().getTaskID() != -1) Bukkit.getScheduler().cancelTask(getGameManager().getTaskID());
        rollbacks.forEach((uuid, rollback) -> {
            if(Bukkit.getPlayer(uuid) != null){
                rollback.give(true);
                rollbacks.remove(uuid);

            }
        });
    }
}
