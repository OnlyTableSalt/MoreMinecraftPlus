package dev.tablesalt.pocketbeacon.beacon;

import dev.tablesalt.pocketbeacon.BeaconPlugin;
import dev.tablesalt.pocketbeacon.PlayerCache;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuQuantitable;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.menu.model.MenuQuantity;
import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompMaterial;

import java.awt.*;
import java.util.Objects;

public class BeaconMenu extends Menu {


    private final BeaconButton clearButton;
    private final BeaconButton jumpButton;
    private final BeaconButton speedButton;
    private final BeaconButton hasteButton;
    private final BeaconButton slowFallingButton;
    private final BeaconButton resistanceButton;

    private final ButtonMenu fuelButton;


    public BeaconMenu() {


        setSize(9 * 3);
        setTitle(ChatUtil.generateGradient("Pocket Beacon", CompChatColor.AQUA, CompChatColor.DARK_BLUE));
        setSlotNumbersVisible();

        clearButton = new BeaconButton(BeaconState.NO_EFFECT,
                ItemCreator.of(CompMaterial.MILK_BUCKET).name("Clear Effect").build().make());

        jumpButton = new BeaconButton(BeaconState.JUMP_BOOST,
                ItemCreator.of(CompMaterial.RABBIT_FOOT).name("Jump Boost").build().make());

        speedButton = new BeaconButton(BeaconState.SPEED,
                ItemCreator.of(CompMaterial.SUGAR).name("Speed").build().make());

        hasteButton = new BeaconButton(BeaconState.HASTE,
                ItemCreator.of(CompMaterial.GOLDEN_PICKAXE).name("Haste").hideTags(true).build().make());

        slowFallingButton = new BeaconButton(BeaconState.SLOW_FALLING,
                ItemCreator.of(CompMaterial.FEATHER).name("Slow Falling").build().make());

        resistanceButton = new BeaconButton(BeaconState.RESISTANCE,
                ItemCreator.of(CompMaterial.SHIELD).name("Resistance").build().make());


        fuelButton = new ButtonMenu(new FuelMenu(), CompMaterial.BLAST_FURNACE,
                "Fuel Menu",
                "Click to Open",
                "The Refueling Menu");


    }


    @Override
    public ItemStack getItemAt(int slot) {
        //effect group one
        if (slot == 2)
            return clearButton.getItem();
        if (slot == 10)
            return jumpButton.getItem();
        if (slot == 11)
            return speedButton.getItem();

        //effect group two
        if (slot == 6)
            return hasteButton.getItem();
        if (slot == 15)
            return slowFallingButton.getItem();
        if (slot == 16)
            return resistanceButton.getItem();

        //filler items
        if (slot == 4 || slot == 5 || slot == 3 || slot == 12 || slot == 14) {
            return ItemCreator.of(CompMaterial.PURPLE_STAINED_GLASS_PANE, "").build().make();
        }

        if (slot == 0 || slot == 1 || slot == 9 || slot == 7 || slot == 8 || slot == 17) {
            return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE, "").build().make();
        }


        //centerpiece
        if (slot == getCenterSlot()) {
            return ItemCreator.of(CompMaterial.BEACON, "&fPocket Beacon").glow(true).build().make();
        }

        //fuel menu
        if (slot == 26) {
            return fuelButton.getItem();
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


    private final class FuelMenu extends Menu implements Listener {

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
                return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE, "").build().make();
            }

            return null;

        }

        protected String[] getInfo() {
            return new String[]{
                    "TODO... ",
                    "Explain Fuel System",
                    "Here"
            };
        }

        @Override
        protected boolean isActionAllowed(MenuClickLocation location, int slot, ItemStack clicked, ItemStack cursor) {

            final ItemStack fuelSlot = getItemAt(getCenterSlot());

            if (slot == getCenterSlot() && fuelSlot != null && BeaconFuel.isFuel(fuelSlot)) {
                //item in center is filled with fuel
                return true;
            } else if (location == MenuClickLocation.PLAYER_INVENTORY && clicked != null && BeaconFuel.isFuel(clicked)) {
                //item in center is not filled with fuel

                return true;
            } else if (!BeaconFuel.isFuel(cursor)) {

                return false;
            } else if (slot == getCenterSlot() && BeaconFuel.isFuel(cursor)) {

                return true;
            } else {
                return location == MenuClickLocation.PLAYER_INVENTORY;
            }
        }

        @Override
        protected void onMenuClose(Player player, Inventory inventory) {
            super.onMenuClose(player, inventory);

            PlayerCache cache = PlayerCache.getCache(player);

            if (inventory.getItem(4) == null) {
                cache.setBeaconFuel(null);
            }
            cache.setBeaconFuel(new BeaconFuel(inventory.getItem(4)));
        }


    }
}


