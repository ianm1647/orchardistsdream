package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.block.ChilledCookingPotBlock;
import com.ianm1647.orchardistsdream.common.block.JuicerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ODBlocks {

    public static final DeferredRegister<Block> BLOCKS;

    public static final RegistryObject<Block> JUICER;

    public static final RegistryObject<Block> CHILLED_COOKING_POT;

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OrchardistsDream.MODID);

        JUICER = BLOCKS.register("juicer", JuicerBlock::new);

        CHILLED_COOKING_POT = BLOCKS.register("chilled_cooking_pot",
                () -> new ChilledCookingPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
    }

}
