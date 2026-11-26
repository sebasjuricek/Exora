package io.github.sebasjuricek.exora.machines.powered;

import io.github.sebasjuricek.exora.energy.PowerStationEntity;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import io.github.sebasjuricek.exora.energy.StationKind;
import io.github.sebasjuricek.exora.energy.StationMenu;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import io.github.sebasjuricek.exora.progression.WorkshopProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class CoilMillEntity extends PowerStationEntity {
    public static final int FEED = 0, TRAY = 1;
    private final RecipeManager.CachedCheck<SingleRecipeInput, MillingRecipe> lookup = RecipeManager.createCheck(PowerWorkshop.MILLING.get());
    // Exactly one committed input owns the in-flight result. Breaking the machine refunds that input.
    private ItemStack workpiece = ItemStack.EMPTY;
    private ItemStack plannedResult = ItemStack.EMPTY;
    private int elapsed, duration, rate;
    private boolean usedGenerator, canCommission;

    public CoilMillEntity(BlockPos pos, BlockState state) {
        super(PowerWorkshop.MILL_ENTITY.get(), pos, state, StationKind.MILL);
    }

    @Override
    public void serverTick(ServerLevel level) {
        MillStatus status = advance(level);
        BlockState state = getBlockState();
        if (state.getValue(CoilMillBlock.STATUS) != status) {
            level.setBlock(worldPosition, state.setValue(CoilMillBlock.STATUS, status), 3);
        }
    }

    private MillStatus advance(ServerLevel level) {
        if (!getItem(TRAY).isEmpty()) {
            return workpiece.isEmpty() && getItem(FEED).isEmpty() ? MillStatus.IDLE : MillStatus.BLOCKED;
        }
        if (workpiece.isEmpty()) {
            if (getItem(FEED).isEmpty()) return MillStatus.IDLE;
            SingleRecipeInput input = new SingleRecipeInput(getItem(FEED));
            var match = lookup.getRecipeFor(input, level);
            if (match.isEmpty()) return MillStatus.UNRECOGNIZED;
            MillingRecipe recipe = match.get().value();
            plannedResult = recipe.assemble(input, level.registryAccess());
            workpiece = removeItem(FEED, 1);
            duration = recipe.ticks();
            rate = recipe.energyPerTick();
            elapsed = 0;
            usedGenerator = false;
            canCommission = false;
        }
        if (charge.getEnergyStored() < rate) return MillStatus.NO_POWER;
        usedGenerator |= charge.consumeMotorEnergy(rate);
        elapsed++;
        if (elapsed < duration) return MillStatus.RUNNING;

        setItem(TRAY, plannedResult);
        canCommission = usedGenerator && plannedResult.is(WorkshopStock.WOUND_CORE.get());
        plannedResult = ItemStack.EMPTY;
        workpiece = ItemStack.EMPTY;
        elapsed = duration = rate = 0;
        usedGenerator = false;
        level.playSound(null, worldPosition, SoundEvents.COPPER_BULB_TURN_ON, SoundSource.BLOCKS, 0.55F, 1.18F);
        return getItem(FEED).isEmpty() ? MillStatus.IDLE : MillStatus.BLOCKED;
    }

    /** Called after an actual transfer from the take-only tray to a player's cursor or inventory. */
    public void collected(ServerPlayer player, ItemStack taken) {
        if (canCommission && taken.is(WorkshopStock.WOUND_CORE.get()) && WorkshopProgress.stage(player) == 2) {
            canCommission = false;
            setChanged();
            WorkshopProgress.prove(player, 3);
        }
    }

    @Override
    protected int displayValue(int index) {
        return switch (index) {
            case StationMenu.PROGRESS -> elapsed;
            case StationMenu.DURATION -> duration;
            case StationMenu.STATUS -> getBlockState().getValue(CoilMillBlock.STATUS).ordinal();
            case StationMenu.WORKPIECE -> net.minecraft.world.item.Item.getId(workpiece.getItem()) & 0xffff;
            case StationMenu.WORKPIECE_HIGH -> net.minecraft.world.item.Item.getId(workpiece.getItem()) >>> 16;
            default -> super.displayValue(index);
        };
    }

    @Override
    public void dropStock(Level level) {
        super.dropStock(level);
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), workpiece);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!workpiece.isEmpty()) {
            tag.put("Workpiece", workpiece.save(registries));
            tag.put("PlannedResult", plannedResult.save(registries));
            tag.putInt("Elapsed", elapsed);
            tag.putInt("Duration", duration);
            tag.putInt("Rate", rate);
            tag.putBoolean("UsedGenerator", usedGenerator);
        }
        tag.putBoolean("CanCommission", canCommission);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        workpiece = ItemStack.parseOptional(registries, tag.getCompound("Workpiece"));
        plannedResult = ItemStack.parseOptional(registries, tag.getCompound("PlannedResult"));
        duration = Mth.clamp(tag.getInt("Duration"), 1, MillingRecipe.MAX_TICKS);
        rate = Mth.clamp(tag.getInt("Rate"), 1, kind.capacity);
        elapsed = Mth.clamp(tag.getInt("Elapsed"), 0, duration - 1);
        usedGenerator = tag.getBoolean("UsedGenerator");
        canCommission = tag.getBoolean("CanCommission") && getItem(TRAY).is(WorkshopStock.WOUND_CORE.get());
    }
}
