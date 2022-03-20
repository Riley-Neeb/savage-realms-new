package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

public class None extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    String armorType = "Any";
    String ability1Name = "Ability 1";
    String ability2Name = "Ability 2";
    String ability3Name = "Ability 3";

    Material scroll_item = Material.WOODEN_SWORD;
    Material ability1_material = Material.PAPER; //Ability1
    Material ability2_material = Material.PAPER; //Ability2
    Material ability3_material = Material.PAPER; //Ability3

    private int currentAbility = 1;
    private int maxAbility = 4;
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

    public void scrollAbility(Player player) {
        if (methods.hasItemInHand(player, scroll_item)) {
            if (currentAbility < maxAbility) {
                currentAbility += 1;

                if (currentAbility == maxAbility) {
                    currentAbility = 1;
                }

                if (currentAbility == 1) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability1Name);
                } else if (currentAbility == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                } else if (currentAbility == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                }
            }
        }
    }

    public void ability1(Player player) {
        if (methods.hasItemInHand(player, scroll_item)) {
            String ability1UUID = className + ability1Name + player.getUniqueId();

            if (methods.isWearingArmorType(player, armorType)) {
                if (!plugin.cooldowns.containsKey(ability1UUID)) {
                    plugin.masterCD = ability1CD;
                    plugin.cooldowns.put(ability1UUID, plugin.masterCD);

                    player.sendMessage(ChatColor.AQUA + ability1Name + "!");

                } else {
                    player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
            }
        }
    }

    public void ability2(Player player) {
        if (methods.hasItemInHand(player, scroll_item)) {
            String ability2UUID = className + ability2Name + player.getUniqueId();

            if (methods.isWearingArmorType(player, armorType)) {
                if (!plugin.cooldowns.containsKey(ability2UUID)) {
                    plugin.masterCD = ability2CD;
                    plugin.cooldowns.put(ability2UUID, plugin.masterCD);

                    player.sendMessage(ChatColor.AQUA + ability2Name + "!");
                } else {
                    player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
            }
        }
    }

    public void ability3(Player player) {
        if (methods.hasItemInHand(player, scroll_item)) {
            String ability3UUID = className + ability3Name + player.getUniqueId();

            if (methods.isWearingArmorType(player, armorType)) {
                if (!plugin.cooldowns.containsKey(ability3UUID)) {
                    plugin.masterCD = ability3CD;
                    plugin.cooldowns.put(ability3UUID, plugin.masterCD);

                    player.sendMessage(ChatColor.AQUA + ability3Name + "!");

                } else {
                    player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
            }
        }
    }
}
