package com.ianm1647.orchardistsdream.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.client.recipebook.JuicerRecipeBookTab;
import com.ianm1647.orchardistsdream.common.registry.ODItems;
import com.ianm1647.orchardistsdream.common.registry.ODRecipeSerializers;
import com.ianm1647.orchardistsdream.common.registry.ODRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class JuicerRecipe implements Recipe<RecipeWrapper> {
   public static final int INPUT_SLOTS = 2;
   private final ResourceLocation id;
   private final String group;
   private final JuicerRecipeBookTab tab;
   private final NonNullList<Ingredient> inputItems;
   private final ItemStack output;
   private final ItemStack container;
   private final float experience;
   private final int juiceTime;

   public JuicerRecipe(ResourceLocation id, String group, @Nullable JuicerRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int juiceTime) {
      this.id = id;
      this.group = group;
      this.tab = tab;
      this.inputItems = inputItems;
      this.output = output;
      if (!container.isEmpty()) {
         this.container = container;
      } else if (!output.getCraftingRemainingItem().isEmpty()) {
         this.container = output.getCraftingRemainingItem();
      } else {
         this.container = ItemStack.EMPTY;
      }
      this.experience = experience;
      this.juiceTime = juiceTime;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public String getGroup() {
      return this.group;
   }

   @Nullable
   public JuicerRecipeBookTab getRecipeBookTab() {
      return this.tab;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.inputItems;
   }

   public ItemStack getResultItem(RegistryAccess access) {
      return this.output;
   }

   public ItemStack getOutputContainer() {
      return this.container;
   }

   public ItemStack assemble(RecipeWrapper inv, RegistryAccess access) {
      return this.output.copy();
   }

   public float getExperience() {
      return this.experience;
   }

   public int getJuiceTime() {
      return this.juiceTime;
   }

   public boolean matches(RecipeWrapper inv, Level level) {
      List<ItemStack> inputs = new ArrayList();
      int i = 0;

      for(int j = 0; j < 2; ++j) {
         ItemStack itemstack = inv.getItem(j);
         if (!itemstack.isEmpty()) {
            ++i;
            inputs.add(itemstack);
         }
      }

      return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= this.inputItems.size();
   }

   public RecipeSerializer<?> getSerializer() {
      return ODRecipeSerializers.JUICING.get();
   }

   public RecipeType<?> getType() {
      return ODRecipeTypes.JUICING.get();
   }

   public ItemStack getToastSymbol() {
      return new ItemStack(ODItems.JUICER.get());
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JuicerRecipe that = (JuicerRecipe)o;
         if (Float.compare(that.getExperience(), this.getExperience()) != 0) {
            return false;
         } else if (this.getJuiceTime() != that.getJuiceTime()) {
            return false;
         } else if (!this.getId().equals(that.getId())) {
            return false;
         } else if (!this.getGroup().equals(that.getGroup())) {
            return false;
         } else if (this.tab != that.tab) {
            return false;
         } else if (!this.inputItems.equals(that.inputItems)) {
            return false;
         } else {
            return this.output.equals(that.output) && this.container.equals(that.container);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.getId().hashCode();
      result = 31 * result + this.getGroup().hashCode();
      result = 31 * result + (this.getRecipeBookTab() != null ? this.getRecipeBookTab().hashCode() : 0);
      result = 31 * result + this.inputItems.hashCode();
      result = 31 * result + this.output.hashCode();
      result = 31 * result + this.container.hashCode();
      result = 31 * result + (this.getExperience() != 0.0F ? Float.floatToIntBits(this.getExperience()) : 0);
      result = 31 * result + this.getJuiceTime();
      return result;
   }

   public static class Serializer implements RecipeSerializer<JuicerRecipe> {
      public JuicerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
         String groupIn = GsonHelper.getAsString(json, "group", "");
         NonNullList<Ingredient> inputItemsIn = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
         if (inputItemsIn.isEmpty()) {
            throw new JsonParseException("No ingredients for cooking recipe");
         } else if (inputItemsIn.size() > 2) {
            throw new JsonParseException("Too many ingredients for cooking recipe! The max is 2");
         } else {
            String tabKeyIn = GsonHelper.getAsString(json, "recipe_book_tab", (String)null);
            JuicerRecipeBookTab tabIn = JuicerRecipeBookTab.findByName(tabKeyIn);
            if (tabKeyIn != null && tabIn == null) {
               OrchardistsDream.LOGGER.warn("Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: " + EnumSet.allOf(JuicerRecipeBookTab.class));
            }

            ItemStack outputIn = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            ItemStack container = GsonHelper.isValidNode(json, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true) : ItemStack.EMPTY;
            float experienceIn = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int juiceTimeIn = GsonHelper.getAsInt(json, "juicingtime", 200);
            return new JuicerRecipe(recipeId, groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, juiceTimeIn);
         }
      }

      private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
         NonNullList<Ingredient> nonnulllist = NonNullList.create();

         for(int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if (!ingredient.isEmpty()) {
               nonnulllist.add(ingredient);
            }
         }

         return nonnulllist;
      }

      @Nullable
      public JuicerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
         String groupIn = buffer.readUtf();
         JuicerRecipeBookTab tabIn = JuicerRecipeBookTab.findByName(buffer.readUtf());
         int i = buffer.readVarInt();
         NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

         for(int j = 0; j < inputItemsIn.size(); ++j) {
            inputItemsIn.set(j, Ingredient.fromNetwork(buffer));
         }

         ItemStack outputIn = buffer.readItem();
         ItemStack container = buffer.readItem();
         float experienceIn = buffer.readFloat();
         int cookTimeIn = buffer.readVarInt();
         return new JuicerRecipe(recipeId, groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
      }

      public void toNetwork(FriendlyByteBuf buffer, JuicerRecipe recipe) {
         buffer.writeUtf(recipe.group);
         buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
         buffer.writeVarInt(recipe.inputItems.size());
         Iterator var3 = recipe.inputItems.iterator();

         while(var3.hasNext()) {
            Ingredient ingredient = (Ingredient)var3.next();
            ingredient.toNetwork(buffer);
         }

         buffer.writeItem(recipe.output);
         buffer.writeItem(recipe.container);
         buffer.writeFloat(recipe.experience);
         buffer.writeVarInt(recipe.juiceTime);
      }
   }
}
