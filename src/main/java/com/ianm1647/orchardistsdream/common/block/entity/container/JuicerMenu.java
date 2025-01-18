package com.ianm1647.orchardistsdream.common.block.entity.container;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.block.entity.JuicerBlockEntity;
import com.ianm1647.orchardistsdream.common.registry.ODBlocks;
import com.ianm1647.orchardistsdream.common.registry.ODMenuTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Objects;

public class JuicerMenu extends RecipeBookMenu<RecipeWrapper> {
    private int output = 4;
    private int display = 2;
    private int bottle = 3;

    public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOTTLE = new ResourceLocation(OrchardistsDream.MODID, "item/empty_container_slot_bottle");
    public final JuicerBlockEntity tileEntity;
    public final ItemStackHandler inventory;
    private final ContainerData juicerData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;

    public JuicerMenu(int windowId, Inventory playerInventory, JuicerBlockEntity tileEntity, ContainerData juicerDataIn) {
        super((MenuType) ODMenuTypes.JUICER.get(), windowId);
        this.tileEntity = tileEntity;
        this.inventory = tileEntity.getInventory();
        this.juicerData = juicerDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        int startX = 8;
        int startY = 18;
        int inputStartX = 42;
        int inputStartY = 23;
        int borderSlotSize = 18;

        for(int row = 0; row < 2; ++row) {
            for(int column = 0; column < 1; ++column) {
                this.addSlot(new SlotItemHandler(this.inventory, row * 1 + column, inputStartX + column * borderSlotSize, inputStartY + row * borderSlotSize));
            }
        }

        this.addSlot(new JuicerDrinkSlot(this.inventory, display, 118, 26));
        this.addSlot(new SlotItemHandler(this.inventory, bottle, 86, 55) {
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> m_7543_() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, JuicerMenu.EMPTY_CONTAINER_SLOT_BOTTLE);
            }
        });
        this.addSlot(new JuicerResultSlot(playerInventory.player, tileEntity, this.inventory, output, 118, 55));
        int startPlayerInvY = startY * 4 + 12;

        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + row * 9 + column, startX + column * borderSlotSize, startPlayerInvY + row * borderSlotSize));
            }
        }

        for(int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + column * borderSlotSize, 142));
        }

        this.addDataSlots(juicerDataIn);
    }

    private static JuicerBlockEntity getTileEntity(Inventory playerInventory, FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof JuicerBlockEntity) {
            return (JuicerBlockEntity)tileAtPos;
        } else {
            throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
        }
    }

    public JuicerMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
    }

    public boolean stillValid(Player playerIn) {
        return stillValid(this.canInteractWithCallable, playerIn, (Block) ODBlocks.JUICER.get());
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        int indexMealDisplay = 2;
        int indexContainerInput = 3;
        int indexOutput = 4;
        int startPlayerInv = indexOutput + 1;
        int endPlayerInv = startPlayerInv + 36;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == indexOutput) {
                if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index > indexOutput) {
                if (itemstack1.getItem() == Items.GLASS_BOTTLE && !this.moveItemStackTo(itemstack1, indexContainerInput, indexContainerInput + 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (!this.moveItemStackTo(itemstack1, 0, indexMealDisplay, false)) {
                    return ItemStack.EMPTY;
                }

                if (!this.moveItemStackTo(itemstack1, indexContainerInput, indexOutput, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBrewProgressionScaled() {
        int i = this.juicerData.get(0);
        int j = this.juicerData.get(1);
        return j != 0 && i != 0 ? i * 40 / j : 0;
    }

    public void fillCraftSlotsStackedContents(StackedContents helper) {
        for(int i = 0; i < this.inventory.getSlots(); ++i) {
            helper.accountSimpleStack(this.inventory.getStackInSlot(i));
        }
    }

    public void clearCraftingContent() {
        for(int i = 0; i < 2; ++i) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean recipeMatches(Recipe<? super RecipeWrapper> recipe) {
        return recipe.matches(new RecipeWrapper(this.inventory), this.level);
    }

    public int getResultSlotIndex() {
        return 3;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 2;
    }

    public int getSize() {
        return 3;
    }

    public RecipeBookType getRecipeBookType() {
        return OrchardistsDream.RECIPE_TYPE_JUICING;
    }

    public boolean shouldMoveToInventory(int slot) {
        return slot < this.getGridWidth() * this.getGridHeight();
    }
}
