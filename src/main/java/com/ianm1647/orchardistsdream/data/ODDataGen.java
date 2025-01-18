package com.ianm1647.orchardistsdream.data;

import com.ianm1647.orchardistsdream.OrchardistsDream;
import com.ianm1647.orchardistsdream.data.loot.ODBlockLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(
        modid = OrchardistsDream.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ODDataGen {
    public ODDataGen() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        ODBlockTags blockTags = new ODBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ODItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new ODRecipes(output));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ODBlockLoot::new, LootContextParamSets.BLOCK))));
        ODBlockStates blockStates = new ODBlockStates(output, helper);
        generator.addProvider(event.includeClient(), blockStates);
    }
}
