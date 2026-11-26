package io.github.sebasjuricek.exora.playtest;

import io.github.sebasjuricek.exora.energy.PowerStationEntity;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import io.github.sebasjuricek.exora.energy.StationMenu;
import io.github.sebasjuricek.exora.machines.powered.CoilMillBlock;
import io.github.sebasjuricek.exora.machines.powered.MillStatus;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import io.github.sebasjuricek.exora.progression.WorkshopProgress;
import java.io.IOException;
import java.io.UncheckedIOException;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.IOUtilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import static io.github.sebasjuricek.exora.playtest.WorkshopChecks.BATCH;
import static io.github.sebasjuricek.exora.playtest.WorkshopChecks.ROOM;
import static io.github.sebasjuricek.exora.playtest.WorkshopChecks.STATION;

@GameTestHolder("exora")
@PrefixGameTestTemplate(false)
public final class WorkshopPowerChecks {
    private static final BlockPos SOURCE = STATION.west();

    @GameTest(template = ROOM, batch = BATCH, timeoutTicks = 260)
    public static void commissioningRequiresGeneratorWorkAndPickup(GameTestHelper test) {
        var operator = WorkshopChecks.player(test, "Proof");
        startAtPower(operator);
        test.setBlock(STATION, PowerWorkshop.COIL_MILL.get());
        test.setBlock(SOURCE, PowerWorkshop.ENERGY_BUFFER.get());
        var supply = energy(test, SOURCE);
        for (int pulse = 0; pulse < 24; pulse++) supply.receiveEnergy(16, false);
        var mill = menu(test, STATION, operator);
        insert(test, mill, operator, WorkshopStock.COPPER_WINDING.toStack());
        test.startSequence()
            .thenWaitUntil(() -> test.assertTrue(mill.getSlot(1).hasItem(), "Buffer-fed core batch incomplete"))
            .thenExecute(() -> {
                mill.quickMoveStack(operator, 1);
                test.assertTrue(operator.getInventory().countItem(WorkshopStock.WOUND_CORE.get()) == 2, "Independent FE source could not run a job");
                test.assertTrue(WorkshopProgress.stage(operator) == 2, "Energy supplied exclusively by a buffer bypassed commissioning");
                test.setBlock(SOURCE, PowerWorkshop.STARTER_GENERATOR.get());
                insert(test, menu(test, SOURCE, operator), operator, new ItemStack(Items.CHARCOAL));
                insert(test, mill, operator, WorkshopStock.COPPER_WINDING.toStack());
            })
            .thenWaitUntil(() -> test.assertTrue(mill.getSlot(1).hasItem(), "Generator-fed core batch incomplete"))
            .thenExecute(() -> {
                mill.clicked(1, 0, ClickType.THROW, operator); // Q drops one item directly onto the floor.
                test.assertTrue(mill.getSlot(1).getItem().getCount() == 1, "Dropping a core did not remove it from the tray");
                test.assertTrue(WorkshopProgress.stage(operator) == 2, "Discarding a core counted as collecting it");
                mill.quickMoveStack(operator, 1);
                test.assertTrue(WorkshopProgress.stage(operator) == 3, "Collecting the remaining qualified core lost its receipt");
            }).thenSucceed();
    }

