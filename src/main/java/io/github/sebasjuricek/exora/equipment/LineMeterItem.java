package io.github.sebasjuricek.exora.equipment;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.List;

/** A reusable meter. Two readings of the same port give the signed change in stored FE. */
public final class LineMeterItem extends Item {
    public LineMeterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var level = context.getLevel();
        var player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        var pos = context.getClickedPos();
        var face = context.getClickedFace();
        var storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, face);
        if (storage == null) return InteractionResult.PASS;
        if (!level.isClientSide) {
            String dimension = level.dimension().location().toString();
            long now = level.getGameTime();
            int energy = storage.getEnergyStored();
            CompoundTag last = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                    .copyTag().getCompound("ExoraReading");
            var message = Component.translatable("exora.meter.read", level.getBlockState(pos).getBlock().getName(),
                    energy, storage.getMaxEnergyStored());
            if (dimension.equals(last.getString("Dimension")) && last.getLong("Position") == pos.asLong()
                    && face.getName().equals(last.getString("Face")) && now > last.getLong("Time")) {
                int change = energy - last.getInt("Energy");
                message.append(Component.translatable("exora.meter.change", change > 0 ? "+" + change : change));
            }
            player.displayClientMessage(message, true);
            CompoundTag reading = new CompoundTag();
            reading.putString("Dimension", dimension);
            reading.putLong("Position", pos.asLong());
            reading.putString("Face", face.getName());
            reading.putLong("Time", now);
            reading.putInt("Energy", energy);
            CustomData.update(DataComponents.CUSTOM_DATA, stack, data -> data.put("ExoraReading", reading));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("exora.meter.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
