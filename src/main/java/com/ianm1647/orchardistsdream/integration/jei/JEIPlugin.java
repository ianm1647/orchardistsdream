package com.ianm1647.orchardistsdream.integration.jei;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.client.gui.JuicerScreen;
import com.ianm1647.orchardistsdream.common.block.entity.container.JuicerMenu;
import com.ianm1647.orchardistsdream.common.registry.ODItems;
import com.ianm1647.orchardistsdream.common.registry.ODMenuTypes;
import com.ianm1647.orchardistsdream.integration.jei.category.JuicingRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@JeiPlugin
public class JEIPlugin implements IModPlugin {
   private static final ResourceLocation ID = new ResourceLocation(OrchardistsDream.MODID, "jei_plugin");

   public void registerCategories(IRecipeCategoryRegistration registry) {
      registry.addRecipeCategories(new JuicingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
   }

   public void registerRecipes(IRecipeRegistration registration) {
      JEIRecipes modRecipes = new JEIRecipes();
      registration.addRecipes(JEIRecipeTypes.JUICING, modRecipes.getJuicerRecipes());
   }

   public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
      registration.addRecipeCatalyst(new ItemStack(ODItems.JUICER.get()), JEIRecipeTypes.JUICING);
   }

   public void registerGuiHandlers(IGuiHandlerRegistration registration) {
      registration.addRecipeClickArea(JuicerScreen.class, 71, 25, 30, 17, JEIRecipeTypes.JUICING);
   }

   public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
      registration.addRecipeTransferHandler(JuicerMenu.class, ODMenuTypes.JUICER.get(), JEIRecipeTypes.JUICING, 0, 2, 5, 36);
   }

   public ResourceLocation getPluginUid() {
      return ID;
   }
}
