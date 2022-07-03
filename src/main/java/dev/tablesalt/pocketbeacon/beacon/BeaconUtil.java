package dev.tablesalt.pocketbeacon.beacon;

import com.bekvon.bukkit.residence.commands.material;
import dev.tablesalt.pocketbeacon.BeaconPlugin;
import dev.tablesalt.pocketbeacon.PlayerCache;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Random;

@UtilityClass
public class BeaconUtil {

	@Getter
	private static final NamespacedKey key = new NamespacedKey(BeaconPlugin.getInstance(), "pocket_beacon");

	public static void giveBeacon(Player target) {
		target.getInventory().addItem(getBeaconItem());
	}

	public static ItemStack getBeaconItem() {
		return ItemCreator.of(CompMaterial.BEACON).name("&fPocket Beacon").glow(true).lore(
				"Right/Left Click to Open").make();
	}

	public static ShapedRecipe getRecipe() {


		ShapedRecipe recipe = new ShapedRecipe(key, getBeaconItem());

		recipe.shape("DSD", "NBN", "DSD");
		recipe.setIngredient('B', Material.BEACON);
		recipe.setIngredient('N', Material.NETHERITE_INGOT);
		recipe.setIngredient('D', Material.DIAMOND_BLOCK);
		recipe.setIngredient('S', Material.SHULKER_SHELL);

		return recipe;
	}

	public static void updateEffect(Player player, BeaconState nextState) {

		//get the players cache
		PlayerCache cache = PlayerCache.getCache(player);

		//get the current active effect and the next effect to switch to
		PotionEffectType currentEffect = null;
		if(cache.getCurrentState() != null) {
			currentEffect = BeaconState.toPotionEffectType(cache.getCurrentState());
		}
		PotionEffectType nextEffect = BeaconState.toPotionEffectType(nextState);


		if (currentEffect != null && currentEffect != nextEffect) {
			player.removePotionEffect(currentEffect);
		}

		//todo add the tier modifier if any
		// can assume that there is fuel since we would not be updating the effect otherwise.


		int modifier = 0;
		int fuelTier = BeaconFuel.getTier(cache.getBeaconFuel());

		if (nextState.getTier() < fuelTier)
			modifier = BeaconFuel.getEffectMultiplier(cache.getBeaconFuel());

		if (nextEffect != null && cache.getBeaconFuel() != null) {
			player.addPotionEffect(new PotionEffect(nextEffect,
					(nextState == BeaconState.NIGHT_VISION ? 20 : 2) * 20
					, modifier));
		}

		//update the cache to the new effect
		cache.setCurrentState(nextState);
	}

	public static boolean isHolding(Player player) {
		return player.getInventory().getItemInOffHand().isSimilar(getBeaconItem()) ||
				player.getInventory().getItemInMainHand().isSimilar(getBeaconItem());
	}

	public static ItemStack getColorfulGlass() {
		Random random = new Random();


		switch (random.nextInt(4)) {
			case 1:
				return makeGlass(Material.BLUE_STAINED_GLASS_PANE);
			case 2:
				return makeGlass(Material.PURPLE_STAINED_GLASS_PANE);
			case 3:
				return makeGlass(Material.RED_STAINED_GLASS_PANE);
		}

		return makeGlass(Material.PINK_STAINED_GLASS_PANE);

	}


	private ItemStack makeGlass(Material material) {
		return ItemCreator.of(CompMaterial.fromMaterial(material), "").make();
	}


}
