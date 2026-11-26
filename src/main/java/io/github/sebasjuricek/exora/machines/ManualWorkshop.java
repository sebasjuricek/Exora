package io.github.sebasjuricek.exora.machines;

import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.machines.bench.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.*;

public final class ManualWorkshop {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Exora.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Exora.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Exora.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Exora.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Exora.MOD_ID);

    public static final DeferredBlock<BenchBlock> WORKSHOP_BENCH = BLOCKS.registerBlock("workshop_bench",
            BenchBlock::new, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.WOOD).noOcclusion());
    public static final DeferredBlock<PressBlock> HAND_PRESS = BLOCKS.registerBlock("hand_press",
            PressBlock::new, BlockBehaviour.Properties.of().strength(3f).sound(SoundType.STONE).noOcclusion());
    public static final DeferredHolder<MenuType<?>, MenuType<WorkshopMenu>> MENU = MENUS.register("workshop",
            () -> IMenuTypeExtension.create((id, inventory, buf) -> new WorkshopMenu(id, inventory, buf.readBlockPos(), buf.readBoolean())));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ManualRecipe>> MANUAL = TYPES.register("manual",
            () -> new RecipeType<>() { @Override public String toString() { return "exora:manual"; } });
    public static final DeferredHolder<RecipeSerializer<?>, ManualRecipe.Serializer> SERIALIZER =
            SERIALIZERS.register("manual", ManualRecipe.Serializer::new);

    private ManualWorkshop() {}

    public static void register(IEventBus bus) {
        ITEMS.registerSimpleBlockItem(WORKSHOP_BENCH);
        ITEMS.registerSimpleBlockItem(HAND_PRESS);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        MENUS.register(bus);
        TYPES.register(bus);
        SERIALIZERS.register(bus);
    }
}
