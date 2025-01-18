package com.ianm1647.orchardistsdream.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import java.util.function.Supplier;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import com.ianm1647.orchardistsdream.common.registry.ODItems;
import com.ianm1647.orchardistsdream.common.registry.ODRecipeTypes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RecipeCategories {
    public static final Supplier<RecipeBookCategories> JUICING_SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("JUICING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> JUICING_DRINKS = Suppliers.memoize(
            () -> RecipeBookCategories.create("JUICING_DRINKS", new ItemStack(Items.HONEY_BOTTLE)));
    public static final Supplier<RecipeBookCategories> JUICING_MISC = Suppliers.memoize(
            () -> RecipeBookCategories.create("JUICING_MISC", new ItemStack(Items.APPLE)));
    public static final Supplier<RecipeBookCategories> CHILLING_SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("CHILLING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> CHILLING_DRINKS = Suppliers.memoize(
            () -> RecipeBookCategories.create("CHILLING_DRINKS", new ItemStack(Items.HONEY_BOTTLE)));
    public static final Supplier<RecipeBookCategories> CHILLING_MISC = Suppliers.memoize(
            () -> RecipeBookCategories.create("CHILLING_MISC", new ItemStack(Items.APPLE)));

    public RecipeCategories() {
    }

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(OrchardistsDream.RECIPE_TYPE_JUICING, ImmutableList.of(JUICING_SEARCH.get(), JUICING_DRINKS.get(), JUICING_MISC.get()));
        event.registerAggregateCategory(JUICING_SEARCH.get(), ImmutableList.of(JUICING_DRINKS.get(), JUICING_MISC.get()));
        event.registerRecipeCategoryFinder(ODRecipeTypes.JUICING.get(), (recipe) -> {
            if (recipe instanceof JuicerRecipe juicerRecipe) {
                JuicerRecipeBookTab tab = juicerRecipe.getRecipeBookTab();
                if (tab != null) {
                    RecipeBookCategories var10000;
                    switch (tab) {
                        case DRINKS -> var10000 = JUICING_DRINKS.get();
                        case MISC -> var10000 = JUICING_MISC.get();
                        default -> throw new IncompatibleClassChangeError();
                    }

                    return var10000;
                }
            }

            return JUICING_MISC.get();
        });
        event.registerBookCategories(OrchardistsDream.RECIPE_TYPE_CHILLING, ImmutableList.of(CHILLING_SEARCH.get(), CHILLING_DRINKS.get(), CHILLING_MISC.get()));
        event.registerAggregateCategory(CHILLING_SEARCH.get(), ImmutableList.of(CHILLING_DRINKS.get(), CHILLING_MISC.get()));
        event.registerRecipeCategoryFinder(ODRecipeTypes.CHILLING.get(), (recipe) -> {
            if (recipe instanceof JuicerRecipe juicerRecipe) {
                JuicerRecipeBookTab tab = juicerRecipe.getRecipeBookTab();
                if (tab != null) {
                    RecipeBookCategories var10000;
                    switch (tab) {
                        case DRINKS -> var10000 = CHILLING_DRINKS.get();
                        case MISC -> var10000 = CHILLING_MISC.get();
                        default -> throw new IncompatibleClassChangeError();
                    }

                    return var10000;
                }
            }

            return CHILLING_MISC.get();
        });
    }
}
