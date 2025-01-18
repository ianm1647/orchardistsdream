package com.ianm1647.orchardistsdream.client.event;

import com.ianm1647.orchardistsdream.client.recipebook.RecipeCategories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(
        modid = "orchardistsdream",
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientSetupEvents {
    public ClientSetupEvents() {
    }

    @SubscribeEvent
    public static void onRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        RecipeCategories.init(event);
    }

    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {

    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {

    }

    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {

    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {

    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {

    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void registerParticles(RegisterParticleProvidersEvent event) {
    }
}
