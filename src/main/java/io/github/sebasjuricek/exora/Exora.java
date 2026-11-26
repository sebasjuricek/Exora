package io.github.sebasjuricek.exora;

import io.github.sebasjuricek.exora.machines.ManualWorkshop;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Exora.MOD_ID)
public final class Exora {
    public static final String MOD_ID = "exora";

    public Exora(IEventBus modBus) {
        WorkshopStock.register(modBus);
        ManualWorkshop.register(modBus);
        PowerWorkshop.register(modBus);
        DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
        tabs.register("workshop", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.exora.workshop"))
                .icon(() -> ManualWorkshop.WORKSHOP_BENCH.toStack())
                .displayItems((parameters, output) -> {
                    output.accept(ManualWorkshop.WORKSHOP_BENCH);
                    output.accept(WorkshopStock.MALLET);
                    output.accept(WorkshopStock.TIMBER_FRAME);
                    output.accept(ManualWorkshop.HAND_PRESS);
                    output.accept(WorkshopStock.IRON_PLATE);
                    output.accept(WorkshopStock.SHAFT);
                    output.accept(WorkshopStock.COPPER_WINDING);
                    output.accept(WorkshopStock.DRIVE_ASSEMBLY);
                    output.accept(WorkshopStock.MACHINE_CASING);
                    output.accept(PowerWorkshop.STARTER_GENERATOR);
                    output.accept(PowerWorkshop.COIL_MILL);
                    output.accept(WorkshopStock.WOUND_CORE);
                    output.accept(WorkshopStock.LINE_METER);
                    output.accept(PowerWorkshop.ENERGY_BUFFER);
                }).build());
        tabs.register(modBus);
    }
}
