package com.lawerens.parkour.model;

import com.lawerens.core.LawerensUtils;
import com.lawerens.core.configuration.LawerensConfig;
import com.lawerens.parkour.LawerensParkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import static com.lawerens.core.LawerensUtils.sendMessageWithPrefix;

public class ParkourConfig extends LawerensConfig {

    public ParkourConfig(Plugin plugin) {
        super("config", plugin.getDataFolder(), true, plugin);
    }

    public void load() {
        Bukkit.getScheduler().runTaskAsynchronously(LawerensParkour.get(), () -> {
            ConfigurationSection s = asConfig();

            Location l1 = null, l2 = null;
            Material mat = null;

            if (s.getConfigurationSection("SpawnLocation") != null) {
                sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fCargando el punto de inicio con éxito.");
                l1 = LawerensUtils.readLocation(s, "SpawnLocation");
            }

            if (s.getConfigurationSection("LobbyLocation") != null) {
                sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fCargando el punto de Lobby con éxito.");
                l2 = LawerensUtils.readLocation(s, "LobbyLocation");
            }

            if (s.contains("FinishBlock")) {
                sendMessageWithPrefix(Bukkit.getConsoleSender(), "PARKOUR EVENTO", "&fCargando el material del fin...");
                mat = Material.matchMaterial(s.getString("FinishBlock"));
            }

            LawerensParkour.get().getParkourInfo().setStartLocation(l1);
            LawerensParkour.get().getParkourInfo().setLobbyLocation(l2);
            LawerensParkour.get().getParkourInfo().setFinishMaterial(mat);
        });
    }


    public void save(){
        ConfigurationSection s = asConfig();

        if(LawerensParkour.get().getParkourInfo().getStartLocation() != null){
            LawerensUtils.writeLocation(s, "SpawnLocation", LawerensParkour.get().getParkourInfo().getStartLocation());
        }

        if(LawerensParkour.get().getParkourInfo().getLobbyLocation() != null){
            LawerensUtils.writeLocation(s, "LobbyLocation", LawerensParkour.get().getParkourInfo().getLobbyLocation());
        }

        if(LawerensParkour.get().getParkourInfo().getFinishMaterial() != null){
            s.set("FinishBlock", LawerensParkour.get().getParkourInfo().getFinishMaterial().name());
        }

        saveConfig();
    }
}
