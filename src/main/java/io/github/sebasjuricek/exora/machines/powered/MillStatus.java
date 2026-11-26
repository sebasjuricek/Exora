package io.github.sebasjuricek.exora.machines.powered;

import net.minecraft.util.StringRepresentable;

public enum MillStatus implements StringRepresentable {
    IDLE("idle"), RUNNING("running"), NO_POWER("no_power"), BLOCKED("blocked"), UNRECOGNIZED("unrecognized");

    private final String name;

    MillStatus(String name) { this.name = name; }

    @Override
    public String getSerializedName() { return name; }
}
