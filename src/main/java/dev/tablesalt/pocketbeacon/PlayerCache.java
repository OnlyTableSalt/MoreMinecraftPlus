package dev.tablesalt.pocketbeacon;

import dev.tablesalt.pocketbeacon.beacon.BeaconFuel;
import dev.tablesalt.pocketbeacon.beacon.BeaconState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mineacademy.fo.jsonsimple.JsonSimpleUtil.getEnum;

public class PlayerCache extends YamlConfig {

	@Getter
	@Setter
	private BeaconState currentState = BeaconState.NO_EFFECT;

	@Getter
	@Setter
	private BeaconFuel beaconFuel;
	private UUID uuid;

	protected PlayerCache(final String uuid) {
		this.uuid = UUID.fromString(uuid);
		loadConfiguration(null, "data.db");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (isSet(uuid.toString()+"Beacon_Fuel"))
			beaconFuel = BeaconFuel.deserialize(getMap(uuid.toString()+"Beacon_Fuel"));

		if (isSet(uuid.toString()+"Effect_State"))
			currentState = getEnum(uuid.toString()+"Effect_State", BeaconState.class); else
				currentState = BeaconState.NO_EFFECT;

	}

	public void saveData() {


		if (beaconFuel != null) {
			save(uuid.toString()+"Beacon_Fuel", beaconFuel.saveToMap());
			save(uuid.toString()+"Effect_State", currentState);
		} else {
			save(uuid.toString()+"Beacon_Fuel",  new BeaconFuel(new ItemStack(Material.AIR)).saveToMap());
			save(uuid.toString()+"Effect_State", BeaconState.NO_EFFECT);

		}

	}
	//-----------------------------------//-----------------------------------//
	//                                  STATIC
	//-----------------------------------//-----------------------------------//

	@Getter
	private static Map<UUID, PlayerCache> cacheMap = new HashMap<>();

	public static PlayerCache getCache(Player player) {
		PlayerCache cache = cacheMap.get(player.getUniqueId());

		if (cache == null) {
			cache = new PlayerCache(player.getUniqueId().toString());
			cacheMap.put(player.getUniqueId(), cache);
		}
		return cache;
	}
}
