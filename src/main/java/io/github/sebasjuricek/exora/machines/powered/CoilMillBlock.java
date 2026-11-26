package io.github.sebasjuricek.exora.machines.powered;

import com.mojang.serialization.MapCodec;
import io.github.sebasjuricek.exora.energy.PowerStationBlock;
import io.github.sebasjuricek.exora.energy.PowerStationEntity;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class CoilMillBlock extends PowerStationBlock {
    public static final EnumProperty<MillStatus> STATUS = EnumProperty.create("status", MillStatus.class);
    public static final MapCodec<CoilMillBlock> CODEC = simpleCodec(CoilMillBlock::new);

    public CoilMillBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(STATUS, MillStatus.IDLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATUS);
    }

    @Override
    protected MapCodec<CoilMillBlock> codec() { return CODEC; }

    @Override
    protected BlockEntityType<? extends PowerStationEntity> stationType() {
        return PowerWorkshop.MILL_ENTITY.get();
    }
}
