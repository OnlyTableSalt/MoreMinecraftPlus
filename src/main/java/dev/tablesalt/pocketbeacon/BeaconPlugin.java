package dev.tablesalt.pocketbeacon;

import dev.tablesalt.pocketbeacon.beacon.BeaconUtil;
import dev.tablesalt.pocketbeacon.command.BeaconCommandGroup;
import dev.tablesalt.pocketbeacon.listener.BeaconListener;
import dev.tablesalt.pocketbeacon.task.BeaconTask;
import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * PluginTemplate is a simple template you can use every time you make
 * a new plugin. This will save you time because you no longer have to
 * recreate the same skeleton and features each time.
 * <p>
 * It uses Foundation for fast and efficient development process.
 */
public final class BeaconPlugin extends SimplePlugin {


	/**
	 * Automatically perform login ONCE when the plugin starts.
	 */
	@Override
	protected void onPluginStart() {


	}

	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {


		registerCommands(BeaconCommandGroup.getInstance());
		registerEvents(BeaconListener.getInstance());
		Bukkit.addRecipe(BeaconUtil.getRecipe());

		BeaconTask beaconTask = new BeaconTask();
		beaconTask.runTaskTimer(this, 0, 5);
	}

	@Override
	protected void onPluginStop() {
		

	}

	/* ------------------------------------------------------------------------------- */
	/* Static */
	/* ------------------------------------------------------------------------------- */

	/**
	 * Return the instance of this plugin, which simply refers to a static
	 * field already created for you in SimplePlugin but casts it to your
	 * specific plugin instance for your convenience.
	 *
	 * @return instance of the main plugin
	 */
	public static BeaconPlugin getInstance() {
		return (BeaconPlugin) SimplePlugin.getInstance();
	}
}
