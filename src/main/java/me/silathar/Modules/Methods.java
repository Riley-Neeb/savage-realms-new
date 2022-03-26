package me.silathar.Modules;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.None;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Methods implements Listener {

    private Main plugin = Main.getPlugin(Main.class);


    //Config
    public void resetConfig(Player player) {
        PlayerUser playerUser = Main.players.get(player);

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nResetting "+player.getName()+"'s config");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Name", player.getName());
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Class" , "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Slowed", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isLeaping", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Ability", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".currentAbility", 1);
        plugin.saveConfig();

        playerUser.setClass(new None());
    }
    public void setConfig(Player player) {
        PlayerUser playerUser = Main.players.get(player);

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSetting "+player.getName()+"'s config");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Setting " + player.getName() + "'s config");
        plugin.getConfig().set("Users." + player.getUniqueId(), player);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Name", player.getName());
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Class" , "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Kills" , 0);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Deaths" , 0);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Slowed", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isLeaping", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Ability", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".currentAbility", 1);
        plugin.saveConfig();

        playerUser.setClass(new None());
    }
    public String setConfigValue(Player player, String nameOfValue, Object value) {
        if (value instanceof Integer) {
            int checkedValue = (int) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, value);
        } else if (value instanceof Boolean) {
            boolean checkedValue = (boolean) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, checkedValue);
        } else if (value instanceof String) {
            String checkedValue = (String) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, checkedValue);
        }

        return "None";
    }

}
