package dev.tablesalt.pocketbeacon.beacon;

import dev.tablesalt.pocketbeacon.PlayerCache;
import dev.tablesalt.pocketbeacon.settings.Settings;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BeaconMenu extends Menu {


	private final ButtonMenu fuelButton;
	private final ButtonMenu tierOne;
	private final ButtonMenu tierTwo;
	private final ButtonMenu tierThree;

	private final Button clearButton;


	public BeaconMenu() {

		setSize(9);
		setTitle(ChatUtil.generateGradient("Pocket Beacon", CompChatColor.AQUA, CompChatColor.DARK_BLUE));

		clearButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				BeaconUtil.updateEffect(player, BeaconState.NO_EFFECT);

				new SimpleSound(Sound.BLOCK_CHAIN_BREAK, 5, MathUtil.range(7, 3, 7)).play(player);
				new SimpleSound(Sound.BLOCK_END_PORTAL_FRAME_FILL, 5, MathUtil.range(2, 0, 1)).play(player);
			}

			@Override
			public ItemStack getItem() {
				return BeaconState.NO_EFFECT.getItem();
			}
		};


		tierOne = new ButtonMenu(new BeaconTierMenu(1, "Pocket Beacon Tier One"),
				CompMaterial.COAL_BLOCK, "Tier 1");

		tierTwo = new ButtonMenu(new BeaconTierMenu(2, "Pocket Beacon Tier Two"),
				CompMaterial.IRON_BLOCK, "Tier 2");

		tierThree = new ButtonMenu(new BeaconTierMenu(3, "Pocket Beacon Tier Three"),
				CompMaterial.GOLD_BLOCK, "Tier 3");


		fuelButton = new ButtonMenu(new FuelMenu(), CompMaterial.BLAST_FURNACE,
				"Fuel Menu",
				"Click to Open",
				"The Refueling Menu");


	}

	@Override
	public ItemStack getItemAt(int slot) {
		//getting the players cache
		if (slot == 7)
			return clearButton.getItem();

		//tier menus
		if (slot == 1)
			return tierOne.getItem();
		if (slot == 2)
			return tierTwo.getItem();
		if (slot == 3)
			return tierThree.getItem();

		//centerpiece
		if (slot == getCenterSlot())
			return ItemCreator.of(CompMaterial.BEACON, "&fPocket Beacon").glow(true).make();

		//fuel menu
		if (slot == 8)
			return fuelButton.getItem();

		//filler glass
		if (slot > 0 && slot < 9) {
			return BeaconUtil.getColorfulGlass();
		}

		return null;
	}

	@Override
	protected String[] getInfo() {
		return new String[]{
				"Pocket Beacon Menu!",
				"Click one of the effects",
				"to activate the beacon."
		};
	}


	//-----------------------------------//----------------------------------------------------------------------//-----------------------------------//
	//                                 									Tiered Menu
	//-----------------------------------//----------------------------------------------------------------------//-----------------------------------//


	//The BeaconTierMenu allows the player to click on an effect assuming the have the right fuel type,
	// and setting their effect respectively
	private class BeaconTierMenu extends MenuPagged<BeaconState> {

		int tier;

		BeaconTierMenu(int tier, String title) {
			super(9, BeaconMenu.this, Arrays.stream(BeaconState.values())
					.filter(state -> state.getTier() == tier)
					.collect(Collectors.toList()), true);

			setTitle(ChatUtil.generateGradient(title, CompChatColor.AQUA, CompChatColor.DARK_BLUE));
			this.tier = tier;
		}


		@Override
		protected ItemStack convertToItemStack(BeaconState state) {
			return state.getItem();
		}

		@Override
		protected void onPageClick(Player player, BeaconState state, ClickType click) {

			PlayerCache cache = PlayerCache.getCache(player);

			//is there fuel present in the cache?
			if (cache.getBeaconFuel() == null || !cache.getBeaconFuel().isBurning()) {
				animateTitle(ChatUtil.generateGradient("No Fuel Present", CompChatColor.RED, CompChatColor.DARK_RED));
				new SimpleSound(Sound.BLOCK_CALCITE_BREAK, 5, MathUtil.range(2, 0, 1)).play(player);
				return;
			}

			//is the fuel the right tier?
			if (BeaconFuel.getTier(cache.getBeaconFuel()) < state.getTier()) {
				animateTitle(ChatUtil.generateGradient("Need better fuel", CompChatColor.RED, CompChatColor.DARK_RED));
				new SimpleSound(Sound.BLOCK_CALCITE_BREAK, 5, MathUtil.range(2, 0, 1)).play(player);
				return;
			}

			PotionEffectType effectType = BeaconState.toPotionEffectType(state);


			if (effectType != null && player.hasPotionEffect(effectType)) {
				animateTitle(ChatUtil.generateGradient("Effect Already Active", CompChatColor.RED, CompChatColor.DARK_RED));
				new SimpleSound(Sound.BLOCK_CALCITE_BREAK, 5, MathUtil.range(2, 0, 1)).play(player);
				return;
			}

			BeaconUtil.updateEffect(player, state);

			new SimpleSound(Sound.BLOCK_CHAIN_BREAK, 5, MathUtil.range(7, 3, 7)).play(player);
			new SimpleSound(Sound.BLOCK_END_PORTAL_FRAME_FILL, 5, MathUtil.range(2, 0, 1)).play(player);
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					""
			};
		}
	}


	//-----------------------------------//----------------------------------------------------------------------//-----------------------------------//
	//                                 									FUEL MENU
	//-----------------------------------//----------------------------------------------------------------------//-----------------------------------//


	private final class FuelMenu extends Menu {


		FuelMenu() {
			super(BeaconMenu.this);
			setTitle(ChatUtil.generateGradient("Pocket Beacon Fuel", CompChatColor.AQUA, CompChatColor.DARK_BLUE));
			setSize(9);
		}


		@Override
		public ItemStack getItemAt(int slot) {
			PlayerCache cache = PlayerCache.getCache(getViewer());

			if (slot == getCenterSlot()) {

				if (cache.getBeaconFuel() == null) return null;

				return cache.getBeaconFuel().getFuel();
			}
			if (slot > 0 && slot < 9) {
				return BeaconUtil.getColorfulGlass();
			}

			return null;

		}

		protected String[] getInfo() {
			return new String[]{
					"&7&lCoal&r&7:&o Burns slow, &e&l+&r&e&o" + Settings.FuelTypes.COALMULTIPLIER,
					"&f&lIron&r&7:&o Burns slow, &e&l+&r&e&o" + Settings.FuelTypes.IRONMULTIPLIER,
					"&e&lGold&r&7:&o Burns fast, &e&l+&r&e&o" + Settings.FuelTypes.GOLDMULTIPLIER + " &7to effects < tier 2",
					"&b&lDiamond&r&7:&o Burns slow, &e&l+&r&e&o" + Settings.FuelTypes.DIAMONDMULTIPLIER + " &7to effects < tier 3",
					"&a&lEmerald&r&7:&o Burns extremely fast, &e&l+&r&e&o" + Settings.FuelTypes.EMERALDMULTIPLIER + " &7to effects <= tier 3"
			};
		}


		@Override
		protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor, InventoryAction action) {
			return this.isActionAllowed(location,slot,clicked,cursor);
		}

		protected boolean isActionAllowed(MenuClickLocation location, int slot, ItemStack clicked, ItemStack cursor) {
			//allows the player to click on the center slot and or fuel in there inventory
			if (location.equals(MenuClickLocation.MENU)) {
				return slot == getCenterSlot();
			}

			return location == MenuClickLocation.PLAYER_INVENTORY && (clicked == null || BeaconFuel.isFuel(clicked));
		}

		@Override
		protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {

			//need to stop runnable when clicked on center slot since the player may be removing fuel
			if (slot == getCenterSlot() && BeaconFuel.isFuel(clicked)) {
				BeaconTaskManager.getInstance().stop(player);
			}

		}

		//todo save and resume fuel tick time
		@Override
		protected void onMenuClose(Player player, Inventory inventory) {
			super.onMenuClose(player, inventory);
			PlayerCache cache = PlayerCache.getCache(player);


			//is the fuel empty? if so set the players fuel to null
			if (inventory.getItem(getCenterSlot()) == null) {
				if (cache.getBeaconFuel() != null) {
					cache.setBeaconFuel(null);
				}
			}

			//fuel is not null? begin the runnable that ticks the fuel.
			if (inventory.getItem(getCenterSlot()) != null) {

				cache.setBeaconFuel(new BeaconFuel(inventory.getItem(getCenterSlot())));

				if (!cache.getBeaconFuel().isBurning() && !cache.getBeaconFuel().empty()) {
					cache.getBeaconFuel().setBurning(true);


					BeaconTaskManager.getInstance().start(player, new BukkitRunnable() {
						final BeaconFuel currentFuel = cache.getBeaconFuel();

						//runnable runs every 5 ticks which is 1/4 of a second
						//So I am multiplying by a correction factor of 1.2
						final int burnTime = (int) Math.round(BeaconFuel.getBurnTime(currentFuel) * 0.2);

						int amountLeft = cache.getBeaconFuel().getFuel().getAmount();
						int timer = 0;

						@Override
						public void run() {
							if(cache.getCurrentState() == null){
							return;
							}
							if (cache.getCurrentState().equals(BeaconState.NO_EFFECT) || !BeaconUtil.isHolding(player)) {
								return;
							}

							//checks when an item should be burned
							if (timer >= burnTime) {
								amountLeft--;
								currentFuel.setAmount(amountLeft);

								if (cache.getBeaconFuel() != null) {
									cache.getBeaconFuel().setFuel(currentFuel.getFuel());
								}

								timer = 0;
								new SimpleSound(Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5, 1).play(player);
								return;
							}


							//handles logic when player is viewing the ticking inventory
							if (isViewing(player)) {
								ItemStack cursorItem = getViewer().getItemOnCursor();

								if (BeaconFuel.isFuel(cursorItem)) {
									BeaconTaskManager.getInstance().stop(player);
									return;
								}
								restartMenu();
							}

							//stops the fuel ticking when the fuel is empty
							if (currentFuel.empty() || (cache.getBeaconFuel() != null && !cache.getBeaconFuel().isBurning())) {
								setItem(getCenterSlot(), null);
								new SimpleSound(Sound.BLOCK_BEACON_DEACTIVATE, 10, 1).play(player);
								BeaconTaskManager.getInstance().stop(player);
								return;
							}
							timer++;
						}

						public void cancel() {
							cache.getBeaconFuel().setBurning(false);
							cache.setCurrentState(BeaconState.NO_EFFECT);
							super.cancel();

						}

					});
				}
			}

			cache.saveData();

		}
	}


}



