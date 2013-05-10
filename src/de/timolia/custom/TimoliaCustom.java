/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.timolia.custom.cmds.TCommand;
import de.timolia.custom.cmds.checkent;
import de.timolia.custom.cmds.google;
import de.timolia.custom.cmds.itp;
import de.timolia.custom.cmds.protnpc;
import de.timolia.custom.cmds.sapopvp;
import de.timolia.custom.cmds.sgames;
import de.timolia.custom.cmds.sgcopy;
import de.timolia.custom.cmds.speed;
import de.timolia.custom.cmds.tcustom;
import de.timolia.custom.cmds.tpack;
import de.timolia.custom.cmds.west;
import de.timolia.custom.events.EntityListener;
import de.timolia.custom.events.InventoryListener;
import de.timolia.custom.events.PlayerListener;
import de.timolia.custom.events.ProjectileListener;

public class TimoliaCustom extends JavaPlugin {

    public static final String PREFIX = ChatColor.GREEN + "[TCustom] " + ChatColor.WHITE;
    public static File dataFolder;
    public static boolean coding = false;

    public void onEnable() {
        initCommands();
        initEventHandlers();
        initConfig();
        initRecipes();
        dataFolder = getDataFolder();

        sapopvp.updateArena();
        protnpc.load();
        west.loadWatchedPlayers();
        west.repeatScanning();
    }

    public void onDisable() {
        west.saveWatchedPlayers();
        west.stopScanning();
        protnpc.save();
    }

    private void initCommands() {
        TCommand.setPluginInstance(this);
        TCommand.add("checkent", new checkent());
        TCommand.add("google", new google());
        TCommand.add("itp", new itp());
        TCommand.add("protnpc", new protnpc());
        TCommand.add("sapopvp", new sapopvp());
        TCommand.add("sgames", new sgames());
        TCommand.add("sgcopy", new sgcopy());
        TCommand.add("speed", new speed());
        TCommand.add("tcustom", new tcustom());
        TCommand.add("tpack", new tpack());
        TCommand.add("west", new west());
    }

    private void initEventHandlers() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(), this);
    }

    private void initConfig() {
        final FileConfiguration conf = getConfig();
        conf.addDefault("sapopvp.pos1", "");
        conf.addDefault("sapopvp.pos2", "");
        conf.addDefault("sapopvp.warpgold", "");
        conf.addDefault("sapopvp.warpeisen", "");
        conf.addDefault("westwatchwelt", "timolia");
        conf.addDefault("sgamesdeathmsg", true);
        conf.options().copyDefaults(true);
        saveConfig();
    }

    private void initRecipes() {
        // giftpfeile
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Giftpfeil");
        item.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(item);
        recipe.addIngredient(Material.ARROW);
        recipe.addIngredient(Material.POISONOUS_POTATO);
        Bukkit.addRecipe(recipe);

        // pocket-workbench
        ItemStack item2 = new ItemStack(Material.WORKBENCH, 1);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(ChatColor.RESET + "Pocket Workbench");
        item.setItemMeta(meta2);
        ShapelessRecipe recipe2 = new ShapelessRecipe(item2);
        recipe2.addIngredient(Material.WORKBENCH);
        recipe2.addIngredient(Material.STICK);
        Bukkit.addRecipe(recipe2);

        // double steps
        ItemStack item3 = new ItemStack(Material.DOUBLE_STEP, 1, (byte) 0);
        ShapedRecipe recipe3 = new ShapedRecipe(item3);
        recipe3.shape("A", "B", "A");
        recipe3.setIngredient('A', new MaterialData(Material.STEP, (byte) 0));
        Bukkit.addRecipe(recipe3);

        for (int i = 8; i <= 9; i++) {
            ItemStack itemI = new ItemStack(Material.DOUBLE_STEP, 1, (byte) i);
            ShapedRecipe recipeI = new ShapedRecipe(itemI);
            recipeI.shape("AA");
            recipeI.setIngredient('A', new MaterialData(Material.STEP, (byte) (i - 8)));
            Bukkit.addRecipe(recipeI);
        }
    }

    public static WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public static void logError(final String err) {
        try {
            final File file = new File(TimoliaCustom.dataFolder + File.separator + "errors.txt");
            final BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            final String date = new SimpleDateFormat("dd. MMM. yyyy, HH:mm").format(System.currentTimeMillis());
            output.append(date + "\t" + err + "\n");
            output.close();
            Message.console("Error logged!");
        } catch (final Exception e) {

        }
    }

}