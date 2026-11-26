package io.github.sebasjuricek.exora.materials;

import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.equipment.LineMeterItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WorkshopStock {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Exora.MOD_ID);

    public static final DeferredItem<Item> MALLET = ITEMS.registerSimpleItem("mallet", new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> TIMBER_FRAME = ITEMS.registerSimpleItem("timber_frame");
    public static final DeferredItem<Item> IRON_PLATE = ITEMS.registerSimpleItem("iron_plate");
    public static final DeferredItem<Item> SHAFT = ITEMS.registerSimpleItem("shaft");
    public static final DeferredItem<Item> COPPER_WINDING = ITEMS.registerSimpleItem("copper_winding");
    public static final DeferredItem<Item> DRIVE_ASSEMBLY = ITEMS.registerSimpleItem("drive_assembly");
    public static final DeferredItem<Item> MACHINE_CASING = ITEMS.registerSimpleItem("machine_casing");
    public static final DeferredItem<Item> WOUND_CORE = ITEMS.registerSimpleItem("wound_core");
    public static final DeferredItem<LineMeterItem> LINE_METER = ITEMS.registerItem("line_meter",
            LineMeterItem::new, new Item.Properties().stacksTo(1));

    private WorkshopStock() {}

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
