package com.ianm1647.orchardistsdream.common.block.entity;

import com.ianm1647.orchardistsdream.common.tag.ODTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface ChillableBlockEntity {
    default boolean isChilled(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.is(ODTags.COLD_SOURCES)) {
            return stateBelow.hasProperty(BlockStateProperties.LIT) ? stateBelow.getValue(BlockStateProperties.LIT) : true;
        } else {
            if (!this.requiresDirectChill() && stateBelow.is(ODTags.COLD_CONDUCTORS)) {
                BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
                if (stateFurtherBelow.is(ODTags.COLD_SOURCES)) {
                    if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT)) {
                        return stateFurtherBelow.getValue(BlockStateProperties.LIT);
                    }
                    return true;
                }
            }

            return false;
        }
    }

    default boolean requiresDirectChill() {
        return false;
    }
}
