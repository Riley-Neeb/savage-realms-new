package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
import me.silathar.Modules.PlayerUser;
import org.bukkit.*;
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

public class Assassin extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    String armorType = "Leather";
    String ability1Name = "Ankle Cutter";
    String ability2Name = "Flicker";
    String ability3Name = "Shadowvoid";

    Material scroll_item = Material.GHAST_TEAR;

    private int currentAbility = 1;
    private int maxAbility = 4;

    private int ability1CD = 5;
    private int ability2CD = 15;
    private int ability3CD = 40;
    final int maxActiveAbilities = 3;

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
                    return;
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
                    player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 2F, 1F);
                    playerUser.setReadiedAbility(ability1Name);

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 2F, 1F);
                    playerUser.setReadiedAbility(ability2Name);

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 2F, 1F);
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

            playerUser.vanishPlayer(player, 1);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 2));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2));
        } else {
            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }



    public void ability2(Player player, Player player2) {
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);
        int currentActiveAbilities = playerUser.getCurrentActiveAbilities();

        if (currentActiveAbilities < maxActiveAbilities) {
            player.sendMessage(abilityUse);
            playerUser.addActiveAbility(ability2CD);

            double nX;
            double nZ;
            float nang = player2.getLocation().getYaw() + 90;
            if (nang < 0) nang += 360;
            nX = Math.cos(Math.toRadians(nang));
            nZ = Math.sin(Math.toRadians(nang));

            Location entityLoc = player2.getLocation();
            Location newDamagerLoc = new Location(entityLoc.getWorld(), entityLoc.getX() - nX,
                    entityLoc.getY(), entityLoc.getZ() - nZ, entityLoc.getYaw(), entityLoc.getPitch());

            player.teleport(newDamagerLoc);
            playerUser.setReadiedAbility("None");
        } else {
            player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + currentActiveAbilities + " charges" + ChatColor.RED + " are still active!");
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

            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
            player.playSound
                    (player.getLocation(), Sound.ENTITY_WITCH_DRINK, 2F, 1F);

            playerUser.vanishPlayer(player, 20);
            playerUser.setVanished(true);
        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");

        }
    }
}