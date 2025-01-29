package com.lawerens.parkour;

import com.lawerens.events.LawerensEvent;
import com.lawerens.events.LawerensEvents;
import com.lawerens.parkour.commands.EventSetupCommand;
import com.lawerens.parkour.listeners.GameListener;
import com.lawerens.parkour.listeners.PreGameListener;
import com.lawerens.parkour.listeners.QuitListener;
import com.lawerens.parkour.model.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.lawerens.parkour.utils.CommonsUtils.sendMessageWithPrefix;

@Getter
public final class LawerensParkour extends JavaPlugin implements LawerensEvent {

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

        getCommand("lpsetup").setExecutor(new EventSetupCommand());

        getServer().getPluginManager().registerEvents(new PreGameListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        gameManager = new GameManager();

        LawerensEvents.getPlugin(LawerensEvents.class).registerEvent(this);
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

    @Override
    public String getEventName() {
        return "Parkour";
    }

    @Override
    public void start() {
        if(LawerensParkour.get().getParkourInfo().getFinishMaterial() == null || LawerensParkour.get().getParkourInfo().getStartLocation() == null || LawerensParkour.get().getParkourInfo().getLobbyLocation() == null){
            return;
        }

        if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING || LawerensParkour.get().getGameManager().isEnable()){
            return;
        }
        LawerensParkour.get().getGameManager().setEnable(true);
    }

    @Override
    public void finish() {
        if(!LawerensParkour.get().getGameManager().isEnable() || getGameManager().getState() != ParkourState.INGAME) return;

        getGameManager().finish();
    }

    @Override
    public void join(Player player) {
        if(!LawerensParkour.get().getGameManager().isEnable()){
            sendMessageWithPrefix(player, "EVENTO", "&cEl evento de parkour no está activado.");
            return;
        }
        if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING){
            sendMessageWithPrefix(player, "EVENTO", "&c¡El evento está en juego!");
            return;
        }
        if(LawerensParkour.get().getGameManager().getPlayers().size() == 35){
            sendMessageWithPrefix(player, "EVENTO", "&cEl evento ya está lleno.");
            return;
        }

        if(LawerensParkour.get().getGameManager().getPlayers().contains((Player) player)){
            sendMessageWithPrefix(player, "EVENTO", "&cYa estás en el evento.");
            return;
        }

        LawerensParkour.get().getGameManager().join(player);
        sendMessageWithPrefix(player, "EVENTO", "&f¡Sé bienvenido al evento!");
    }

    @Override
    public void leave(Player player) {
        if(!LawerensParkour.get().getGameManager().isEnable()){
            sendMessageWithPrefix(player, "EVENTO", "&cEl evento de parkour no está activado.");
            return;
        }

        if(!LawerensParkour.get().getGameManager().getPlayers().contains(player)){
            sendMessageWithPrefix(player, "EVENTO", "&cNo estás en el evento.");
            return;
        }

        getRollbacks().get(player.getUniqueId()).give(true);
        getRollbacks().remove(player.getUniqueId());
        sendMessageWithPrefix(player, "EVENTO", "&fHas salido del evento Parkour.");
    }

    @Override
    public boolean isStarted() {
        return getGameManager().isEnable();
    }
}
