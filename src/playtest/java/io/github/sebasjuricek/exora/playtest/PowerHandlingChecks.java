package io.github.sebasjuricek.exora.playtest;

import io.github.sebasjuricek.exora.energy.BufferEntity;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("exora")
@PrefixGameTestTemplate(false)
public final class PowerHandlingChecks {
    @GameTest(template = WorkshopChecks.ROOM, batch = WorkshopChecks.BATCH)
    public static void movingAChargedBufferConservesEnergy(GameTestHelper test) {
        var player = WorkshopChecks.player(test, "Carrier");
        var level = test.getLevel();
        BlockPos here = WorkshopChecks.STATION;
        BlockPos absolute = test.absolutePos(here);
        test.setBlock(here, PowerWorkshop.ENERGY_BUFFER.get());
        var buffer = (BufferEntity) level.getBlockEntity(absolute);
        for (int i = 0; i < 37; i++) buffer.energy().receiveEnergy(13, false);
        int charge = 37 * 13;
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_PICKAXE));
        player.gameMode.handleBlockBreakAction(absolute, Action.START_DESTROY_BLOCK, Direction.NORTH, level.getMaxBuildHeight(), 1);
        player.gameMode.handleBlockBreakAction(absolute, Action.ABORT_DESTROY_BLOCK, Direction.NORTH, level.getMaxBuildHeight(), 2);
        test.assertBlockPresent(PowerWorkshop.ENERGY_BUFFER.get(), here);
        test.assertTrue(test.getEntities(EntityType.ITEM, here, 1).isEmpty(), "Partial mining dropped a buffer");
        test.assertTrue(buffer.energy().getEnergyStored() == charge, "Partial mining changed charge");
        test.assertTrue(player.gameMode.destroyBlock(absolute), "Pickaxe failed to harvest buffer");
        var drops = test.getEntities(EntityType.ITEM, here, 1);
        test.assertTrue(drops.size() == 1, "Buffer should leave exactly one item");
        var item = drops.getFirst().getItem();
        test.assertTrue(item.is(PowerWorkshop.ENERGY_BUFFER.get().asItem()) && item.getCount() == 1, "Wrong buffer drop");
        test.assertTrue(item.getOrDefault(PowerWorkshop.STORED_ENERGY.get(), 0) == charge, "Item lost charge");
        test.assertFalse(item.has(DataComponents.BLOCK_ENTITY_DATA), "Buffer carried a second block entity payload");
        player.setItemInHand(InteractionHand.MAIN_HAND, item);
        drops.getFirst().discard();
        BlockPos relocated = test.absolutePos(here.east());
        test.setBlock(here.east().below(), Blocks.STONE);
        var hit = new BlockHitResult(Vec3.atBottomCenterOf(relocated), Direction.UP, relocated.below(), false);
        player.gameMode.useItemOn(player, level, item, InteractionHand.MAIN_HAND, hit);
        var placed = (BufferEntity) level.getBlockEntity(relocated);
        test.assertTrue(placed != null && placed.energy().getEnergyStored() == charge, "Placed buffer lost charge");
        test.assertTrue(player.getMainHandItem().isEmpty(), "Placement retained charged item");
        test.assertTrue(level.getBlockEntity(absolute) == null, "Old buffer remained after moving");
        test.succeed();
    }

    @GameTest(template = WorkshopChecks.ROOM, batch = WorkshopChecks.BATCH, timeoutTicks = 160)
    public static void hoppersSupplyAndCollectOneBatch(GameTestHelper test) {
        var level = test.getLevel();
        BlockPos mill = WorkshopChecks.STATION.above();
        BlockPos generator = mill.east();
        test.setBlock(mill, PowerWorkshop.COIL_MILL.get());
        test.setBlock(generator, PowerWorkshop.STARTER_GENERATOR.get());
        test.setBlock(mill.above(), Blocks.HOPPER);
        test.setBlock(generator.above(), Blocks.HOPPER);
        test.setBlock(mill.below(), Blocks.HOPPER);
        var stock = (HopperBlockEntity) level.getBlockEntity(test.absolutePos(mill.above()));
        var fuel = (HopperBlockEntity) level.getBlockEntity(test.absolutePos(generator.above()));
        var tray = (HopperBlockEntity) level.getBlockEntity(test.absolutePos(mill.below()));
        stock.setItem(0, new ItemStack(Items.IRON_INGOT));
        fuel.setItem(0, new ItemStack(Items.CHARCOAL));
        test.succeedWhen(() -> {
            test.assertTrue(tray.countItem(WorkshopStock.IRON_PLATE.get()) == 3, "Hopper did not collect the plate batch");
            test.assertTrue(stock.isEmpty() && fuel.isEmpty(), "Feed or fuel duplicated");
        });
    }
}
