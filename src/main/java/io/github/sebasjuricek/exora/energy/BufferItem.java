package io.github.sebasjuricek.exora.energy;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

/** The charge gauge follows a portable buffer from the machine floor into the inventory. */
final class BufferItem extends BlockItem {
    BufferItem(Block block, Properties properties) {
        super(block, properties);
    }

    private static int charge(ItemStack stack) {
        return stack.getOrDefault(PowerWorkshop.STORED_ENERGY.get(), 0);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return charge(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.max(1, Math.round(13F * charge(stack) / StationKind.BUFFER.capacity));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x94D7B4;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("exora.station.charge", charge(stack), StationKind.BUFFER.capacity));
    }
}
