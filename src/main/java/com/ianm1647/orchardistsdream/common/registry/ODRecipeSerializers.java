package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.crafting.ChillingRecipe;
import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ODRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final RegistryObject<RecipeSerializer<?>> JUICING;
    public static final RegistryObject<RecipeSerializer<?>> CHILLING;

    public ODRecipeSerializers() {
    }

    static {
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OrchardistsDream.MODID);
        JUICING = RECIPE_SERIALIZERS.register("juicing", JuicerRecipe.Serializer::new);
        CHILLING = RECIPE_SERIALIZERS.register("chilling", ChillingRecipe.Serializer::new);
    }
}

