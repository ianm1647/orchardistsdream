package com.ianm1647.orchardistsdream.data;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ODItemTags extends ItemTagsProvider {
    public ODItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, OrchardistsDream.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {

    }

    private void registerModTags() {

    }

    private void registerForgeTags() {

    }

    public void registerCompatibilityTags() {

    }
}