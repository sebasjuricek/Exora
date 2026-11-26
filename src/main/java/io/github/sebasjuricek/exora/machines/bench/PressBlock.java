package io.github.sebasjuricek.exora.machines.bench;

import com.mojang.serialization.MapCodec;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;

public final class PressBlock extends BenchBlock {
    private static final MapCodec<PressBlock> CODEC = simpleCodec(PressBlock::new);
    public static final BooleanProperty DRIVEN = BooleanProperty.create("driven");
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");

    public PressBlock(Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK));
        registerDefaultState(defaultBlockState().setValue(DRIVEN, false).setValue(PRESSED, false));
    }

    @Override
    protected MapCodec<PressBlock> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DRIVEN, PRESSED);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRESSED)) {
            level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.55f, 1.1f);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState next, boolean moving) {
        if (!level.isClientSide && !next.is(this) && state.getValue(DRIVEN)) {
            popResource(level, pos, new ItemStack(WorkshopStock.DRIVE_ASSEMBLY.get()));
        }
        super.onRemove(state, level, pos, next, moving);
    }
}
