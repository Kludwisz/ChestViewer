package loot;

import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mcfeature.loot.LootContext;
import com.seedfinding.mcfeature.loot.LootTable;
import com.seedfinding.mcfeature.loot.MCLootTables;
import com.seedfinding.mcfeature.loot.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class BastionLootGenerator {
    private final LootTable bastionLootTable = MCLootTables.BASTION_OTHER_CHEST.get();
    private long iseed;

    public void setInternalSeed(long seed) {
        this.iseed = seed;
    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> loot = new LinkedList<>();

        ChunkRand rand = new ChunkRand();
        LootContext ctx = new LootContext(0L);

        rand.setSeed(iseed, false);
        long lowerChestSeed = rand.nextLong();
        long upperChestSeed = rand.nextLong();

        ctx.setSeed(upperChestSeed);
        loot.addAll(bastionLootTable.generateIndexed(ctx));
        ctx.setSeed(lowerChestSeed);
        loot.addAll(bastionLootTable.generateIndexed(ctx));

        return loot;
    }
}
