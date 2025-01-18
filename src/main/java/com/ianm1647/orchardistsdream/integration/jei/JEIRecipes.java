package com.ianm1647.orchardistsdream.integration.jei;

import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import com.ianm1647.orchardistsdream.common.registry.ODRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class JEIRecipes {
   private final RecipeManager recipeManager;

   public JEIRecipes() {
      Minecraft minecraft = Minecraft.getInstance();
      ClientLevel level = minecraft.level;
      if (level != null) {
         this.recipeManager = level.getRecipeManager();
      } else {
         throw new NullPointerException("minecraft world must not be null.");
      }
   }

   public List<JuicerRecipe> getJuicerRecipes() {
      return this.recipeManager.getAllRecipesFor((RecipeType) ODRecipeTypes.JUICING.get()).stream().toList();
   }
}
