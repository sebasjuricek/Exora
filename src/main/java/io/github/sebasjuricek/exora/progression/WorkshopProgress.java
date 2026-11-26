package io.github.sebasjuricek.exora.progression;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.PlayerTeam;

import java.util.HashMap;
import java.util.Map;

/** Sequential proofs, owned by the current scoreboard team or by an unteamed player's UUID. */
public final class WorkshopProgress extends SavedData {
    private static final Factory<WorkshopProgress> FACTORY =
            new Factory<>(WorkshopProgress::new, WorkshopProgress::load);
    private final Map<String, Integer> ledgers = new HashMap<>();

    private static WorkshopProgress get(ServerPlayer player) {
        return player.server.overworld().getDataStorage().computeIfAbsent(FACTORY, "exora_workshop");
    }

    private static String ledger(ServerPlayer player) {
        PlayerTeam team = player.getTeam();
        return team == null ? "player/" + player.getUUID() : "team/" + team.getName();
    }

    public static int stage(ServerPlayer player) {
        return get(player).ledgers.getOrDefault(ledger(player), 0);
    }

    /** Call only after a station has committed the corresponding physical operation. */
    public static boolean prove(ServerPlayer player, int milestone) {
        WorkshopProgress progress = get(player);
        String key = ledger(player);
        if (milestone < 1 || milestone > 3 || milestone != progress.ledgers.getOrDefault(key, 0) + 1) {
            return false;
        }
        progress.ledgers.put(key, milestone);
        progress.setDirty();
        Component notice = Component.translatable("exora.workshop.proof." + milestone)
                .withStyle(milestone == 3 ? ChatFormatting.GOLD : ChatFormatting.AQUA);
        for (ServerPlayer member : player.server.getPlayerList().getPlayers()) {
            if (ledger(member).equals(key)) {
                member.sendSystemMessage(notice);
            }
        }
        return true;
    }

    private static WorkshopProgress load(CompoundTag root, HolderLookup.Provider registries) {
        WorkshopProgress progress = new WorkshopProgress();
        CompoundTag saved = root.getCompound("Ledgers");
        for (String key : saved.getAllKeys()) {
            int stage = saved.getInt(key);
            if (stage > 0 && stage <= 3) {
                progress.ledgers.put(key, stage);
            }
        }
        return progress;
    }

    @Override
    public CompoundTag save(CompoundTag root, HolderLookup.Provider registries) {
        CompoundTag saved = new CompoundTag();
        ledgers.forEach(saved::putInt);
        root.putInt("Version", 1);
        root.put("Ledgers", saved);
        return root;
    }
}
