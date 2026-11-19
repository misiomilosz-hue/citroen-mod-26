package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CitroenMod.MODID)
public class CitroenMod {
    public static final String MODID = "citroenmod";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<Item> CITROEN_C5_ITEM = ITEMS.registerSimpleItem("citroen_c5_item");
    public static final DeferredItem<Item> CITROEN_C5_ESTATE_ITEM = ITEMS.registerSimpleItem("citroen_c5_estate_item");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CITROEN_TAB = CREATIVE_MODE_TABS.register("citroen_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("Citroen Mod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CITROEN_C5_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CITROEN_C5_ITEM.get());
                output.accept(CITROEN_C5_ESTATE_ITEM.get());
            }).build());

    public CitroenMod(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
