package com.ianm1647.orchardistsdream.common.registry;

import com.google.common.collect.Sets;
import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.utility.ModFoods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ODItems {

    public static final DeferredRegister<Item> ITEMS;
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS;

    public static RegistryObject<Item> JUICER;

    public static RegistryObject<Item> CHILLED_COOKING_POT;

    public static RegistryObject<Item> ORANGE;


    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OrchardistsDream.MODID);
        CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

        JUICER = registerWithTab("juicer", () -> new ItemNameBlockItem(ODBlocks.JUICER.get(), basicItem()));

        CHILLED_COOKING_POT = registerWithTab("chilled_cooking_pot", () -> new ItemNameBlockItem(ODBlocks.CHILLED_COOKING_POT.get(), basicItem()));

        ORANGE = registerWithTab("orange", () -> new Item(basicItem().food(ModFoods.ORANGE)));

    }

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    public static Item.Properties basicItem() {
        return (new Item.Properties());
    }

    public static Item.Properties drinkItem() {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Item.Properties bowlFoodItem() {
        return (new Item.Properties()).craftRemainder(Items.BOWL).stacksTo(16);
    }

}
