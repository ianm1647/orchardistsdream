package com.ianm1647.orchardistsdream.common.tag;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ODTags {

    public static final TagKey<Block> COLD_SOURCES = modBlockTag("cold_sources");
    public static final TagKey<Block> COLD_CONDUCTORS = modBlockTag("cold_conductors");

    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation(OrchardistsDream.MODID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(new ResourceLocation(OrchardistsDream.MODID, path));
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(OrchardistsDream.MODID, path));
    }

}
