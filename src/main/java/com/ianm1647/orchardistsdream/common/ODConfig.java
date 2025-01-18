package com.ianm1647.orchardistsdream.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EventBusSubscriber
public class ODConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    private static final Map<String, ForgeConfigSpec.BooleanValue> ITEMS = new HashMap<>();
    public static final String CATEGORY_RECIPE_BOOK = "recipe_book";
    public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE_BOOK_JUICER;

    public ODConfig() {
    }

    public static boolean verify(String item) {
        return contains(item) && ITEMS.get(item).get();
    }

    public static boolean verify(RegistryObject<Item> item) {
        return verify(item.getId().getPath());
    }

    public static boolean verify(Item item) {
        return verify(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath());
    }

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Recipe book").push("recipe_book");
        ENABLE_RECIPE_BOOK_JUICER = COMMON_BUILDER.comment("Should the Juicer have a Recipe Book available on its interface?").define("enableRecipeBookJuicer", true);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void put(ForgeConfigSpec.Builder builder, String name) {
        ITEMS.put(name, builder.define(name, true));
    }

    private static boolean contains(String item) {
        return ITEMS.containsKey(item);
    }
}
