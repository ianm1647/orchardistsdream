package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.crafting.ChillingRecipe;
import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ODRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    public static final RegistryObject<RecipeType<JuicerRecipe>> JUICING;
    public static final RegistryObject<RecipeType<ChillingRecipe>> CHILLING;

    public ODRecipeTypes() {
    }

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<T>() {
            public String toString() {
                return "orchardistsdream:" + identifier;
            }
        };
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, OrchardistsDream.MODID);
        JUICING = RECIPE_TYPES.register("juicing", () -> registerRecipeType("juicing"));
        CHILLING = RECIPE_TYPES.register("chilling", () -> registerRecipeType("chilling"));
    }
}
