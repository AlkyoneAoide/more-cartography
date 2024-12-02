package gg.rosie.items;

import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class CoralReefMap extends FilledMapItem {
	public CoralReefMap(Settings settings) {
		super(settings);
	}

	public static ItemStack createMap(World world, int x, int z, byte scale, boolean showIcons,
			boolean unlimitedTracking) {
		ServerWorld serverWorld = null;
		if (!world.isClient()) {
			serverWorld = (ServerWorld) world;
		}

		ItemStack stack = new ItemStack(Items.FILLED_MAP, 1);
		fillExplorationMap(serverWorld, stack);
		return stack;
	};
}
