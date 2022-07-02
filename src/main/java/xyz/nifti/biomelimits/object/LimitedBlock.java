package xyz.nifti.biomelimits.object;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class LimitedBlock {
    private final Material material;
    private final List<Biome> whitelistedBiomes = new ArrayList<>();
    private final List<Biome> blacklistedBiomes = new ArrayList<>();
    private final boolean placeDisabled;
    private final boolean breakDisabled;

    public LimitedBlock(Material material, boolean placeDisabled, boolean breakDisabled) {
        this.material = material;
        this.placeDisabled = placeDisabled;
        this.breakDisabled = breakDisabled;
    }


    public Material getMaterial() {
        return material;
    }

    public List<Biome> getBlacklistedBiomes() {
        return blacklistedBiomes;
    }

    public List<Biome> getWhitelistedBiomes() {
        return whitelistedBiomes;
    }

    public boolean isBreakDisabled() {
        return breakDisabled;
    }

    public boolean isPlaceDisabled() {
        return placeDisabled;
    }
}
