package com.ianm1647.orchardistsdream;

import com.ianm1647.orchardistsdream.client.ODClientSetup;
import com.ianm1647.orchardistsdream.common.ODCommonSetup;
import com.ianm1647.orchardistsdream.common.ODConfig;
import com.ianm1647.orchardistsdream.common.registry.*;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OrchardistsDream.MODID)
public class OrchardistsDream
{
    public static final String MODID = "orchardistsdream";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final RecipeBookType RECIPE_TYPE_JUICING = RecipeBookType.create("JUICING");
    public static final RecipeBookType RECIPE_TYPE_CHILLING = RecipeBookType.create("CHILLING");

    public OrchardistsDream() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ODCommonSetup::init);
        modEventBus.register(new ODCommonSetup());
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ODClientSetup::init);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ODConfig.COMMON_CONFIG);
        ODBlocks.BLOCKS.register(modEventBus);
        ODItems.ITEMS.register(modEventBus);
        ODBlockEntityTypes.TILES.register(modEventBus);
        ODMenuTypes.MENU_TYPES.register(modEventBus);
        ODRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ODRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ODTabs.CREATIVE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
