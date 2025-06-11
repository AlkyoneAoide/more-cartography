package gg.rosie;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

public class MoreCartography implements ModInitializer {
	public static final String MOD_ID = "more-cartography";

	@Override
	public void onInitialize() {
		ModItems.register();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 5, factories -> {
			for (Item item : ModItems.getItems()) {
				factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 7),
					Optional.of(new TradedItem(Items.COMPASS, 1)),
					new ItemStack(item, 1),
					12, 10, 0.05f));
			}
		});
	}
}
