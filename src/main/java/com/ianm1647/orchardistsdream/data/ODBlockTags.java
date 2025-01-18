package com.ianm1647.orchardistsdream.data;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.tag.ODTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ODBlockTags extends BlockTagsProvider {
    public ODBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, OrchardistsDream.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerMinecraftTags();
        this.registerForgeTags();
        this.registerCompatibilityTags();
        this.registerBlockMineables();
    }

    protected void registerBlockMineables() {

    }

    protected void registerMinecraftTags() {

    }

    protected void registerForgeTags() {

    }

    protected void registerModTags() {
        tag(ODTags.COLD_SOURCES).add(
                Blocks.ICE,
                Blocks.BLUE_ICE,
                Blocks.PACKED_ICE,
                Blocks.SNOW_BLOCK);
        tag(ODTags.COLD_CONDUCTORS).add(
                Blocks.HOPPER);
    }

    private void registerCompatibilityTags() {

    }
}
