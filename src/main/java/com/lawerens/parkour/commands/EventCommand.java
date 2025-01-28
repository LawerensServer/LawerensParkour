package com.lawerens.parkour.commands;

import com.lawerens.parkour.LawerensParkour;
import com.lawerens.parkour.model.ParkourState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.lawerens.parkour.utils.CommonsUtils.sendMessageWithPrefix;

public class EventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("parkour.join")){
            sendMessageWithPrefix(sender, "EVENTO", "&cEl evento está siendo probado por DamianFarias, esperen a que se termine.");
            return false;
        }
        if(!LawerensParkour.get().getGameManager().isEnable()){
            sendMessageWithPrefix(sender, "EVENTO", "&cEl evento de parkour no está activado.");
            return false;
        }
        if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING){
            sendMessageWithPrefix(sender, "EVENTO", "&c¡El evento está en juego!");
            return false;
        }
        if(LawerensParkour.get().getGameManager().getPlayers().size() == 35){
            sendMessageWithPrefix(sender, "EVENTO", "&cEl evento ya está lleno.");
            return false;
        }

        if(LawerensParkour.get().getGameManager().getPlayers().contains((Player) sender)){
            sendMessageWithPrefix(sender, "EVENTO", "&cYa estás en el evento.");
            return false;
        }

        LawerensParkour.get().getGameManager().join((Player) sender);
        sendMessageWithPrefix(sender, "EVENTO", "&f¡Sé bienvenido al evento!");
        return true;
    }
}
