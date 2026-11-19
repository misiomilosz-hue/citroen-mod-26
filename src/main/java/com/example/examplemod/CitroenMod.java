package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<CitroenEntity>> CITROEN_C5 = ENTITIES.register("citroen_c5",
            () -> EntityType.Builder.<CitroenEntity>of(CitroenEntity::new, MobCategory.MISC)
                    .sized(2.0f, 1.5f)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "citroen_c5"))));

    public static final DeferredHolder<EntityType<?>, EntityType<CitroenEntity>> CITROEN_C5_ESTATE = ENTITIES.register("citroen_c5_estate",
            () -> EntityType.Builder.<CitroenEntity>of(CitroenEntity::new, MobCategory.MISC)
                    .sized(2.0f, 1.5f)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "citroen_c5_estate"))));

    public static final DeferredItem<Item> CITROEN_C5_ITEM = ITEMS.registerItem("citroen_c5_item",
            props -> new CitroenSpawnItem(props, CITROEN_C5));

    public static final DeferredItem<Item> CITROEN_C5_ESTATE_ITEM = ITEMS.registerItem("citroen_c5_estate_item",
            props -> new CitroenSpawnItem(props, CITROEN_C5_ESTATE));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CITROEN_TAB = CREATIVE_MODE_TABS.register("citroen_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("Citroen Mod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CITROEN_C5_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CITROEN_C5_ITEM.get());
                output.accept(CITROEN_C5_ESTATE_ITEM.get());
            }).build());

    public CitroenMod(IEventBus modEventBus, ModContainer modContainer) {
        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static class CitroenEntity extends Boat {
        public CitroenEntity(EntityType<? extends Boat> type, Level level) {
            super(type, level, () -> CITROEN_C5_ITEM.get());
        }
    }

    public static class CitroenSpawnItem extends Item {
        private final DeferredHolder<EntityType<?>, EntityType<CitroenEntity>> entityType;

        public CitroenSpawnItem(Properties properties, DeferredHolder<EntityType<?>, EntityType<CitroenEntity>> entityType) {
            super(properties);
            this.entityType = entityType;
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            Level level = context.getLevel();
            if (!level.isClientSide()) {
                Vec3 pos = context.getClickLocation();
                CitroenEntity car = entityType.get().create(level, null, context.getClickedPos().above(), net.minecraft.world.entity.EntitySpawnReason.SPAWN_EGG, true, false);
                if (car != null) {
                    car.setPos(pos.x, pos.y, pos.z);
                    level.addFreshEntity(car);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }
}
