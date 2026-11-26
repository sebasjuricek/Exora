package io.github.sebasjuricek.exora.energy;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

abstract class PowerSourceEntity extends PowerStationEntity {
    private final List<BlockCapabilityCache<IEnergyStorage, Direction>> neighbors = new ArrayList<>(6);
    private int firstFace;

    PowerSourceEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, StationKind kind) {
        super(type, pos, state, kind);
    }

    protected void supplyNeighbors(ServerLevel level) {
        int available = charge.extractEnergy(16, true);
        if (available == 0) return;
        if (neighbors.isEmpty()) {
            for (Direction side : Direction.values()) {
                neighbors.add(BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, level,
                        worldPosition.relative(side), side.getOpposite(), () -> !isRemoved(), () -> {}));
            }
        }
        for (int offset = 0; available > 0 && offset < neighbors.size(); offset++) {
            IEnergyStorage receiver = neighbors.get((firstFace + offset) % neighbors.size()).getCapability();
            if (receiver == null || !receiver.canReceive()) continue;
            // Two passive workshop buffers side by side must not pump the same charge back and forth.
            if (kind == StationKind.BUFFER && receiver instanceof StationCharge && receiver.canExtract()) continue;
            int accepted = Math.min(available, receiver.receiveEnergy(available, true));
            if (accepted > 0) {
                int sent = receiver.receiveEnergy(accepted, false);
                if (kind == StationKind.GENERATOR && receiver instanceof StationCharge motor && !motor.canExtract()) {
                    motor.recordGeneratorEnergy(sent);
                }
                available -= charge.extractEnergy(sent, false);
            }
        }
        firstFace = (firstFace + 1) % neighbors.size();
    }

    protected void setLamp(ServerLevel level, boolean lit) {
        BlockState state = getBlockState();
        if (state.getValue(PowerIndicatorBlock.LIT) != lit) {
            level.setBlock(worldPosition, state.setValue(PowerIndicatorBlock.LIT, lit), 3);
        }
    }
}
