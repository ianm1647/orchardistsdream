package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ODTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS;
    public static final RegistryObject<CreativeModeTab> TAB_OD;

    public ODTabs() {
    }

    static {
        CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OrchardistsDream.MODID);
        TAB_OD = CREATIVE_TABS.register(OrchardistsDream.MODID,
                () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + OrchardistsDream.MODID)).icon(
                        () -> new ItemStack(ODItems.ORANGE.get())).displayItems((parameters, output) ->
                        ODItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get()))).build());
    }
}
