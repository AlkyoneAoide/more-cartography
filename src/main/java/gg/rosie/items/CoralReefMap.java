package gg.rosie.items;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapDecorationsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class CoralReefMap extends EmptyMapItem {
	public CoralReefMap(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack emptyMap = user.getStackInHand(hand);

		if (world.isClient)
			return TypedActionResult.success(emptyMap);
		else {
			// Increment stats and decrement item
			emptyMap.decrementUnlessCreative(1, user);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			user.getWorld().playSoundFromEntity(null, user, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, user.getSoundCategory(), 1f, 1f);

			// Get biome coords
			Pair<BlockPos, RegistryEntry<Biome>> coordsPair = ((ServerWorld)world).locateBiome(biomeRegistryEntry -> biomeRegistryEntry.matchesKey(BiomeKeys.WARM_OCEAN),
					user.getBlockPos(), 12801, 32, 64);

			ItemStack filledMap;
			BlockPos coords;

			// make sure coords is non-null
			if (coordsPair == null) {
				coords = BlockPos.fromLong(world.random.nextLong());
				filledMap = DubiousMap.createMap(world, coords.getX(), coords.getZ(), (byte) 1, true, true);
				DubiousMap.fillExplorationMap((ServerWorld) world, filledMap);

				ComponentMap componentMap = filledMap.getComponents();
				MapDecorationsComponent mapDecorationsComponent = componentMap.get(DataComponentTypes.MAP_DECORATIONS);
				mapDecorationsComponent = mapDecorationsComponent.with("red_x", new MapDecorationsComponent.Decoration(MapDecorationTypes.RED_X, coords.getX(), coords.getZ(), 0f));
				filledMap.set(DataComponentTypes.MAP_DECORATIONS, mapDecorationsComponent);
				filledMap.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.more-cartography.dubious_map"));
			} else {
				// Create new filled map
				coords = coordsPair.getFirst();
				filledMap = FilledMapItem.createMap(world, coords.getX(), coords.getZ(), (byte) 1, true, true);
				FilledMapItem.fillExplorationMap((ServerWorld) world, filledMap);

				ComponentMap componentMap = filledMap.getComponents();
				MapDecorationsComponent mapDecorationsComponent = componentMap.get(DataComponentTypes.MAP_DECORATIONS);
				mapDecorationsComponent = mapDecorationsComponent.with("red_x", new MapDecorationsComponent.Decoration(MapDecorationTypes.RED_X, coords.getX(), coords.getZ(), 0f));
				filledMap.set(DataComponentTypes.MAP_DECORATIONS, mapDecorationsComponent);
				filledMap.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.more-cartography.coral_map"));
			}

			// Replace old map with filled map
			if (emptyMap.isEmpty())
				return TypedActionResult.consume(filledMap);
			else {
				if (!user.getInventory().insertStack(filledMap.copy()))
					user.dropItem(filledMap, false);

				return TypedActionResult.consume(emptyMap);
			}
		}
	}
}
