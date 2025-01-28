package com.lawerens.parkour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ParkourInfo {

    private @Nullable Location startLocation, lobbyLocation;
    private @Nullable Material finishMaterial;

}
