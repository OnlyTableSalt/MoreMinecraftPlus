package dev.tablesalt.pocketbeacon.task;

import dev.tablesalt.pocketbeacon.PlayerCache;
import dev.tablesalt.pocketbeacon.beacon.BeaconState;
import dev.tablesalt.pocketbeacon.beacon.BeaconUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

public class BeaconTask extends BukkitRunnable {


	@Override
	public void run() {
		for (Player player : Remain.getOnlinePlayers()) {
			try {
				if (player.getInventory().getItemInOffHand().equals(BeaconUtil.getBeaconItem()) ||
						player.getInventory().getItemInMainHand().equals(BeaconUtil.getBeaconItem())) {

					PlayerCache cache = PlayerCache.getCache(player);

					if (cache.getBeaconFuel() == null || !cache.getBeaconFuel().isBurning()) {
						return;
					}

					switch (cache.getCurrentState()) {
						case NO_EFFECT:
							BeaconUtil.updateEffect(player, BeaconState.NO_EFFECT);
							break;
						case SPEED:
							BeaconUtil.updateEffect(player, BeaconState.SPEED);
							break;
						case JUMP_BOOST:
							BeaconUtil.updateEffect(player, BeaconState.JUMP_BOOST);
							break;
						case HASTE:
							BeaconUtil.updateEffect(player, BeaconState.HASTE);
							break;
						case RESISTANCE:
							BeaconUtil.updateEffect(player, BeaconState.RESISTANCE);
							break;
						case SLOW_FALLING:
							BeaconUtil.updateEffect(player, BeaconState.SLOW_FALLING);
							break;
						case LEVITATION:
							BeaconUtil.updateEffect(player, BeaconState.LEVITATION);
							break;
						case NIGHT_VISION:
							BeaconUtil.updateEffect(player, BeaconState.NIGHT_VISION);
							break;
						case FIRE_RESISTANCE:
							BeaconUtil.updateEffect(player, BeaconState.FIRE_RESISTANCE);
							break;
						case REGENERATION:
							BeaconUtil.updateEffect(player, BeaconState.REGENERATION);
							break;
						case STRENGTH:
							BeaconUtil.updateEffect(player, BeaconState.STRENGTH);
							break;
						case FAST_SWIMMING:
							BeaconUtil.updateEffect(player, BeaconState.FAST_SWIMMING);
							break;
						case WATER_BREATHING:
							BeaconUtil.updateEffect(player, BeaconState.WATER_BREATHING);
							break;

					}
				}
			} catch (Exception exception){

			}
		}
	}
}
