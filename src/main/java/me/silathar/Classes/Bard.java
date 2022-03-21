package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.PlayerUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Bard extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    String armorType = "Iron";
    String ability1Name = "Hymn of Strength";
    String ability2Name = "Hymn of Speed";
    String ability3Name = "Hymn of Regeneration";
    String ability4Name = "Hymn of Absorption";
    String ability5Name = "Hymn of Resistances";

    Material scroll_item = Material.NOTE_BLOCK;

    final int maxAbility = 6;

    final int ability1CD = 20;
    final int ability2CD = 20;
    final int ability3CD = 20;
    final int ability4CD = 20;
    final int ability5CD = 20;
    final int abilityRange = 20;
    final int abilityDuration = 820;
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
                    } else if (playerUser.getCurrentAbility() == 4) {
                        ability4(player);
                    } else if (playerUser.getCurrentAbility() == 5) {
                        ability5(player);
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
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 4) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability4Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2F, 1F);

                } else if (playerUser.getCurrentAbility() == 5) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability5Name);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2F, 1F);

                }
            }
        }
    }

    public void ability1(Player player) {
        String ability1UUID = className + "Hymn" + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability1Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, abilityDuration, 1));

            ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (playerUser.isSameParty(player, otherPlayer)) {
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 820, 1));
                    otherPlayer.sendMessage(plugin.allyColor + player.getName() + plugin.abilityUseColor + " blessed you with damage!");
                    player.sendMessage(plugin.abilityUseColor + "You've blessed " + plugin.allyColor + otherPlayer.getName() + plugin.abilityUseColor + " with damage!");
                } else {
                   return;
                }
            }

        } else {
            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + "Hymn" + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability2UUID)) {
            plugin.masterCD = ability2CD;
            plugin.cooldowns.put(ability2UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, abilityDuration, 1));

            ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (playerUser.isSameParty(player, otherPlayer)) {
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 820, 2));
                    otherPlayer.sendMessage(plugin.allyColor + player.getName() + plugin.abilityUseColor + " blessed you with speed!");
                    player.sendMessage(plugin.abilityUseColor + "You've blessed " + plugin.allyColor + otherPlayer.getName() + plugin.abilityUseColor + " with speed!");
                } else {
                    return;
                }
            }

        } else {
            player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + "Hymn" + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, abilityDuration, 1));

            ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (playerUser.isSameParty(player, otherPlayer)) {
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, abilityDuration, 1));
                    otherPlayer.sendMessage(plugin.allyColor + player.getName() + plugin.abilityUseColor + " blessed you with regeneration!");
                    player.sendMessage(plugin.abilityUseColor + "You've blessed " + plugin.allyColor + otherPlayer.getName() + plugin.abilityUseColor + " with regeneration!");
                } else {
                    return;
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability4(Player player) {
        String ability4UUID = className + "Hymn" + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability4UUID)) {
            plugin.masterCD = ability4CD;
            plugin.cooldowns.put(ability4UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, abilityDuration, 1));

            ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (playerUser.isSameParty(player, otherPlayer)) {
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, abilityDuration, 1));
                    otherPlayer.sendMessage(plugin.allyColor + player.getName() + plugin.abilityUseColor + " blessed you with absorption!");
                    player.sendMessage(plugin.abilityUseColor + "You've blessed " + plugin.allyColor + otherPlayer.getName() + plugin.abilityUseColor + " with absorption!");
                } else {
                    return;
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability4UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability5(Player player) {
        String ability5UUID = className + "Hymn" + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability5UUID)) {
            plugin.masterCD = ability5CD;
            plugin.cooldowns.put(ability5UUID, plugin.masterCD);
            player.sendMessage(abilityUse);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, abilityDuration, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, abilityDuration, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, abilityDuration, 1));

            ArrayList<Entity> List = playerUser.getNearbyPlayers(player, abilityRange);
            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (playerUser.isSameParty(player, otherPlayer)) {
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 820, 1));
                    otherPlayer.sendMessage(plugin.allyColor + player.getName() + plugin.abilityUseColor + " blessed you with resistances!");
                    player.sendMessage(plugin.abilityUseColor + "You've blessed " + plugin.allyColor + otherPlayer.getName() + plugin.abilityUseColor + " with resistances!");
                } else {
                    return;
                }
            }

        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability5UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }
}
