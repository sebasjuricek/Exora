package io.github.sebasjuricek.exora.energy;

import io.github.sebasjuricek.exora.machines.powered.CoilMillEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class StationMenu extends AbstractContainerMenu {
    public static final int ENERGY = 0, PROGRESS = 1, DURATION = 2, STATUS = 3, WORKPIECE = 4, WORKPIECE_HIGH = 5, DATA_COUNT = 6;
    private final Container station;
    private final ContainerData data;
    private boolean droppingFromTray;
    public final StationKind kind;

    public StationMenu(StationKind kind, int id, Inventory inventory) {
        this(kind, id, inventory, new SimpleContainer(kind.slots), new SimpleContainerData(DATA_COUNT));
    }

    StationMenu(StationKind kind, int id, Inventory inventory, Container station, ContainerData data) {
        super(kind.menu(), id);
        this.kind = kind;
        this.station = station;
        this.data = data;
        if (kind.slots > 0) addSlot(new Slot(station, 0, 27, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return kind == StationKind.MILL || stack.is(Items.CHARCOAL);
            }
        });
        if (kind == StationKind.MILL) addSlot(new Slot(station, CoilMillEntity.TRAY, 175, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public void onTake(Player player, ItemStack taken) {
                super.onTake(player, taken);
                if (!droppingFromTray && station instanceof CoilMillEntity mill && player instanceof ServerPlayer server) {
                    mill.collected(server, taken);
                }
            }
        });
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 29 + col * 18, 132 + row * 18));
        }
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 29 + col * 18, 190));
        addDataSlots(data);
    }

    public int value(int index) { return data.get(index); }

    @Override
    public boolean stillValid(Player player) { return station.stillValid(player); }

    @Override
    public void clicked(int slot, int button, ClickType action, Player player) {
        // Dropping from a slot also invokes onTake, but nothing entered the player's possession.
        droppingFromTray = action == ClickType.THROW && slot == CoilMillEntity.TRAY;
        try {
            super.clicked(slot, button, action, player);
        } finally {
            droppingFromTray = false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size() || !stillValid(player)) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack original = stack.copy();
        if (index < kind.slots) {
            if (!moveItemStackTo(stack, kind.slots, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (kind.slots == 0 || !slots.getFirst().mayPlace(stack) || !moveItemStackTo(stack, 0, 1, false)) {
                int hotbar = kind.slots + 27;
                boolean moved = index < hotbar ? moveItemStackTo(stack, hotbar, slots.size(), false)
                        : moveItemStackTo(stack, kind.slots, hotbar, false);
                if (!moved) return ItemStack.EMPTY;
            }
        }
        int taken = original.getCount() - stack.getCount();
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, original.copyWithCount(taken));
        return original;
    }
}
