package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.FactionMethods;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Guardian extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    FactionMethods factionMethods = new FactionMethods();

    String armorType = "Diamond";
    String ability1Name = "Link";
    String ability2Name = "Resolve";
    String ability3Name = "Courage";

    public Material scroll_item = Material.IRON_INGOT;
    public Material offhand_item = Material.SHIELD;

    final int maxAbility = 4;

    final int ability1CD = 5;
    final int ability2CD = 60;
    final int ability3CD = 60;
    final int abilityRange = 12;
    final int ability2Duration = 400;
    final int ability3Duration = 100;
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

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 2F, 1F);
                }
            }
        }
    }

    public void ability1(Player player, Player player2) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability1Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        boolean hasItem = (playerUser.hasItemInHand(player, offhand_item));
        if (hasItem == true) {
            if (!plugin.cooldowns.containsKey(ability1UUID)) {
                plugin.masterCD = ability1CD;
                plugin.cooldowns.put(ability1UUID, plugin.masterCD);
                player.sendMessage(abilityUse);

                HashMap<UUID, Double> HP = new HashMap<>();

                ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
                for (Entity entity : List) {
                    Player otherPlayer = (Player) entity;

                    if (factionMethods.isSameFaction(player, otherPlayer)) {
                        PlayerUser otherPlayerUser = Main.players.get(player2);
                        playerUser.setLinkedPlayer(player2);
                        otherPlayerUser.setLinkedPlayer(player);
                        player.sendMessage(plugin.successColor + "You linked with: " + plugin.allyColor + player2.getName());
                        player2.sendMessage(plugin.successColor + player.getName() +" has linked with you!");
                        return;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(plugin.errorColor + ability1Name + plugin.abilityNameColor + "requires a shield in your offhand!");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability2Name + "!";
        PlayerUser playerUser = Main.players.get(player);
        Player linkedPlayer = playerUser.getLinkedPlayer();

        if (linkedPlayer != null) {
            if (!plugin.cooldowns.containsKey(ability2UUID)) {
                plugin.masterCD = ability2CD;
                plugin.cooldowns.put(ability2UUID, plugin.masterCD);
                player.sendMessage(abilityUse);

                //4 hearts per amplifier
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, ability2Duration, 3));
                linkedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, ability2Duration, 3));

                linkedPlayer.sendMessage(plugin.successColor+"Your guardian blessed you with additional health!");
            } else {
                player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(plugin.errorColor + "You must link with a player!");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = plugin.abilityUseColor + "You have used " + plugin.abilityNameColor + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);
        Player linkedPlayer = playerUser.getLinkedPlayer();

        if (linkedPlayer != null) {
            if (!plugin.cooldowns.containsKey(ability3UUID)) {
                plugin.masterCD = ability3CD;
                plugin.cooldowns.put(ability3UUID, plugin.masterCD);
                player.sendMessage(abilityUse);

                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ability3Duration, 4));
                linkedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ability3Duration, 4));

                Location linkedPos = linkedPlayer.getLocation();
                player.teleport(linkedPos);
                player.sendMessage(plugin.successColor + "You teleported to your link!");

            } else {
                player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
            }
        } else {
            player.sendMessage(plugin.errorColor + "You must link with a player!");
        }
    }


}