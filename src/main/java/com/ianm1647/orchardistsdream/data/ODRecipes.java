package com.ianm1647.orchardistsdream.data;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.data.builder.JuicerRecipeBuilder;
import com.ianm1647.orchardistsdream.data.condition.EnabledCondition;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ODRecipes extends RecipeProvider implements IConditionBuilder {
    public ODRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        wrap(JuicerRecipeBuilder.juicerRecipe(ModItems.APPLE_CIDER.get(), 1, 200, 1.0f, Items.GLASS_BOTTLE)
                    .addIngredient(Items.APPLE)
                    .addIngredient(Items.SUGAR)
                    .unlockedBy("has_apple", has(Items.APPLE)),
                "food/apple_cider", consumer, enabled(ModItems.APPLE_CIDER));
    }













    //below is borrowed from collectors reap for qol

    public void wrap(RecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        wrap(builder, OrchardistsDream.MODID, name, consumer, conds);
    }

    private void wrap(RecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.save(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public void wrap(SingleItemRecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.save(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public void wrap(SmithingTransformRecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(OrchardistsDream.MODID, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.save(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public void wrap(CuttingBoardRecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        wrap(builder, OrchardistsDream.MODID, name, consumer, conds);
    }

    public void wrap(CuttingBoardRecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.build(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public void wrap(CookingPotRecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        wrap(builder, OrchardistsDream.MODID, name, consumer, conds);
    }

    public void wrap(CookingPotRecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.build(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public void wrap(JuicerRecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        wrap(builder, OrchardistsDream.MODID, name, consumer, conds);
    }

    public void wrap(JuicerRecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conds) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conds.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conds));
        } else if (conds.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conds[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.build(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0])
                .generateAdvancement()
                .build(consumer, loc);
    }

    public EnabledCondition enabled(RegistryObject<Item> item) {
        return new EnabledCondition(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get())).getPath());
    }

    public EnabledCondition enabled(String name) {
        return new EnabledCondition(name);
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, RegistryObject<Item> returns, int... count) {
        if (count.length > 0 && count[0] > 1) {
            return ShapelessRecipeBuilder.shapeless(category, returns.get(), count[0]);
        }
        return ShapelessRecipeBuilder.shapeless(category, returns.get());
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, RegistryObject<Item> returns, int... count) {
        if (count.length > 0 && count[0] > 1) {
            return ShapedRecipeBuilder.shaped(category, returns.get(), count[0]);
        }
        return ShapedRecipeBuilder.shaped(category, returns.get());
    }
}
