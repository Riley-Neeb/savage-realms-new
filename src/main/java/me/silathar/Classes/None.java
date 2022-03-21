package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
import me.silathar.Modules.PlayerUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class None extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    String armorType = "Any";
    String ability1Name = "Ability 1";
    String ability2Name = "Ability 2";
    String ability3Name = "Ability 3";

    Material scroll_item = Material.WOODEN_SWORD;
    Material ability1_material = Material.PAPER; //Ability1
    Material ability2_material = Material.PAPER; //Ability2
    Material ability3_material = Material.PAPER; //Ability3

    private int maxAbility = 4;

    private int ability1CD = 3;
    private int ability2CD = 3;
    private int ability3CD = 3;

    String className = this.getClass().getSimpleName();

    public void fireAbility(Player player) {
        PlayerUser playerUser = Main.players.get(player);
        boolean hasItem = (playerUser.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor = (playerUser.isWearingArmorType(player, armorType));

        if (hasItem && wearingRightArmor) {
            if (playerUser.hasItemInHand(player, scroll_item)) {
                if (playerUser.isWearingArmorType(player, armorType)) {
                    if (playerUser.getCurrentAbility() == 1) {
                        ability1(player);
                    } else if (playerUser.getCurrentAbility() == 2) {
                        ability2(player);
                    } else if (playerUser.getCurrentAbility() == 3) {
                        ability3(player);
                    }
                }
            }
        }
    }

    public void scrollAbility(Player player) {
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.hasItemInHand(player, scroll_item)) {
            if (playerUser.getCurrentAbility() < maxAbility) {
                playerUser.addCurrentAbility();

                if (playerUser.getCurrentAbility() == maxAbility) {
                    playerUser.setCurrentAbility(1);
                }

                if (playerUser.getCurrentAbility() == 1) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability1Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                }
            }
        }
    }

    public void ability1(Player player) {
        /*
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
         */
    }

    public void ability2(Player player) {
       /*
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
         */
    }

    public void ability3(Player player) {
       /*
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
         */
    }
}
