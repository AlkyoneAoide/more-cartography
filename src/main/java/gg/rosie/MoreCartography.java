package gg.rosie;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MoreCartography implements ModInitializer {
	public static final String MOD_ID = "more-cartography";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 2, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new ItemStack(Items.EMERALD, 7),
					new ItemStack(Objects.requireNonNull(ModItems.getItem("coral_reef_map")), 1),
					12, 10, 0.05f));
		});
	}
}
