package io.github.sebasjuricek.exora.client.screen;

import io.github.sebasjuricek.exora.Exora;
import io.github.sebasjuricek.exora.energy.GeneratorEntity;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import io.github.sebasjuricek.exora.energy.StationKind;
import io.github.sebasjuricek.exora.energy.StationMenu;
import io.github.sebasjuricek.exora.machines.powered.MillingRecipe;
import io.github.sebasjuricek.exora.machines.powered.MillStatus;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Exora.MOD_ID, value = Dist.CLIENT)
public final class PowerStationScreen extends AbstractContainerScreen<StationMenu> {
    private static final int BACK = 0xFF152426, PANEL = 0xFF223638, EDGE = 0xFF425554;
    private static final int TEXT = 0xFFF0E6CD, SUBTLE = 0xFFACC0B5, COPPER = 0xFFD99765, MINT = 0xFF94D7B4;
    private List<RecipeHolder<MillingRecipe>> plans = List.of();
    private int plan;
    private boolean showPlan;

    public PowerStationScreen(StationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 220;
        imageHeight = 216;
    }

    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent event) {
        event.register(PowerWorkshop.GENERATOR_MENU.get(), PowerStationScreen::new);
        event.register(PowerWorkshop.MILL_MENU.get(), PowerStationScreen::new);
        event.register(PowerWorkshop.BUFFER_MENU.get(), PowerStationScreen::new);
    }

    private static Component tr(String key, Object... args) {
        return Component.translatable("exora.station." + key, args);
    }

    @Override
    protected void init() {
        super.init();
        if (menu.kind == StationKind.MILL) {
            plans = minecraft.level.getRecipeManager().getAllRecipesFor(PowerWorkshop.MILLING.get())
                    .stream().sorted(Comparator.comparing(RecipeHolder::id)).toList();
            if (plans.isEmpty()) return;
            Button selector = addRenderableWidget(Button.builder(tr("recipes"), b -> {
                if (showPlan) plan++;
                showPlan = plan < plans.size();
                if (!showPlan) plan = 0;
                b.setMessage(showPlan ? tr("plan_count", plan + 1, plans.size()) : tr("recipes"));
            }).bounds(leftPos + 142, topPos + 5, 69, 15).build());
            selector.setTooltip(Tooltip.create(tr("recipes_hint")));
        }
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        g.fill(x, y, x + imageWidth, y + imageHeight, EDGE);
        g.fill(x + 1, y + 1, x + imageWidth - 1, y + imageHeight - 1, BACK);
        g.fill(x + 5, y + 6, x + 7, y + 18, COPPER);
        g.fill(x + 8, y + 22, x + 212, y + 65, PANEL);
        int stored = menu.value(StationMenu.ENERGY);
        meter(g, 9, 81, 202, stored, menu.kind.capacity, MINT);
        if (menu.kind == StationKind.MILL) {
            meter(g, 52, 54, 114, menu.value(StationMenu.PROGRESS), menu.value(StationMenu.DURATION), COPPER);
        } else if (menu.kind == StationKind.GENERATOR) {
            meter(g, 52, 54, 148, menu.value(StationMenu.PROGRESS), GeneratorEntity.BURN_TICKS, COPPER);
        }
        for (Slot slot : menu.slots) {
            int sx = x + slot.x, sy = y + slot.y;
            g.fill(sx - 1, sy - 1, sx + 17, sy + 17, EDGE);
            g.fill(sx, sy, sx + 16, sy + 16, BACK);
        }
    }

    private void meter(GuiGraphics g, int x, int y, int width, int current, int max, int color) {
        x += leftPos;
        y += topPos;
        g.fill(x, y, x + width, y + 4, EDGE);
        if (max > 0) g.fill(x, y, x + width * Mth.clamp(current, 0, max) / max, y + 4, color);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 12, 8, TEXT, false);
        int stored = menu.value(StationMenu.ENERGY);
        Component hint;
        if (menu.kind == StationKind.MILL) {
            MillStatus[] statuses = MillStatus.values();
            MillStatus status = statuses[Mth.clamp(menu.value(StationMenu.STATUS), 0, statuses.length - 1)];
            String key = status.getSerializedName();
            if (status == MillStatus.IDLE && menu.getSlot(1).hasItem()) key = "ready";
            g.drawCenteredString(font, tr("feed"), 35, 26, SUBTLE);
            g.drawCenteredString(font, tr("tray"), 183, 26, SUBTLE);
            g.drawCenteredString(font, tr(key), 109, 26, status == MillStatus.RUNNING ? MINT : TEXT);
            int itemId = (menu.value(StationMenu.WORKPIECE_HIGH) << 16) | (menu.value(StationMenu.WORKPIECE) & 0xffff);
            ItemStack workpiece = new ItemStack(Item.byId(itemId));
            if (!workpiece.isEmpty()) {
                g.renderItem(workpiece, 80, 36);
                int percent = 100 * menu.value(StationMenu.PROGRESS) / Math.max(1, menu.value(StationMenu.DURATION));
                g.drawString(font, percent + "%", 103, 40, TEXT, false);
            }
            hint = tr(key + "_hint");
            if (showPlan && !plans.isEmpty()) {
                MillingRecipe recipe = plans.get(plan).value();
                ItemStack[] inputs = recipe.getIngredients().getFirst().getItems();
                ItemStack result = recipe.getResultItem(minecraft.level.registryAccess());
                if (inputs.length > 0) {
                    hint = tr("plan", inputs[0].getHoverName(), result.getCount(), result.getHoverName(),
                            recipe.ticks() * recipe.energyPerTick());
                }
            }
        } else if (menu.kind == StationKind.GENERATOR) {
            int remaining = menu.value(StationMenu.PROGRESS);
            String key = stored > menu.kind.capacity - GeneratorEntity.GENERATION ? "governed" : remaining > 0 ? "burning" : "out";
            g.drawCenteredString(font, tr("fuel"), 35, 26, SUBTLE);
            g.drawString(font, tr(key), 52, 27, TEXT, false);
            g.drawString(font, tr("burn_left", (remaining + 19) / 20), 52, 41, SUBTLE, false);
            hint = tr(key + "_hint");
        } else {
            g.drawString(font, tr(stored > 0 ? "charged" : "empty"), 17, 28, TEXT, false);
            g.drawString(font, tr("buffer_rate"), 17, 44, SUBTLE, false);
            hint = tr("buffer_hint");
        }
        g.drawString(font, tr("charge", stored, menu.kind.capacity), 9, 69, MINT, false);
        g.drawWordWrap(font, hint, 9, 90, 202, TEXT);
        g.drawString(font, playerInventoryTitle, 28, 119, SUBTLE, false);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }
}
