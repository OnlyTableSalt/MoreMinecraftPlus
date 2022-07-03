package dev.tablesalt.pocketbeacon.settings;

import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 2;
	}


	public static class FuelTypes {

		public static Integer BLOCK_BURN_MULTIPLIER = getInteger("Block_Burn_Multiplier");

		public static Integer COALMULTIPLIER, IRONMULTIPLIER, GOLDMULTIPLIER, EMERALDMULTIPLIER, DIAMONDMULTIPLIER;

		public static SimpleTime COALBURN, IRONBURN, GOLDBURN, EMERALDBURN, DIAMONDBURN;

		//todo update config version
		private static void init() {
			COALMULTIPLIER = getInteger("Fuel_Types.Coal.Multiplier");
			IRONMULTIPLIER = getInteger("Fuel_Types.Iron.Multiplier");
			GOLDMULTIPLIER = getInteger("Fuel_Types.Gold.Multiplier");
			EMERALDMULTIPLIER = getInteger("Fuel_Types.Emerald.Multiplier");
			DIAMONDMULTIPLIER = getInteger("Fuel_Types.Diamond.Multiplier");

			COALBURN = getTime("Fuel_Types.Coal.Burn_Time");
			IRONBURN = getTime("Fuel_Types.Iron.Burn_Time");
			GOLDBURN = getTime("Fuel_Types.Gold.Burn_Time");
			EMERALDBURN = getTime("Fuel_Types.Emerald.Burn_Time");
			DIAMONDBURN = getTime("Fuel_Types.Diamond.Burn_Time");
		}
	}


}
