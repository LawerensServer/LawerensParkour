package com.lawerens.parkour.listeners;

import com.lawerens.parkour.LawerensParkour;
import com.lawerens.parkour.model.ParkourState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.lawerens.parkour.LawerensParkour.checkPlayer;

public class GameListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(checkPlayer(e.getPlayer())) {
            if (LawerensParkour.get().getGameManager().getState() != ParkourState.INGAME) return;
            if (e.getTo().getWorld().getBlockAt(e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ()).getType() == LawerensParkour.get().getParkourInfo().getFinishMaterial()) {
                if(e.getTo().getY() % 1 != 0) return;
                LawerensParkour.get().getGameManager().win(e.getPlayer());
            }
            if (e.getTo().getWorld().getBlockAt(e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ()).getType() == Material.WATER) {
                e.setTo(LawerensParkour.get().getParkourInfo().getStartLocation());
            }
        }
    }

}
