package dev.tablesalt.pocketbeacon.beacon;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public enum BeaconState {
	NO_EFFECT(0, Material.MILK_BUCKET),

	JUMP_BOOST(1, Material.RABBIT_FOOT),
	SPEED(1, Material.SUGAR),
	SLOW_FALLING(1, Material.SHULKER_SHELL),
	NIGHT_VISION(1, Material.GOLDEN_CARROT),

	HASTE(2, Material.GOLDEN_AXE),
	FAST_SWIMMING(2, Material.TROPICAL_FISH),
	WATER_BREATHING(2, Material.TRIDENT),
	LEVITATION(2, Material.PHANTOM_MEMBRANE),

	RESISTANCE(3, Material.SHIELD),
	FIRE_RESISTANCE(3, Material.MAGMA_CREAM),
	REGENERATION(3, Material.COOKED_BEEF),
	STRENGTH(3, Material.NETHERITE_AXE);


	@Getter
	int tier;
	@Getter
	ItemStack item;

	BeaconState(int tier, Material material) {
		this.tier = tier;
		item = ItemCreator.of(CompMaterial.fromMaterial(material),
				ItemUtil.bountifyCapitalized(this.toString())).make();
	}

	public static PotionEffectType toPotionEffectType(BeaconState beaconState) {
		switch (beaconState) {
			case NO_EFFECT:
				return null;
			case SPEED:
				return PotionEffectType.SPEED;
			case JUMP_BOOST:
				return PotionEffectType.JUMP;
			case HASTE:
				return PotionEffectType.FAST_DIGGING;
			case RESISTANCE:
				return PotionEffectType.DAMAGE_RESISTANCE;
			case SLOW_FALLING:
				return PotionEffectType.SLOW_FALLING;
			case NIGHT_VISION:
				return PotionEffectType.NIGHT_VISION;
			case FIRE_RESISTANCE:
				return PotionEffectType.FIRE_RESISTANCE;
			case REGENERATION:
				return PotionEffectType.REGENERATION;
			case LEVITATION:
				return PotionEffectType.LEVITATION;
			case STRENGTH:
				return PotionEffectType.INCREASE_DAMAGE;
			case FAST_SWIMMING:
				return PotionEffectType.DOLPHINS_GRACE;
			case WATER_BREATHING:
				return PotionEffectType.WATER_BREATHING;
		}
		return null;
	}
}