    @GameTest(template = ROOM, batch = BATCH, timeoutTicks = 330)
    public static void pausedJobAndCompetingCollectors(GameTestHelper test) {
        var collector = WorkshopChecks.player(test, "Tray");
        var partner = WorkshopChecks.player(test, "Bench");
        var other = WorkshopChecks.player(test, "Visitor");
        String team = WorkshopChecks.joinTeam(collector, partner);
        startAtPower(collector);
        startAtPower(other);
        test.setBlock(STATION, PowerWorkshop.COIL_MILL.get());
        test.setBlock(SOURCE, PowerWorkshop.STARTER_GENERATOR.get());
        var generator = menu(test, SOURCE, collector);
        test.assertFalse(generator.getSlot(0).mayPlace(new ItemStack(Items.COAL)), "Generator fuel filter accepted coal");
        var console = menu(test, STATION, collector);
        test.assertFalse(console.getSlot(1).mayPlace(WorkshopStock.WOUND_CORE.toStack()), "Operator can insert a false commissioning result");
        insert(test, console, collector, WorkshopStock.COPPER_WINDING.toStack());

        test.startSequence()
            .thenWaitUntil(() -> status(test, MillStatus.NO_POWER))
            .thenIdle(10)
            .thenExecute(() -> {
                test.assertTrue(console.value(StationMenu.PROGRESS) == 0, "Unpowered job advanced");
                test.assertTrue(console.getSlot(0).getItem().isEmpty(), "Active workpiece still available to extract");
                test.assertTrue(console.getSlot(1).getItem().isEmpty(), "Unpowered mill created output");
                insert(test, generator, collector, new ItemStack(Items.CHARCOAL));
                insert(test, console, collector, new ItemStack(Items.IRON_INGOT));
            })
            .thenWaitUntil(() -> test.assertTrue(console.value(StationMenu.PROGRESS) >= 12, "Motor has yet to start"))
            .thenExecute(() -> {
                int done = console.value(StationMenu.PROGRESS);
                int burn = generator.value(StationMenu.PROGRESS);
                reloadEntity(test, SOURCE);
                reloadEntity(test, STATION);
                test.assertTrue(menu(test, STATION, collector).value(StationMenu.PROGRESS) == done, "Lost active machining progress");
                test.assertTrue(menu(test, SOURCE, collector).value(StationMenu.PROGRESS) == burn, "Lost remaining charcoal burn");
                test.assertTrue(WorkshopProgress.stage(collector) == 2, "Starting a motor prematurely granted Electrical");
            })
            .thenWaitUntil(() -> status(test, MillStatus.BLOCKED))
            .thenIdle(35)
            .thenExecute(() -> {
                // Reload again with an unclaimed finished batch in the tray.
                reloadEntity(test, STATION);
                var first = menu(test, STATION, collector);
                var second = menu(test, STATION, other);
                test.assertTrue(first.getSlot(0).getItem().is(Items.IRON_INGOT), "Blocked mill consumed its next feed");
                test.assertTrue(first.getSlot(1).getItem().getCount() == 2, "Blocked mill changed batch size");
                test.assertTrue(WorkshopProgress.stage(partner) == 2, "An uncollected batch granted proof");
                first.clicked(1, 1, ClickType.PICKUP, collector); // Right click takes half the stack.
                test.assertTrue(first.getCarried().is(WorkshopStock.WOUND_CORE.get()) && first.getCarried().getCount() == 1,
                        "Partial pickup did not transfer exactly one core");
                test.assertTrue(WorkshopProgress.stage(partner) == 3, "Team did not receive collection proof");
                second.quickMoveStack(other, 1);
                test.assertTrue(other.getInventory().countItem(WorkshopStock.WOUND_CORE.get()) == 1, "Second collector duplicated or lost a core");
                test.assertTrue(WorkshopProgress.stage(other) == 2, "Remainder of one batch credited an unrelated operator twice");
                test.assertTrue(second.quickMoveStack(other, 1).isEmpty(), "A repeated take returned more output");
                checkLedgerOnDisk(test, team);
            })
            .thenWaitUntil(() -> test.assertTrue(menu(test, STATION, other).getSlot(1).getItem().is(WorkshopStock.IRON_PLATE.get()),
                    "Clearing the tray did not resume queued iron"))
            .thenExecute(() -> {
                var finish = menu(test, STATION, other);
                finish.quickMoveStack(other, 1);
                test.assertTrue(other.getInventory().countItem(WorkshopStock.IRON_PLATE.get()) == 3, "Expected exactly one rolled-plate batch");
                test.assertTrue(WorkshopProgress.stage(other) == 2, "Stock machining incorrectly granted commissioning proof");
            })
            .thenSucceed();
    }

    @GameTest(template = ROOM, batch = BATCH, timeoutTicks = 240)
    public static void chargedBufferRunsAlone(GameTestHelper test) {
        var operator = WorkshopChecks.player(test, "Buffer");
        test.setBlock(SOURCE, PowerWorkshop.STARTER_GENERATOR.get());
        test.setBlock(STATION, PowerWorkshop.ENERGY_BUFFER.get());
        insert(test, menu(test, SOURCE, operator), operator, new ItemStack(Items.CHARCOAL));
        int[] start = new int[1];
        BlockPos mill = STATION.east();
        test.startSequence()
            .thenWaitUntil(() -> test.assertTrue(energy(test, STATION).getEnergyStored() >= 512, "Generator has not charged buffer"))
            .thenExecute(() -> {
                test.destroyBlock(SOURCE);
                start[0] = energy(test, STATION).getEnergyStored();
                reloadEntity(test, STATION);
                var port = energy(test, STATION);
                test.assertTrue(port.getEnergyStored() == start[0], "Buffer lost charge on reload");
                test.assertTrue(port.extractEnergy(16, true) == 16 && port.getEnergyStored() == start[0], "Simulated power transfer changed stored FE");
                test.setBlock(mill, PowerWorkshop.COIL_MILL.get());
                insert(test, menu(test, mill, operator), operator, new ItemStack(Items.COPPER_INGOT));
            })
            .thenWaitUntil(() -> test.assertTrue(menu(test, mill, operator).getSlot(1).hasItem(), "Isolated mill has not finished"))
            .thenExecute(() -> {
                var finish = menu(test, mill, operator);
                finish.quickMoveStack(operator, 1);
                test.assertTrue(operator.getInventory().countItem(WorkshopStock.COPPER_WINDING.get()) == 4, "Incorrect stored-power yield");
                int remaining = energy(test, STATION).getEnergyStored() + energy(test, mill).getEnergyStored();
                test.assertTrue(start[0] - remaining == 56 * 4, "Buffer-to-mill transfer lost or created FE");
            }).thenSucceed();
    }

