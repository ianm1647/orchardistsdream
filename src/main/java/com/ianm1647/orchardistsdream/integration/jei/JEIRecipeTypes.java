package com.ianm1647.orchardistsdream.integration.jei;

import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import mezz.jei.api.recipe.RecipeType;

public class JEIRecipeTypes {
   public static final RecipeType<JuicerRecipe> JUICING = RecipeType.create("orchardistsdream", "juicing", JuicerRecipe.class);
}
