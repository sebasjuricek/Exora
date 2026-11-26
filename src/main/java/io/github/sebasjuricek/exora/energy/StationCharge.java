package io.github.sebasjuricek.exora.energy;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;

/** External transfers obey the device's ports. Its own generator or motor uses the internal balance. */
public final class StationCharge extends EnergyStorage {
    private final Runnable changed;
    private int generatorEnergy;

    StationCharge(StationKind kind, Runnable changed) {
        super(kind.capacity, kind == StationKind.GENERATOR ? 0 : 16, kind == StationKind.MILL ? 0 : 16);
        this.changed = changed;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (amount <= 0) return 0;
        int accepted = super.receiveEnergy(amount, simulate);
        if (!simulate && accepted > 0) changed.run();
        return accepted;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        if (amount <= 0) return 0;
        int removed = super.extractEnergy(amount, simulate);
        if (!simulate && removed > 0) changed.run();
        return removed;
    }

    void restore(int amount) {
        energy = Mth.clamp(amount, 0, capacity);
    }

    void recordGeneratorEnergy(int amount) {
        generatorEnergy = Mth.clamp(generatorEnergy + amount, 0, energy);
    }

    int generatorEnergy() { return generatorEnergy; }

    void restoreGeneratorEnergy(int amount) { generatorEnergy = Mth.clamp(amount, 0, energy); }

    /** A receipt follows actual FE consumed by the motor, including pulses held across a save. */
    public boolean consumeMotorEnergy(int amount) {
        int fromGenerator = Math.min(amount, generatorEnergy);
        generatorEnergy -= fromGenerator;
        adjust(-amount);
        return fromGenerator > 0;
    }

    public void adjust(int delta) {
        restore(energy + delta);
        changed.run();
    }
}
