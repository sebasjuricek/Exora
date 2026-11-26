package io.github.sebasjuricek.exora.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PowerStationEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] EMPTY = {}, INPUT = {0}, OUTPUT = {1};
    protected final StationKind kind;
    protected final StationCharge charge;
    private NonNullList<ItemStack> inventory;

    protected PowerStationEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, StationKind kind) {
        super(type, pos, state);
        this.kind = kind;
        this.charge = new StationCharge(kind, this::setChanged);
        this.inventory = NonNullList.withSize(kind.slots, ItemStack.EMPTY);
    }

    public StationCharge energy() {
        return charge;
    }

    public abstract void serverTick(ServerLevel level);

    public void dropStock(Level level) {
        Containers.dropContents(level, worldPosition, this);
    }

    @Override
    public StationMenu createMenu(int id, Inventory playerInventory) {
        ContainerData values = new ContainerData() {
            public int get(int index) { return displayValue(index); }
            public void set(int index, int value) { }
            public int getCount() { return StationMenu.DATA_COUNT; }
        };
        return new StationMenu(kind, id, playerInventory, this, values);
    }

    protected int displayValue(int index) {
        return index == StationMenu.ENERGY ? charge.getEnergyStored() : 0;
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (kind == StationKind.BUFFER) return EMPTY;
        return kind == StationKind.MILL && side == Direction.DOWN ? OUTPUT : INPUT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && (kind == StationKind.MILL || kind == StationKind.GENERATOR && stack.is(Items.CHARCOAL));
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return kind == StationKind.MILL && slot == 1;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, inventory, registries);
        tag.putInt("Energy", charge.getEnergyStored());
        tag.putInt("GeneratorEnergy", charge.generatorEnergy());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory = NonNullList.withSize(kind.slots, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, inventory, registries);
        charge.restore(tag.getInt("Energy"));
        charge.restoreGeneratorEnergy(tag.getInt("GeneratorEnergy"));
    }
}
