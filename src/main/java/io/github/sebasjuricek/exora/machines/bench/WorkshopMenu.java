package io.github.sebasjuricek.exora.machines.bench;

import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.machines.ManualWorkshop;
import io.github.sebasjuricek.exora.materials.WorkshopStock;
import io.github.sebasjuricek.exora.progression.WorkshopProgress;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public final class WorkshopMenu extends AbstractContainerMenu {
    public static final ResourceLocation INSTALL = ResourceLocation.fromNamespaceAndPath(Exora.MOD_ID, "manual/install_drive");
    private final Inventory inventory;
    private final BlockPos pos;
    private final boolean press;
    private final List<RecipeHolder<ManualRecipe>> recipes;
    private final DataSlot stage = DataSlot.standalone();
    private final RecipeInput stock;

    public WorkshopMenu(int id, Inventory inventory, BlockPos pos, boolean press) {
        super(ManualWorkshop.MENU.get(), id);
        this.inventory = inventory;
        this.pos = pos.immutable();
        this.press = press;
        this.stock = new RecipeInput() {
            @Override public int size() { return inventory.items.size(); }
            @Override public ItemStack getItem(int index) { return inventory.items.get(index); }
        };
        recipes = inventory.player.level().getRecipeManager().getAllRecipesFor(ManualWorkshop.MANUAL.get()).stream()
                .filter(holder -> holder.value().press() == press)
                .sorted(Comparator.<RecipeHolder<ManualRecipe>>comparingInt(holder -> holder.value().order())
                        .thenComparing(RecipeHolder::id)).toList();
        addDataSlot(stage);
        // Bind the pack to vanilla slot synchronization while the ledger displays ingredient totals.
        // The menu takes only button intents; players rearrange their pack in the regular inventory.
        for (int i = 0; i < inventory.items.size(); i++) addStockSlot(i);
        addStockSlot(Inventory.SLOT_OFFHAND);
        refreshStage();
    }

    private void addStockSlot(int index) {
        addSlot(new Slot(inventory, index, -1000, -1000) {
            @Override public boolean isActive() { return false; }
        });
    }

    @Override
    public void clicked(int index, int button, ClickType type, Player player) {}

    @Override
    public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }

    @Override
    public void broadcastChanges() {
        refreshStage();
        super.broadcastChanges();
    }

    private void refreshStage() {
        if (inventory.player instanceof ServerPlayer player) stage.set(WorkshopProgress.stage(player));
    }

    @Override
    public boolean stillValid(Player player) {
        Block expected = press ? ManualWorkshop.HAND_PRESS.get() : ManualWorkshop.WORKSHOP_BENCH.get();
        return inventory.player == player && player.level().getBlockState(pos).is(expected)
                && player.canInteractWithBlock(pos, 4);
    }

    public boolean isPress() { return press; }
    public int stage() { return stage.get(); }
    public List<RecipeHolder<ManualRecipe>> recipes() { return recipes; }

    public boolean hasMallet() {
        return inventory.items.stream().anyMatch(stack -> stack.is(WorkshopStock.MALLET.get()))
                || inventory.player.getOffhandItem().is(WorkshopStock.MALLET.get());
    }

    public int owned(SizedIngredient material) {
        return inventory.items.stream().filter(material.ingredient()::test).mapToInt(ItemStack::getCount).sum();
    }

    public int playerStock(ItemStack output) {
        return inventory.items.stream().filter(s -> ItemStack.isSameItemSameComponents(s, output))
                .mapToInt(ItemStack::getCount).sum();
    }

    public boolean driven() {
        return press && inventory.player.level().getBlockState(pos).getOptionalValue(PressBlock.DRIVEN).orElse(false);
    }

    public boolean pressed() {
        return press && inventory.player.level().getBlockState(pos).getOptionalValue(PressBlock.PRESSED).orElse(false);
    }

    public static boolean isInstall(RecipeHolder<ManualRecipe> recipe) { return recipe.id().equals(INSTALL); }

    public boolean canRun(RecipeHolder<ManualRecipe> holder, int batches) {
        ManualRecipe recipe = holder.value();
        return recipe.stage() <= stage() && (press || hasMallet())
                && (!isInstall(holder) || !driven()) && !pressed()
                && (batches == 1 || batches == 4 && driven() && !isInstall(holder))
                && recipe.consumption(stock, batches) != null;
    }

    /** Even button IDs are single operations; an odd ID requests four strokes on a geared press. */
    @Override
    public boolean clickMenuButton(Player sender, int button) {
        if (!(sender instanceof ServerPlayer player) || player.isSpectator() || !stillValid(player) || button < 0) return false;
        int index = button / 2;
        if (index >= recipes.size()) return false;
        int batches = button % 2 == 0 ? 1 : 4;
        var holder = recipes.get(index);
        var recipe = holder.value();
        // A /reload may have replaced the recipe after the ledger opened.
        if (player.level().getRecipeManager().byKey(holder.id()).orElse(null) != holder) {
            player.closeContainer();
            player.displayClientMessage(Component.translatable("exora.workshop.reopen"), true);
            return false;
        }
        refreshStage();
        if (!canRun(holder, batches)) return false;
        int[] used = recipe.consumption(stock, batches);
        for (int slot = 0; slot < used.length; slot++) {
            if (used[slot] > 0) inventory.removeItem(slot, used[slot]);
        }
        var level = player.serverLevel();
        BlockState state = level.getBlockState(pos);
        if (isInstall(holder)) {
            level.setBlock(pos, state.setValue(PressBlock.DRIVEN, true), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.85f, 0.78f);
            WorkshopProgress.prove(player, 2);
        } else {
            for (int i = 0; i < batches; i++) inventory.placeItemBackInInventory(recipe.result().copy());
            if (press) {
                level.setBlock(pos, state.setValue(PressBlock.PRESSED, true), Block.UPDATE_ALL);
                level.scheduleTick(pos, state.getBlock(), driven() ? 7 : 18);
                level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.38f, driven() ? 1.25f : 0.95f);
            } else {
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.7f, 0.78f);
                if (recipe.result().is(WorkshopStock.TIMBER_FRAME.get())) WorkshopProgress.prove(player, 1);
            }
        }
        inventory.setChanged();
        broadcastChanges();
        return true;
    }
}
