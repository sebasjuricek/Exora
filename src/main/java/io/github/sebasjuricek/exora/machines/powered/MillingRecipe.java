package io.github.sebasjuricek.exora.machines.powered;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sebasjuricek.exora.energy.PowerWorkshop;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class MillingRecipe extends SingleItemRecipe {
    public static final int MAX_TICKS = 24000;
    private final int ticks;
    private final int energyPerTick;

    public MillingRecipe(Ingredient ingredient, ItemStack result, int ticks, int energyPerTick) {
        super(PowerWorkshop.MILLING.get(), PowerWorkshop.MILLING_SERIALIZER.get(), "", ingredient, result);
        this.ticks = ticks;
        this.energyPerTick = energyPerTick;
    }

    public int ticks() { return ticks; }
    public int energyPerTick() { return energyPerTick; }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static final class Serializer implements RecipeSerializer<MillingRecipe> {
        private static final MapCodec<MillingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                Codec.intRange(1, MAX_TICKS).fieldOf("ticks").forGetter(MillingRecipe::ticks),
                Codec.intRange(1, 48).fieldOf("energy_per_tick").forGetter(MillingRecipe::energyPerTick)
        ).apply(i, MillingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, MillingRecipe> STREAM = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                ItemStack.STREAM_CODEC, r -> r.result,
                ByteBufCodecs.VAR_INT, MillingRecipe::ticks,
                ByteBufCodecs.VAR_INT, MillingRecipe::energyPerTick, MillingRecipe::new);

        @Override
        public MapCodec<MillingRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MillingRecipe> streamCodec() { return STREAM; }
    }
}
