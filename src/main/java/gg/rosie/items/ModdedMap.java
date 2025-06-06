package gg.rosie.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapDecorationsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ModdedMap extends EmptyMapItem {
    private final RegistryKey<Biome> biome;
//    private final TagKey<Block> block;
    private String itemName;

    public ModdedMap(Settings settings, RegistryKey<Biome> biome, String itemName) {
        super(settings);
        this.biome = biome;
//        this.block = null;
        this.itemName = "item.more-cartography." + itemName;
    }

 /*
    public ModdedMap(Settings settings, RegistryKey<Biome> biome, TagKey<Block> block, String itemName) {
        super(settings);
        this.biome = biome;
        this.block = block;
        this.itemName = itemName;
    }
 */

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack emptyMap = user.getStackInHand(hand);

        if (world.isClient)
            return TypedActionResult.success(emptyMap);
        else {
            // Increment stats, decrement item count, play sound
            emptyMap.decrementUnlessCreative(1, user);

            user.incrementStat(Stats.USED.getOrCreateStat(this));

            user.getWorld().playSoundFromEntity(null, user,
                    SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, user.getSoundCategory(), 1f, 1f);

            // Get biome coords
            BlockPos coords = locateBiome((ServerWorld) world, user);
            ItemStack filledMap;

            //Create and fill map item
            if (coords == null) {
                coords = BlockPos.fromLong(world.random.nextLong());

                filledMap = DubiousMap.createMap(world, coords.getX(), coords.getZ(),
                        (byte) 1, true, true);
                DubiousMap.fillExplorationMap((ServerWorld) world, filledMap);

                this.itemName = "item.more-cartography.dubious_map";
            } else {
                filledMap = FilledMapItem.createMap(world, coords.getX(), coords.getZ(),
                        (byte) 1, true, true);
                FilledMapItem.fillExplorationMap((ServerWorld) world, filledMap);
            }

            // Decorate and name map item
            ComponentMap componentMap = filledMap.getComponents();

            MapDecorationsComponent mapDecorationsComponent = componentMap.get(DataComponentTypes.MAP_DECORATIONS);
            mapDecorationsComponent = mapDecorationsComponent.with("red_x",
                    new MapDecorationsComponent.Decoration(MapDecorationTypes.RED_X, coords.getX(), coords.getZ(), 0f));

            filledMap.set(DataComponentTypes.MAP_DECORATIONS, mapDecorationsComponent);
            filledMap.set(DataComponentTypes.ITEM_NAME, Text.translatable(this.itemName));

            // Delete old map item stack if empty, give new map to user
            if (emptyMap.isEmpty())
                return TypedActionResult.consume(filledMap);
            else {
                if (!user.getInventory().insertStack(filledMap.copy()))
                    user.dropItem(filledMap, false);

                return TypedActionResult.consume(emptyMap);
            }
        }
    }

    public BlockPos locateBiome(ServerWorld world, PlayerEntity user) {
        Pair<BlockPos, RegistryEntry<Biome>> coordsPair = world.locateBiome(
                biomeRegistryEntry -> biomeRegistryEntry.matchesKey(biome),
                user.getBlockPos(), 12801, 32, 64);

//        if (coordsPair == null || isBlockNearby(world, coordsPair.getFirst(), 50))
        if (coordsPair == null)
            return null;
        return coordsPair.getFirst();
    }

 /*
    public boolean isBlockNearby(ServerWorld world, BlockPos origin, int range) {
        Chunk chunk;
        final boolean[] found = {false};
        int x;
        int z;

        for (x = origin.getX() - range; x < origin.getX() + range; x+=16) {
            for (z = origin.getY() - range; z < origin.getY() + range; z+=16) {
                chunk = world.getChunk(x, z);
                found[0] = false;
                chunk.forEachBlockMatchingPredicate(blockState -> blockState.isIn(this.block),
                        (pos, state) -> found[0] = true);

                if (found[0])
                    return true;
            }
        }

        return false;
    }
 */
}
