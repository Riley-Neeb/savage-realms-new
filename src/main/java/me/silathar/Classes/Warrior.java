package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Methods;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Warrior implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    Methods methods;

    String armorType = "Iron";
    String ability1Name = "Ability 1";
    String ability2Name = "Ability 2";
    String ability3Name = "Ability 3";

    private int currentAbility = 1;
    private int maxAbility = 3;
    private String selectedAbility = "";

    private int ability1CD = 3;
    public boolean isAbility1ASpell = false; //If the ability can be used without hitting somebody.

    private int ability2CD = 3;
    public boolean isAbility2ASpell = false; //If the ability can be used without hitting somebody.

    private int ability3CD = 3;
    public boolean isAbility3ASpell = false; //If the ability can be used without hitting somebody.
    String className = this.getClass().getSimpleName();

    public Integer returnAbility(Player player) {
        return currentAbility;
    }

    public void fireAbility(Player player) {
        if (currentAbility == 1) {
            ability1(player);
        } else if (currentAbility == 2) {
            ability2(player);
        } else if (currentAbility == 3) {
            ability3(player);
        }
    }

    public void ability1(Player player) {
        String ability1UUID = className + " Ability1 " + player.getUniqueId();

        if (methods.isWearingArmorType(player, armorType)) {
            if (!plugin.cooldowns.containsKey(ability1UUID)) {
                plugin.masterCD = ability1CD;
                plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            } else {
                player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + " Ability2" + player.getUniqueId();

        if (methods.isWearingArmorType(player, armorType)) {
            if (!plugin.cooldowns.containsKey(ability2UUID)) {
                plugin.masterCD = ability2CD;
                plugin.cooldowns.put(ability2UUID, plugin.masterCD);
            } else {
                player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + " Ability3" + player.getUniqueId();

        if (methods.isWearingArmorType(player, armorType)) {
            if (!plugin.cooldowns.containsKey(ability3UUID)) {
                plugin.masterCD = ability3CD;
                plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            } else {
                player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
        }
    }


}
