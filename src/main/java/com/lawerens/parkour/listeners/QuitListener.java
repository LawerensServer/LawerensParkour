package com.lawerens.parkour.listeners;

import com.lawerens.parkour.LawerensParkour;
import com.lawerens.parkour.model.ParkourState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        LawerensParkour.get().leave(e.getPlayer());
        /*
        if(LawerensParkour.get().getRollbacks().containsKey(e.getPlayer().getUniqueId())){
            LawerensParkour.get().getRollbacks().get(e.getPlayer().getUniqueId()).give(true);
            LawerensParkour.get().getRollbacks().remove(e.getPlayer().getUniqueId());
        }

        if(checkPlayer(e.getPlayer())){
            LawerensParkour.get().getGameManager().getPlayers().remove(e.getPlayer());
            if(LawerensParkour.get().getGameManager().getState() == ParkourState.INGAME && LawerensParkour.get().getGameManager().getPlayers().isEmpty()){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    sendUnderline(p, "#cf0011");
                    sendMessage(p, " ");
                    sendCenteredMessage(p, "&fEl Evento &cParkour&f se detuvo");
                    sendCenteredMessage(p, "&fDebido a que todos los jugadores han salido.");
                    sendMessage(p, " ");
                    sendUnderline(p, "#cf0011");
                }
                LawerensParkour.get().getGameManager().finish();
                return;
            }
            for (Player player : LawerensParkour.get().getGameManager().getPlayers())
                sendMessageWithPrefix(player, "EVENTO", "&e"+e.getPlayer().getName()+
                        " &fha salido. &f(&e"+LawerensParkour.get().getGameManager().getPlayers().size()+"&f/&e30&f)");

        }
        */
    }
}
