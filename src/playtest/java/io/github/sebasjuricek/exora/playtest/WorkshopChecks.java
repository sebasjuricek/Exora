package io.github.sebasjuricek.exora.playtest;

import com.mojang.authlib.GameProfile;
import io.github.sebasjuricek.exora.machines.ManualWorkshop;
import io.github.sebasjuricek.exora.machines.bench.WorkshopMenu;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import io.github.sebasjuricek.exora.progression.WorkshopProgress;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("exora")
@PrefixGameTestTemplate(false)
public final class WorkshopChecks {
    static final String ROOM = "workshop_test_space";
    static final String BATCH = "workshop";
    static final BlockPos STATION = new BlockPos(2, 1, 2);

    @GameTest(template = ROOM, batch = BATCH)
    public static void manualProofsAndLeverReturn(GameTestHelper test) {
        var fitter = player(test, "Fitter");
        var winder = player(test, "Winder");
        joinTeam(fitter, winder);
        test.setBlock(STATION, ManualWorkshop.WORKSHOP_BENCH.get());
        BlockPos pressPos = STATION.east();
        test.setBlock(pressPos, ManualWorkshop.HAND_PRESS.get());
        var bench = new WorkshopMenu(1, fitter.getInventory(), test.absolutePos(STATION), false);
        var press = new WorkshopMenu(2, fitter.getInventory(), test.absolutePos(pressPos), true);
        fitter.getInventory().add(new ItemStack(Items.SPRUCE_PLANKS, 2));
        fitter.getInventory().add(new ItemStack(Items.STICK, 2));
        fitter.getInventory().add(new ItemStack(Items.IRON_INGOT, 7));
        test.assertFalse(operate(fitter, press, "shaft", false), "Press accepted stock before frame proof");
        test.assertFalse(operate(fitter, bench, "timber_frame", false), "Frame assembled without a mallet");
        fitter.getInventory().add(WorkshopStock.MALLET.toStack());
        test.assertTrue(operate(fitter, bench, "timber_frame", false), "Frame assembly did not consume spruce planks and sticks");
        test.assertTrue(fitter.getInventory().countItem(WorkshopStock.TIMBER_FRAME.get()) == 1, "Frame missing");
        test.assertTrue(WorkshopProgress.stage(winder) == 1, "Teammate missed frame proof");
        test.assertFalse(operate(fitter, bench, "timber_frame", false), "Replayed frame request duplicated stock");
        test.assertTrue(operate(fitter, press, "iron_plate", false), "Press could not produce plate batch");
        test.assertFalse(operate(fitter, press, "shaft", false), "Second stroke while ram was down");

        test.startSequence().thenIdle(22).thenExecute(() -> {
            test.assertTrue(operate(fitter, press, "shaft", false), "Ram did not return");
            test.assertTrue(operate(fitter, bench, "drive_assembly", false), "Plates and shafts could not assemble drive");
            test.assertTrue(WorkshopProgress.stage(winder) == 1, "Loose drive prematurely opened power plans");
        }).thenIdle(22).thenExecute(() -> {
            test.assertTrue(operate(fitter, press, "install_drive", false), "Drive installation failed");
            test.assertTrue(WorkshopProgress.stage(winder) == 2, "Teammate missed installation proof");
            test.assertFalse(operate(fitter, press, "install_drive", false), "Press accepted duplicate installation");
            test.assertTrue(operate(fitter, press, "shaft", true), "Driven press did not accept four batches");
            test.assertTrue(fitter.getInventory().countItem(WorkshopStock.SHAFT.get()) == 8, "Incorrect bulk shaft yield");
            test.assertTrue(fitter.getInventory().countItem(Items.IRON_INGOT) == 0, "Bulk stroke spent the wrong iron quantity");
            test.assertTrue(fitter.getInventory().countItem(WorkshopStock.MALLET.get()) == 1, "Reusable mallet was lost");
        }).thenSucceed();
    }

    static ServerPlayer player(GameTestHelper test, String prefix) {
        UUID id = UUID.randomUUID();
        var player = new FakePlayer(test.getLevel(), new GameProfile(id, prefix + id.toString().substring(0, 5)));
        player.setGameMode(GameType.SURVIVAL);
        BlockPos pos = test.absolutePos(STATION.north());
        player.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        return player;
    }

    static String joinTeam(ServerPlayer... members) {
        var scoreboard = members[0].getScoreboard();
        String name = "test" + UUID.randomUUID().toString().substring(0, 8);
        var team = scoreboard.addPlayerTeam(name);
        for (var member : members) scoreboard.addPlayerToTeam(member.getScoreboardName(), team);
        return name;
    }

    private static boolean operate(ServerPlayer player, WorkshopMenu menu, String plan, boolean bulk) {
        for (int i = 0; i < menu.recipes().size(); i++) {
            if (menu.recipes().get(i).id().getPath().equals("manual/" + plan))
                return menu.clickMenuButton(player, 2 * i + (bulk ? 1 : 0));
        }
        throw new IllegalArgumentException("Missing plan " + plan);
    }
}
