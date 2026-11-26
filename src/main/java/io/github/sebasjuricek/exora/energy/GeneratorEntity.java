package io.github.sebasjuricek.exora.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public final class GeneratorEntity extends PowerSourceEntity {
    public static final int BURN_TICKS = 320;
    public static final int GENERATION = 8;
    private int burnRemaining;

    public GeneratorEntity(BlockPos pos, BlockState state) {
        super(PowerWorkshop.GENERATOR_ENTITY.get(), pos, state, StationKind.GENERATOR);
    }

    @Override
    public void serverTick(ServerLevel level) {
        supplyNeighbors(level);
        boolean headroom = charge.getEnergyStored() <= kind.capacity - GENERATION;
        if (headroom && burnRemaining == 0 && getItem(0).is(Items.CHARCOAL)) {
            removeItem(0, 1);
            burnRemaining = BURN_TICKS;
        }
        boolean burning = headroom && burnRemaining > 0;
        if (burning) {
            burnRemaining--;
            charge.adjust(GENERATION);
        }
        setLamp(level, burning);
    }

    @Override
    protected int displayValue(int index) {
        return index == StationMenu.PROGRESS ? burnRemaining : super.displayValue(index);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("BurnRemaining", burnRemaining);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        burnRemaining = Mth.clamp(tag.getInt("BurnRemaining"), 0, BURN_TICKS);
    }
}
