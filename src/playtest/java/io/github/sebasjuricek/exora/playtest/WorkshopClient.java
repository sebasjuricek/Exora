package io.github.sebasjuricek.exora.playtest;

import com.mojang.logging.LogUtils;
import io.github.sebasjuricek.exora.machines.bench.WorkshopMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

/** Drives ordinary client interactions in the two opt-in, local development clients. */
@EventBusSubscriber(modid = "exora", value = Dist.CLIENT)
public final class WorkshopClient {
    private static final Deque<String> actions = new ArrayDeque<>();
    private static int cooldown;
    private static BlockPos mining;
    private static int mineTicks;
    private static int waited;

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null || mc.level == null || mc.getCurrentServer() == null
                || !mc.getCurrentServer().ip.equals("127.0.0.1:25641")) return;
        try {
            if (Files.deleteIfExists(mc.gameDirectory.toPath().resolve("workshop.cancel"))) {
                actions.clear();
                mining = null;
                waited = 0;
                cooldown = 0;
            }
            if (mining != null) {
                if (mc.level.getBlockState(mining).isAir()) {
                    mining = null;
                    cooldown = 14;
                } else {
                    if (++mineTicks > 400) throw new IllegalStateException("Mining timed out at " + mining);
                    mc.gameMode.continueDestroyBlock(mining, Direction.UP);
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }
                return;
            }
            if (cooldown-- > 0) return;
            Path input = mc.gameDirectory.toPath().resolve("workshop.steps");
            if (actions.isEmpty()) {
                if (!Files.exists(input)) { cooldown = 4; return; }
                actions.addAll(Files.readAllLines(input));
                Files.delete(input);
            }
            String line = actions.poll();
            if (line == null || line.isBlank() || line.startsWith("#")) return;
            String[] args = line.trim().split("\\s+");
            AbstractContainerMenu menu = mc.player.containerMenu;
            cooldown = 4;
            switch (args[0]) {
                case "wait" -> cooldown = Integer.parseInt(args[1]);
                case "use", "mine" -> {
                    BlockPos pos = new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                    if (args[0].equals("mine")) {
                        mining = pos;
                        mineTicks = 0;
                        mc.gameMode.startDestroyBlock(pos, Direction.UP);
                    } else {
                        Direction face = args.length > 4 ? Direction.valueOf(args[4]) : Direction.UP;
                        Vec3 hit = Vec3.atCenterOf(pos).relative(face, 0.5);
                        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, new BlockHitResult(hit, face, pos, false));
                    }
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }
                case "equip" -> {
                    if (menu != mc.player.inventoryMenu) throw new IllegalStateException("Close the machine before equipping");
                    var inventory = mc.player.getInventory();
                    int i = 0;
                    while (i < 36 && !id(inventory.getItem(i)).equals(args[1])) i++;
                    if (i == 36) throw new IllegalStateException("Missing " + args[1]);
                    if (i < 9) inventory.selected = i;
                    else mc.gameMode.handleInventoryMouseClick(menu.containerId, i, inventory.selected, ClickType.SWAP, mc.player);
                    mc.gameMode.tick();
                }
                case "recipe" -> mc.gameMode.handlePlaceRecipe(menu.containerId,
                        mc.level.getRecipeManager().byKey(ResourceLocation.parse(args[1])).orElseThrow(), false);
                case "button" -> mc.gameMode.handleInventoryButtonClick(menu.containerId, Integer.parseInt(args[1]));
                case "assemble" -> {
                    WorkshopMenu ledger = (WorkshopMenu) menu;
                    var recipes = ledger.recipes();
                    int index = 0;
                    while (index < recipes.size() && !recipes.get(index).id().getPath().equals("manual/" + args[1])) index++;
                    if (index == recipes.size()) throw new IllegalArgumentException("Unknown plan " + args[1]);
                    mc.gameMode.handleInventoryButtonClick(menu.containerId, index * 2 + (args.length > 2 ? 1 : 0));
                }
                case "awaitstage" -> {
                    if (((WorkshopMenu) menu).stage() != Integer.parseInt(args[1])) {
                        if (++waited > 40) throw new IllegalStateException("Wrong ledger stage: " + line);
                        actions.addFirst(line);
                        return;
                    }
                    waited = 0;
                }
                case "click", "shift", "right" -> {
                    int slot = Integer.parseInt(args[1]);
                    mc.gameMode.handleInventoryMouseClick(menu.containerId, slot, args[0].equals("right") ? 1 : 0,
                            args[0].equals("shift") ? ClickType.QUICK_MOVE : ClickType.PICKUP, mc.player);
                }
                case "clickitem", "shiftitem" -> {
                    var slot = menu.slots.stream().filter(s -> s.container == mc.player.getInventory()
                            && id(s.getItem()).equals(args[1])).findFirst().orElseThrow();
                    mc.gameMode.handleInventoryMouseClick(menu.containerId, slot.index, 0,
                            args[0].equals("shiftitem") ? ClickType.QUICK_MOVE : ClickType.PICKUP, mc.player);
                }
                case "awaititem", "awaitslot" -> {
                    boolean ready;
                    if (args[0].equals("awaitslot")) {
                        var stack = menu.getSlot(Integer.parseInt(args[1])).getItem();
                        ready = id(stack).equals(args[2]) && stack.getCount() >= Integer.parseInt(args[3]);
                    } else {
                        int count = mc.player.getInventory().items.stream().filter(s -> id(s).equals(args[1]))
                                .mapToInt(ItemStack::getCount).sum();
                        ready = count >= Integer.parseInt(args[2]);
                    }
                    if (!ready) {
                        if (++waited > 160) throw new IllegalStateException("Timed out: " + line);
                        actions.addFirst(line);
                        return;
                    }
                    waited = 0;
                }
                case "close" -> mc.player.closeContainer();
                case "expectcount" -> {
                    int count = mc.player.getInventory().items.stream().filter(s -> id(s).equals(args[1]))
                            .mapToInt(ItemStack::getCount).sum();
                    if (count != Integer.parseInt(args[2])) throw new IllegalStateException(line + " (actual " + count + ")");
                }
                case "look" -> {
                    mc.player.setYRot(Float.parseFloat(args[1]));
                    mc.player.setXRot(Float.parseFloat(args[2]));
                }
                case "shot" -> Screenshot.grab(mc.gameDirectory, args[1] + ".png", mc.getMainRenderTarget(), ignored -> {});
                case "inspect" -> LogUtils.getLogger().info("WORKSHOP {} {} hand={} inventory={} slots={} carried={}",
                        mc.player.blockPosition(), menu.getClass().getSimpleName(), mc.player.getMainHandItem(),
                        mc.player.getInventory().items.stream().filter(s -> !s.isEmpty()).toList(),
                        menu.slots.stream().filter(s -> s.hasItem()).map(s -> s.index + "=" + s.getItem()).toList(), menu.getCarried());
                case "quit" -> mc.stop();
                case "mark" -> LogUtils.getLogger().info("WORKSHOP PASS {}", args[1]);
                default -> throw new IllegalArgumentException(line);
            }
        } catch (Exception failure) {
            actions.clear();
            mining = null;
            waited = 0;
            LogUtils.getLogger().error("WORKSHOP CHECK FAILED", failure);
        }
    }

    private static String id(ItemStack stack) {
        return stack.isEmpty() ? "empty" : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }
}
