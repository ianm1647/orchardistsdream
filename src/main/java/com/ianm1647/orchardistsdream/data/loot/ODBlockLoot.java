package com.ianm1647.orchardistsdream.data.loot;

import com.ianm1647.orchardistsdream.common.registry.ODBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vectorwing.farmersdelight.common.loot.function.CopyMealFunction;

import java.util.HashSet;
import java.util.Set;

public class ODBlockLoot extends BlockLootSubProvider {
    private final Set<Block> generatedLootTables = new HashSet();

    public ODBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    protected void generate() {
        this.add(ODBlocks.JUICER.get(),
                (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyMealFunction.builder())))));
    }

    protected void dropNamedContainer(Block block) {
        this.add(block, this::createNameableBlockEntityTable);
    }

    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    protected Iterable<Block> getKnownBlocks() {
        return this.generatedLootTables;
    }
}
