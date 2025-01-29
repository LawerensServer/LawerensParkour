package com.lawerens.parkour.commands;

import com.lawerens.events.LawerensEvent;
import com.lawerens.events.LawerensEvents;
import com.lawerens.parkour.LawerensParkour;
import com.lawerens.parkour.model.ParkourState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.lawerens.parkour.utils.CommonsUtils.sendMessageWithPrefix;
import static xyz.lawerens.utils.LawerensUtils.*;
import static xyz.lawerens.utils.LawerensUtils.sendUnderline;

public class EventSetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return false;

        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("setstartlocation")) {
            LawerensParkour.get().getParkourInfo().setStartLocation(((Player) sender).getLocation().toCenterLocation());
            LawerensParkour.get().getParkourConfig().save();
            sendMessageWithPrefix(sender, "EVENTO", "&fActualizaste la localización de inicio.");

        } else if (args[0].equalsIgnoreCase("setlobbylocation")) {
            LawerensParkour.get().getParkourInfo().setLobbyLocation(((Player) sender).getLocation().toCenterLocation());
            LawerensParkour.get().getParkourConfig().save();
            sendMessageWithPrefix(sender, "EVENTO", "&fActualizaste la localización del lobby.");

        }
        else if(args[0].equalsIgnoreCase("enable")){
            if(LawerensParkour.get().getParkourInfo().getFinishMaterial() == null || LawerensParkour.get().getParkourInfo().getStartLocation() == null || LawerensParkour.get().getParkourInfo().getLobbyLocation() == null){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl punto de inicio, lobby y material de meta no están definidos.");
                return false;
            }

            if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING || LawerensParkour.get().getGameManager().isEnable()){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl evento está activado.");
                return false;
            }
            LawerensParkour.get().getGameManager().setEnable(true, sender);
            LawerensEvents.getPlugin(LawerensEvents.class).setEvent(LawerensParkour.get());
        }
        else if(args[0].equalsIgnoreCase("start")){
            if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl evento ya inició. &fUsa &e/lesetup stop&f para detenerlo.");
                return false;
            }
            if(LawerensParkour.get().getGameManager().getPlayers().isEmpty()){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl evento no puede inciar si está vacio.");
                return false;
            }
            LawerensParkour.get().getGameManager().start();
            LawerensParkour.get().getGameManager().cancelTask();
        }
        else if(args[0].equalsIgnoreCase("stop")){
            if(LawerensParkour.get().getGameManager().getState() != ParkourState.INGAME){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl evento no ha iniciado.");
                return false;
            }
            LawerensParkour.get().getGameManager().finish(sender);
        }
        else if(args[0].equalsIgnoreCase("disable")){
            if(LawerensParkour.get().getGameManager().getState() != ParkourState.WAITING){
                sendMessageWithPrefix(sender, "EVENTO", "&cEl evento ya inició. &fUsa &e/lesetup stop&f para detenerlo.");
                return false;
            }
            LawerensParkour.get().getGameManager().setEnable(false, sender);

        }
        else if (args[0].equalsIgnoreCase("setmaterialfinish")) {
            if (args.length < 2) return false;
            Material mat;

            try {
                mat = Material.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                return false;
            }
            LawerensParkour.get().getParkourInfo().setFinishMaterial(mat);
            LawerensParkour.get().getParkourConfig().save();
            sendMessageWithPrefix(sender, "EVENTO", "&fActualizaste el material de finalización.");
        }
        return true;
    }
}
