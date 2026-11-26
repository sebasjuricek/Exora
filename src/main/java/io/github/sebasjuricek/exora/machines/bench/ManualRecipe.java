package io.github.sebasjuricek.exora.machines.bench;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sebasjuricek.exora.machines.ManualWorkshop;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

/** A manual assembly or pressing operation. Materials may be split across inventory slots. */
public record ManualRecipe(boolean press, int stage, int order, List<SizedIngredient> materials,
                           ItemStack result) implements Recipe<RecipeInput> {
    public ManualRecipe {
        materials = List.copyOf(materials);
    }

    /** Reserve all materials before mutating any stack, including when ingredient tags overlap. */
    public int[] consumption(RecipeInput stock, int batches) {
        int[] used = new int[stock.size()];
        for (SizedIngredient cost : materials) {
            long needed = (long) cost.count() * batches;
            for (int slot = 0; slot < used.length && needed > 0; slot++) {
                ItemStack stack = stock.getItem(slot);
                if (cost.ingredient().test(stack)) {
                    int take = (int) Math.min(needed, stack.getCount() - used[slot]);
                    used[slot] += take;
                    needed -= take;
                }
            }
            if (needed > 0) return null;
        }
        return used;
    }

    @Override public boolean matches(RecipeInput input, Level level) { return consumption(input, 1) != null; }
    @Override public ItemStack assemble(RecipeInput input, HolderLookup.Provider lookup) { return result.copy(); }
    @Override public ItemStack getResultItem(HolderLookup.Provider lookup) { return result; }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public boolean isSpecial() { return true; }
    @Override public RecipeType<?> getType() { return ManualWorkshop.MANUAL.get(); }
    @Override public RecipeSerializer<?> getSerializer() { return ManualWorkshop.SERIALIZER.get(); }

    public static final class Serializer implements RecipeSerializer<ManualRecipe> {
        private static final MapCodec<ManualRecipe> CODEC = RecordCodecBuilder.mapCodec(fields -> fields.group(
                Codec.BOOL.optionalFieldOf("press", false).forGetter(ManualRecipe::press),
                Codec.intRange(0, 3).fieldOf("stage").forGetter(ManualRecipe::stage),
                Codec.INT.optionalFieldOf("order", 0).forGetter(ManualRecipe::order),
                SizedIngredient.FLAT_CODEC.listOf(1, 4).fieldOf("materials").forGetter(ManualRecipe::materials),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ManualRecipe::result)
        ).apply(fields, ManualRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ManualRecipe> STREAM =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
        @Override public MapCodec<ManualRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, ManualRecipe> streamCodec() { return STREAM; }
    }
}
