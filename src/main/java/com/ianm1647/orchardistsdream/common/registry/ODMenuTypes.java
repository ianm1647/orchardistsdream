package com.ianm1647.orchardistsdream.common.registry;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.common.block.entity.container.ChilledCookingPotMenu;
import com.ianm1647.orchardistsdream.common.block.entity.container.JuicerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ODMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES;
    public static final RegistryObject<MenuType<JuicerMenu>> JUICER;
    public static final RegistryObject<MenuType<ChilledCookingPotMenu>> CHILLED_COOKING_POT;

    public ODMenuTypes() {
    }

    static {
        MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OrchardistsDream.MODID);
        JUICER = MENU_TYPES.register("juicer", () -> IForgeMenuType.create(JuicerMenu::new));
        CHILLED_COOKING_POT = MENU_TYPES.register("chilled_cooking_pot", () -> IForgeMenuType.create(ChilledCookingPotMenu::new));
    }
}

