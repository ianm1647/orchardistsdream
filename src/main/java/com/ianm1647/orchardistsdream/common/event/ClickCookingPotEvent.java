package com.ianm1647.orchardistsdream.common.event;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.registry.ODBlocks;
import com.ianm1647.orchardistsdream.common.tag.ODTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModItems;

@Mod.EventBusSubscriber(modid = OrchardistsDream.MODID)
public class ClickCookingPotEvent {

    @SubscribeEvent
    public static InteractionResult onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        if (stack.is(ModItems.COOKING_POT.get()) && state.is(ODTags.COLD_SOURCES)) {
            BlockState block = ODBlocks.CHILLED_COOKING_POT.get().defaultBlockState();
            Direction face = event.getFace();
            if (face == Direction.UP) {
                level.setBlock(pos.above(), block, 3);
                level.playSound(player, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                player.swing(player.getUsedItemHand());
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.CONSUME;
    }
}
