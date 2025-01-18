package com.ianm1647.orchardistsdream.client.gui;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.ODConfig;
import com.ianm1647.orchardistsdream.common.block.entity.container.JuicerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class JuicerScreen extends AbstractContainerScreen<JuicerMenu> implements RecipeUpdateListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(OrchardistsDream.MODID, "textures/gui/juicer.png");
    private static final Rectangle PROGRESS_ARROW = new Rectangle(71, 25, 0, 17);
    private final JuicerRecipeBookComponent recipeBookComponent = new JuicerRecipeBookComponent();
    private boolean widthTooNarrow;

    public JuicerScreen(JuicerMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.titleLabelX = 35;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        if (ODConfig.ENABLE_RECIPE_BOOK_JUICER.get()) {
            this.addRenderableWidget(new ImageButton(this.leftPos + 8, this.height / 2 - 48, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, button -> {
                this.recipeBookComponent.toggleVisibility();
                this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                button.setPosition(this.leftPos + 8, this.height / 2 - 48);
            }));
        } else {
            this.recipeBookComponent.hide();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        }
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
    }

    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(ms, partialTicks, mouseX, mouseY);
            this.recipeBookComponent.render(ms, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookComponent.render(ms, mouseX, mouseY, partialTicks);
            super.render(ms, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(ms, this.leftPos, this.topPos, false, partialTicks);
        }

        this.renderMealDisplayTooltip(ms, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(ms, this.leftPos, this.topPos, mouseX, mouseY);
    }

    protected void renderMealDisplayTooltip(GuiGraphics ms, int mouseX, int mouseY) {
        if (this.minecraft != null
                && this.minecraft.player != null
                && this.menu.getCarried().isEmpty()
                && this.hoveredSlot != null
                && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == 2) {
                List<Component> tooltip = new ArrayList();
                ItemStack mealStack = this.hoveredSlot.getItem();
                tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().color));
                ItemStack containerStack = this.menu.tileEntity.getContainer();
                String container = !containerStack.isEmpty() ? containerStack.getItem().getDescription().getString() : "";
                tooltip.add(Component.translatable("farmersdelight.container.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
                ms.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            } else {
                ms.renderTooltip(this.font, this.hoveredSlot.getItem(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
            ms.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            int l = this.menu.getBrewProgressionScaled();
            ms.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 0, l + 1, PROGRESS_ARROW.height);
        }
    }

    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonId)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, buttonId);
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonIdx) {
        boolean flag = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.imageWidth) || mouseY >= (double) (y + this.imageHeight);
        return flag && this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonIdx);
    }

    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public void removed() {
        this.recipeBookComponent.hide();
        super.removed();
    }

    @Nonnull
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
