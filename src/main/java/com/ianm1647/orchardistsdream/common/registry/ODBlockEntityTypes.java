package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.block.entity.ChillableBlockEntity;
import com.ianm1647.orchardistsdream.common.block.entity.ChilledCookingPotBlockEntity;
import com.ianm1647.orchardistsdream.common.block.entity.JuicerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ODBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final RegistryObject<BlockEntityType<JuicerBlockEntity>> JUICER;
    public static final RegistryObject<BlockEntityType<ChilledCookingPotBlockEntity>> CHILLED_COOKING_POT;

    public ODBlockEntityTypes() {
    }

    static {
        TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OrchardistsDream.MODID);
        JUICER = TILES.register("juicer",
                () -> BlockEntityType.Builder.of(JuicerBlockEntity::new, new Block[]{ODBlocks.JUICER.get()}).build(null));
        CHILLED_COOKING_POT = TILES.register("chilled_cooking_pot",
                () -> BlockEntityType.Builder.of(ChilledCookingPotBlockEntity::new, new Block[]{ODBlocks.CHILLED_COOKING_POT.get()}).build(null));
    }
}
