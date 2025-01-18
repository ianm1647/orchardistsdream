package com.ianm1647.orchardistsdream.client.gui;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nonnull;
import java.util.List;

public class JuicerRecipeBookComponent extends RecipeBookComponent {
    protected static final ResourceLocation RECIPE_BOOK_BUTTONS = new ResourceLocation(OrchardistsDream.MODID, "textures/gui/recipe_book_buttons.png");

    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(0, 0, 28, 18, RECIPE_BOOK_BUTTONS);
    }

    public void hide() {
        this.setVisible(false);
    }

    @Nonnull
    protected Component getRecipeFilterName() {
        return Component.translatable("orchardistsdream.container.recipe_book.juiceable");
    }

    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        NonNullList<Ingredient> ingredientsList = NonNullList.create();
        ingredientsList.addAll(recipe.getIngredients());
        ItemStack resultStack = recipe.getResultItem(RegistryAccess.EMPTY);
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(2).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(resultStack), slots.get(2).x, slots.get(2).y);
        }

        if (recipe instanceof JuicerRecipe juicerRecipe) {
            ItemStack containerStack = juicerRecipe.getOutputContainer();
            if (!containerStack.isEmpty()) {
                this.ghostRecipe.addIngredient(Ingredient.of(containerStack), slots.get(3).x, slots.get(3).y);
            }
        }

        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, ingredientsList.iterator(), 0);
    }
}