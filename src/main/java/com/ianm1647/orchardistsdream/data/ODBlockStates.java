package com.ianm1647.orchardistsdream.data;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.registry.ODBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class ODBlockStates extends BlockStateProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public ODBlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, OrchardistsDream.MODID, existingFileHelper);
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return new ResourceLocation(OrchardistsDream.MODID, "block/" + path);
    }

    public ModelFile existingModel(Block block) {
        return new ModelFile.ExistingModelFile(this.resourceBlock(this.blockName(block)), this.models().existingFileHelper);
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(this.resourceBlock(path), this.models().existingFileHelper);
    }

    protected void registerStatesAndModels() {
        this.customHorizontalBlock(ODBlocks.JUICER.get(), ($) -> this.existingModel(ODBlocks.JUICER.get()));

    }

    public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
        String var10000 = this.blockName(block);
        String formattedName = var10000 + (suffix.isEmpty() ? "" : "_" + suffix);
        return ConfiguredModel.allYRotations(this.models().cubeAll(formattedName, this.resourceBlock(formattedName)), 0, false);
    }

    public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            Direction dir = (Direction)state.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile)modelFunc.apply(state)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int)dir.toYRot() + 180) % 360).build();
        }, ignored);
    }

    public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            return ConfiguredModel.builder().modelFile((ModelFile)modelFunc.apply(state)).rotationY(((int)((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build();
        }, ignored);
    }
}
