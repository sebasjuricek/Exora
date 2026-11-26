package io.github.sebasjuricek.exora.client.screen;

import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.machines.ManualWorkshop;
import io.github.sebasjuricek.exora.machines.bench.ManualRecipe;
import io.github.sebasjuricek.exora.machines.bench.WorkshopMenu;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.locale.Language;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Exora.MOD_ID, value = Dist.CLIENT)
public final class WorkshopScreen extends AbstractContainerScreen<WorkshopMenu> {
    private static final int PAPER = 0xFFF0E6CD, MUTED = 0xFFACC0B5, COPPER = 0xFFD99765, MINT = 0xFF94D7B4;
    private static final int PANEL = 0xFF223638, EDGE = 0xFF425554, BACK = 0xFF152426;
    private static final int ROWS = 4;
    private final List<Button> plans = new ArrayList<>();
    private final List<Button> chapters = new ArrayList<>();
    private Button previous, next, work, batch;
    private int selected, first, chapter, lastStage = -1;

    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent event) {
        event.register(ManualWorkshop.MENU.get(), WorkshopScreen::new);
    }

    public WorkshopScreen(WorkshopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 300;
        imageHeight = 220;
    }

    private static Component tr(String key, Object... args) {
        return Component.translatable("exora.workshop." + key, args);
    }

    @Override
    protected void init() {
        super.init();
        plans.clear();
        chapters.clear();
        for (int i = 0; i < 4; i++) {
            final int page = i;
            chapters.add(button(8 + i * 71, 22, 70, 17, tr("chapter." + i), b -> { chapter = page; updateControls(); }));
        }
        for (int i = 0; i < ROWS; i++) {
            final int row = i;
            plans.add(button(8, 101 + i * 21, 122, 20, Component.empty(), b -> { selected = first + row; updateControls(); }));
        }
        previous = button(8, 191, 28, 19, Component.literal("<"), b -> turn(-1));
        next = button(102, 191, 28, 19, Component.literal(">"), b -> turn(1));
        work = button(140, 191, 148, 20, tr("assemble"), b -> run(1));
        batch = button(245, 191, 43, 20, Component.literal("×4"), b -> run(4));
        batch.setTooltip(Tooltip.create(tr("batch_hint")));
        updateControls();
    }

    private Button button(int x, int y, int w, int h, Component label, Button.OnPress callback) {
        return addRenderableWidget(Button.builder(label, callback).bounds(leftPos + x, topPos + y, w, h).build());
    }

    private void run(int batches) {
        if (minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, selected * 2 + (batches == 4 ? 1 : 0));
        }
    }

    private void turn(int direction) {
        int lastPage = Math.max(0, (menu.recipes().size() - 1) / ROWS);
        first = Math.clamp(first / ROWS + direction, 0, lastPage) * ROWS;
        selected = first;
        updateControls();
    }

    @Override
    public boolean mouseScrolled(double x, double y, double dx, double dy) {
        if (x >= leftPos + 8 && x <= leftPos + 130 && y >= topPos + 100 && y < topPos + 211 && dy != 0) {
            turn(dy > 0 ? -1 : 1);
            return true;
        }
        return super.mouseScrolled(x, y, dx, dy);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateControls();
    }

    private Component planName(RecipeHolder<ManualRecipe> holder) {
        return WorkshopMenu.isInstall(holder) ? tr("fit_drive") : holder.value().result().getHoverName();
    }

    private void updateControls() {
        int stage = menu.stage();
        if (stage != lastStage) {
            chapter = stage;
            lastStage = stage;
            String nextPlan = menu.isPress() ? "iron_plate" : switch (stage) {
                case 1 -> "hand_press";
                case 2 -> "starter_generator";
                case 3 -> "energy_buffer";
                default -> "timber_frame";
            };
            for (int index = 0; index < menu.recipes().size(); index++) {
                if (menu.recipes().get(index).id().getPath().equals("manual/" + nextPlan)) {
                    selected = index;
                    first = index / ROWS * ROWS;
                    break;
                }
            }
        }
        for (int i = 0; i < chapters.size(); i++) {
            String marker = i < stage ? "✓ " : i == stage ? "• " : "";
            chapters.get(i).setMessage(Component.literal(marker).append(tr("chapter." + i)));
            chapters.get(i).active = i != chapter;
        }
        for (int row = 0; row < ROWS; row++) {
            Button option = plans.get(row);
            int index = first + row;
            option.visible = index < menu.recipes().size();
            if (option.visible) {
                var holder = menu.recipes().get(index);
                var label = planName(holder).copy().withColor(holder.value().stage() > stage ? MUTED & 0xFFFFFF : PAPER & 0xFFFFFF);
                if (!label.equals(option.getMessage())) {
                    option.setMessage(label);
                    String hint = "exora.workshop.hint." + holder.id().getPath().substring(holder.id().getPath().lastIndexOf('/') + 1);
                    option.setTooltip(Language.getInstance().has(hint) ? Tooltip.create(Component.translatable(hint)) : null);
                }
                option.active = index != selected;
            }
        }
        previous.active = first > 0;
        next.active = first + ROWS < menu.recipes().size();
        work.active = false;
        batch.visible = false;
        work.setWidth(148);
        if (selected >= menu.recipes().size()) return;
        var holder = menu.recipes().get(selected);
        boolean fit = WorkshopMenu.isInstall(holder);
        batch.visible = menu.driven() && !fit;
        if (batch.visible) {
            work.setWidth(102);
            batch.active = menu.canRun(holder, 4);
        }
        work.active = menu.canRun(holder, 1);
        if (holder.value().stage() > stage) work.setMessage(tr("needs_stage", tr("chapter." + holder.value().stage())));
        else if (fit && menu.driven()) work.setMessage(tr("fitted"));
        else if (!menu.isPress() && !menu.hasMallet()) work.setMessage(tr("need_mallet"));
        else if (menu.pressed()) work.setMessage(tr("returning"));
        else if (!work.active) work.setMessage(tr("need_stock"));
        else work.setMessage(tr(fit ? "fit_drive" : menu.isPress() ? "press" : "assemble"));
    }

    @Override
    protected void renderBg(GuiGraphics g, float partial, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        g.fill(x, y, x + imageWidth, y + imageHeight, EDGE);
        g.fill(x + 1, y + 1, x + imageWidth - 1, y + imageHeight - 1, BACK);
        g.fill(x + 5, y + 5, x + 7, y + 18, COPPER);
        g.fill(x + 8, y + 42, x + 292, y + 81, PANEL);
        g.fill(x + 8, y + 42, x + 10, y + 81, chapter < menu.stage() ? MINT : COPPER);
        g.fill(x + 135, y + 87, x + 136, y + 212, EDGE);
        g.fill(x + 141, y + 101, x + 289, y + 123, PANEL);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 12, 8, PAPER, false);
        Component progress = tr("proofs", menu.stage());
        g.drawString(font, progress, 290 - font.width(progress), 8, MINT, false);
        var text = tr("objective." + chapter);
        g.drawWordWrap(font, text, 16, 47, 271, PAPER);
        g.drawString(font, tr(menu.isPress() ? "dies" : "plans"), 9, 88, COPPER, false);
        g.drawString(font, tr("materials"), 141, 88, COPPER, false);
        int pages = Math.max(1, (menu.recipes().size() + ROWS - 1) / ROWS);
        g.drawCenteredString(font, (first / ROWS + 1) + " / " + pages, 69, 196, MUTED);
        if (selected >= menu.recipes().size()) return;
        var holder = menu.recipes().get(selected);
        var recipe = holder.value();
        g.renderItem(recipe.result(), 144, 104);
        String quantity = recipe.result().getCount() > 1 ? recipe.result().getCount() + " × " : "";
        g.drawString(font, Component.literal(quantity).append(planName(holder)), 165, 103, PAPER, false);
        var owned = menu.playerStock(recipe.result());
        g.drawString(font, tr("stock_total", owned), 165, 113, MUTED, false);
        int y = 127;
        for (var cost : recipe.materials()) {
            var examples = cost.ingredient().getItems();
            if (examples.length == 0) continue;
            ItemStack sample = examples[0];
            Component name = cost.ingredient().getValues().length == 1
                    && cost.ingredient().getValues()[0] instanceof net.minecraft.world.item.crafting.Ingredient.TagValue tag
                    && tag.tag().location().toString().equals("minecraft:planks") ? tr("planks") : sample.getHoverName();
            int count = menu.owned(cost);
            String tally = count + "/" + cost.count();
            int color = count >= cost.count() ? MINT : COPPER;
            g.drawString(font, font.plainSubstrByWidth(name.getString(), 140 - font.width(tally)), 143, y, PAPER, false);
            g.drawString(font, tally, 287 - font.width(tally), y, color, false);
            y += 12;
        }
        Component note;
        if (!menu.isPress()) note = tr(menu.hasMallet() ? "tool_ready" : "tool_recipe");
        else if (WorkshopMenu.isInstall(holder)) note = tr("upgrade_hint");
        else note = tr(menu.driven() ? "geared" : "hand_fed");
        g.drawString(font, note, 142, 178, MUTED, false);
    }
}
