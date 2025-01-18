package com.ianm1647.orchardistsdream.common;

import com.ianm1647.orchardistsdream.common.registry.ODItems;
import com.ianm1647.orchardistsdream.data.condition.EnabledCondition;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class ODCommonSetup {
    public ODCommonSetup() {
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(ODCommonSetup::registerCompostables);
    }

    @SubscribeEvent
    public void registerSerializers(RegisterEvent e) {
        if (e.getRegistryKey() == ForgeRegistries.RECIPE_SERIALIZERS.getRegistryKey()) {
            CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);
        }
    }

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(ODItems.ORANGE.get(), 0.3F);
    }

}
