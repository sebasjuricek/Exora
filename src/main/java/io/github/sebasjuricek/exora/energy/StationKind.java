package io.github.sebasjuricek.exora.energy;

import net.minecraft.world.inventory.MenuType;

public enum StationKind {
    GENERATOR(1, 1024), MILL(2, 48), BUFFER(0, 4096);

    public final int slots;
    public final int capacity;

    StationKind(int slots, int capacity) {
        this.slots = slots;
        this.capacity = capacity;
    }

    public MenuType<StationMenu> menu() {
        return switch (this) {
            case GENERATOR -> PowerWorkshop.GENERATOR_MENU.get();
            case MILL -> PowerWorkshop.MILL_MENU.get();
            case BUFFER -> PowerWorkshop.BUFFER_MENU.get();
        };
    }
}
