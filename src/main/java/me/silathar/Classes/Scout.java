package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.FactionMethods;
import me.silathar.Modules.Methods;
import me.silathar.Modules.PlayerUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Scout extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    FactionMethods factionMethods = new FactionMethods();

    String armorType = "Scout";
    String ability1Name = "Bat to The Face!";
    String ability2Name = "Power Slide";
    String ability3Name = "A Lovely Feeling...";

    Material scroll_item = Material.STICK;

    final int maxAbility = 4;

    final int ability1CD = 15;
    final int ability2CD = 15;
    final int ability3CD = 40;
    final int abilityDuration = 100;
    final int abilityRange = 12;
    String className = this.getClass().getSimpleName();

    public void fireAbility(Player player) {
        PlayerUser playerUser = Main.players.get(player);
        boolean hasItem = (playerUser.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor = (playerUser.isWearingArmorType(player, armorType));

        if (hasItem) {
            if (wearingRightArmor) {
                if (playerUser.getCurrentAbility() == 1) {
                    return;
                } else if (playerUser.getCurrentAbility() == 2) {
                    ability2(player);
                } else if (playerUser.getCurrentAbility() == 3) {
                    ability3(player);
                }
            } else {
                player.sendMessage(ChatColor.RED+"You're not wearing the right armor!");
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
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 2F, 1F);
                    playerUser.setReadiedAbility(ability1Name);

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 2F, 1F);
                    playerUser.setReadiedAbility("None");

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 2F, 1F);
                    playerUser.setReadiedAbility("None");
                }
            }
        }
    }

    public void ability1(Player player, Player player2) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability1Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            player2.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
            player.setFoodLevel(player.getFoodLevel()-1);
            player2.setFoodLevel(player.getFoodLevel()-2);
        } else {
            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability2UUID)) {
            plugin.masterCD = ability2CD;
            plugin.cooldowns.put(ability2UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 300, 1));

            Vector unitVector = new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ());
            unitVector = unitVector.normalize();
            player.setVelocity(unitVector.multiply(4));
            player.setFoodLevel(player.getFoodLevel()-1);

            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2F, 1F);
        } else {
            player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, abilityDuration, 5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 5));
            player.setFoodLevel(player.getFoodLevel()-1);
            player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }


}