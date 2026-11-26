package io.github.sebasjuricek.exora.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public final class BufferEntity extends PowerSourceEntity {
    public BufferEntity(BlockPos pos, BlockState state) {
        super(PowerWorkshop.BUFFER_ENTITY.get(), pos, state, StationKind.BUFFER);
    }

    @Override
    public void serverTick(ServerLevel level) {
        supplyNeighbors(level);
        setLamp(level, charge.getEnergyStored() > 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (charge.getEnergyStored() > 0) {
            components.set(PowerWorkshop.STORED_ENERGY.get(), charge.getEnergyStored());
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput components) {
        super.applyImplicitComponents(components);
        charge.restore(components.getOrDefault(PowerWorkshop.STORED_ENERGY.get(), 0));
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove("Energy");
    }
}
