package io.github.sebasjuricek.exora.energy;

import com.mojang.serialization.Codec;
import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.machines.powered.CoilMillBlock;
import io.github.sebasjuricek.exora.machines.powered.CoilMillEntity;
import io.github.sebasjuricek.exora.machines.powered.MillingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class PowerWorkshop {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Exora.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Exora.MOD_ID);
    private static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Exora.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_ENERGY =
            COMPONENTS.registerComponentType("stored_energy", builder -> builder
                    .persistent(Codec.intRange(0, StationKind.BUFFER.capacity)).networkSynchronized(ByteBufCodecs.VAR_INT));
    private static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Exora.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Exora.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Exora.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Exora.MOD_ID);

    public static final DeferredBlock<PowerIndicatorBlock> STARTER_GENERATOR = BLOCKS.register("starter_generator",
            () -> new PowerIndicatorBlock(StationKind.GENERATOR, lampProperties()));
    public static final DeferredBlock<CoilMillBlock> COIL_MILL = BLOCKS.register("coil_mill", () -> new CoilMillBlock(properties()));
    public static final DeferredBlock<PowerIndicatorBlock> ENERGY_BUFFER = BLOCKS.register("energy_buffer",
            () -> new PowerIndicatorBlock(StationKind.BUFFER, lampProperties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorEntity>> GENERATOR_ENTITY = ENTITIES.register("starter_generator",
            () -> BlockEntityType.Builder.of(GeneratorEntity::new, STARTER_GENERATOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoilMillEntity>> MILL_ENTITY = ENTITIES.register("coil_mill",
            () -> BlockEntityType.Builder.of(CoilMillEntity::new, COIL_MILL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BufferEntity>> BUFFER_ENTITY = ENTITIES.register("energy_buffer",
            () -> BlockEntityType.Builder.of(BufferEntity::new, ENERGY_BUFFER.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<StationMenu>> GENERATOR_MENU = menu("starter_generator", StationKind.GENERATOR);
    public static final DeferredHolder<MenuType<?>, MenuType<StationMenu>> MILL_MENU = menu("coil_mill", StationKind.MILL);
    public static final DeferredHolder<MenuType<?>, MenuType<StationMenu>> BUFFER_MENU = menu("energy_buffer", StationKind.BUFFER);
    public static final DeferredHolder<RecipeType<?>, RecipeType<MillingRecipe>> MILLING = TYPES.register("milling",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Exora.MOD_ID, "milling")));
    public static final DeferredHolder<RecipeSerializer<?>, MillingRecipe.Serializer> MILLING_SERIALIZER =
            SERIALIZERS.register("milling", MillingRecipe.Serializer::new);

    private PowerWorkshop() {}

    private static BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F).sound(SoundType.METAL)
                .requiresCorrectToolForDrops().noOcclusion();
    }

    private static BlockBehaviour.Properties lampProperties() {
        return properties().lightLevel(state -> state.getValue(PowerIndicatorBlock.LIT) ? 5 : 0);
    }

    private static DeferredHolder<MenuType<?>, MenuType<StationMenu>> menu(String id, StationKind kind) {
        return MENUS.register(id, () -> new MenuType<>((window, inventory) -> new StationMenu(kind, window, inventory),
                FeatureFlags.DEFAULT_FLAGS));
    }

    public static void register(IEventBus bus) {
        ITEMS.registerSimpleBlockItem(STARTER_GENERATOR);
        ITEMS.registerSimpleBlockItem(COIL_MILL);
        ITEMS.registerItem("energy_buffer", p -> new BufferItem(ENERGY_BUFFER.get(), p));
        BLOCKS.register(bus);
        COMPONENTS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        MENUS.register(bus);
        TYPES.register(bus);
        SERIALIZERS.register(bus);
        bus.addListener(PowerWorkshop::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, GENERATOR_ENTITY.get(), (be, side) -> be.energy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MILL_ENTITY.get(), (be, side) -> be.energy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BUFFER_ENTITY.get(), (be, side) -> be.energy());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GENERATOR_ENTITY.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MILL_ENTITY.get(), SidedInvWrapper::new);
    }
}