    @GameTest(template = ROOM, batch = BATCH, timeoutTicks = 170)
    public static void powerLossAndBreakRefundOneInput(GameTestHelper test) {
        var operator = WorkshopChecks.player(test, "Break");
        test.setBlock(STATION, PowerWorkshop.COIL_MILL.get());
        test.setBlock(SOURCE, PowerWorkshop.STARTER_GENERATOR.get());
        var mill = menu(test, STATION, operator);
        insert(test, mill, operator, WorkshopStock.COPPER_WINDING.toStack());
        insert(test, menu(test, SOURCE, operator), operator, new ItemStack(Items.CHARCOAL));
        int[] paused = new int[1];
        test.startSequence().thenWaitUntil(() -> test.assertTrue(mill.value(StationMenu.PROGRESS) >= 10, "Motor has yet to start"))
            .thenExecute(() -> test.destroyBlock(SOURCE))
            .thenWaitUntil(() -> status(test, MillStatus.NO_POWER))
            .thenExecute(() -> paused[0] = mill.value(StationMenu.PROGRESS))
            .thenIdle(20)
            .thenExecute(() -> {
                test.assertTrue(paused[0] > 0 && mill.value(StationMenu.PROGRESS) == paused[0], "Outage rewound or advanced active work");
                reloadEntity(test, STATION);
                test.destroyBlock(STATION);
                int inputs = 0;
                for (var entity : test.getLevel().getEntities(EntityType.ITEM, test.getBounds(), item -> item.isAlive())) {
                    ItemStack drop = entity.getItem();
                    test.assertFalse(drop.is(WorkshopStock.WOUND_CORE.get()), "Breaking an incomplete job dropped its future output");
                    if (drop.is(WorkshopStock.COPPER_WINDING.get())) inputs += drop.getCount();
                }
                test.assertTrue(inputs == 1, "Breaking an interrupted mill must refund one committed winding");
            }).thenSucceed();
    }

    private static void startAtPower(ServerPlayer player) {
        // Manual proof production is exercised in manualProofsAndLeverReturn.
        WorkshopProgress.prove(player, 1);
        WorkshopProgress.prove(player, 2);
    }

    private static StationMenu menu(GameTestHelper test, BlockPos pos, ServerPlayer player) {
        PowerStationEntity station = test.getBlockEntity(pos);
        return station.createMenu(1, player.getInventory());
    }

    private static void insert(GameTestHelper test, StationMenu menu, ServerPlayer player, ItemStack stack) {
        Item item = stack.getItem();
        test.assertTrue(player.getInventory().add(stack), "Fixture inventory full");
        var from = menu.slots.stream().filter(s -> s.container == player.getInventory() && s.getItem().is(item))
                .findFirst().orElseThrow();
        menu.quickMoveStack(player, from.index);
        test.assertTrue(menu.getSlot(0).getItem().is(item), "Shift-click failed to insert " + item);
    }

    private static IEnergyStorage energy(GameTestHelper test, BlockPos pos) {
        var port = test.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, test.absolutePos(pos), Direction.UP);
        test.assertTrue(port != null, "Missing face energy capability at " + pos);
        return port;
    }

    private static void status(GameTestHelper test, MillStatus expected) {
        test.assertTrue(test.getBlockState(STATION).getValue(CoilMillBlock.STATUS) == expected, "Expected mill state " + expected);
    }

    private static void reloadEntity(GameTestHelper test, BlockPos relative) {
        var level = test.getLevel();
        var pos = test.absolutePos(relative);
        var state = level.getBlockState(pos);
        var tag = test.getBlockEntity(relative).saveWithFullMetadata(level.registryAccess());
        level.removeBlockEntity(pos);
        var reloaded = BlockEntity.loadStatic(pos, state, tag, level.registryAccess());
        test.assertTrue(reloaded != null, "Failed to reload station at " + pos);
        level.setBlockEntity(reloaded);
    }

    private static void checkLedgerOnDisk(GameTestHelper test, String team) {
        var data = test.getLevel().getServer().overworld().getDataStorage();
        data.save();
        IOUtilities.waitUntilIOWorkerComplete();
        try {
            var saved = data.readTagFromDisk("exora_workshop", null, SharedConstants.getCurrentVersion().getDataVersion().getVersion());
            test.assertTrue(saved.getCompound("data").getCompound("Ledgers").getInt("team/" + team) == 3,
                    "Electrical proof missing from world saved data on disk");
        } catch (IOException error) {
            throw new UncheckedIOException(error);
        }
    }
}
