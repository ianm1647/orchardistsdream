package com.ianm1647.orchardistsdream.client;

import com.ianm1647.orchardistsdream.client.gui.ChilledCookingPotScreen;
import com.ianm1647.orchardistsdream.client.gui.JuicerScreen;
import com.ianm1647.orchardistsdream.common.registry.ODMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ODClientSetup {
    public ODClientSetup() {
    }

    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ODMenuTypes.JUICER.get(), JuicerScreen::new);
            MenuScreens.register(ODMenuTypes.CHILLED_COOKING_POT.get(), ChilledCookingPotScreen::new);
        });
    }
}
