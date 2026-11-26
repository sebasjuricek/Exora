package io.github.sebasjuricek.exora.energy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public final class PowerIndicatorBlock extends PowerStationBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<PowerIndicatorBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("buffer").forGetter(b -> b.kind == StationKind.BUFFER), propertiesCodec()
    ).apply(instance, (buffer, properties) -> new PowerIndicatorBlock(buffer ? StationKind.BUFFER : StationKind.GENERATOR, properties)));
    private final StationKind kind;

    PowerIndicatorBlock(StationKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected MapCodec<PowerIndicatorBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockEntityType<? extends PowerStationEntity> stationType() {
        return kind == StationKind.BUFFER ? PowerWorkshop.BUFFER_ENTITY.get() : PowerWorkshop.GENERATOR_ENTITY.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (kind == StationKind.GENERATOR && state.getValue(LIT) && random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.05, pos.getZ() + 0.5, 0, 0.035, 0);
        }
    }
}
