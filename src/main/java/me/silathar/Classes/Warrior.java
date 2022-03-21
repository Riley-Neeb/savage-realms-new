package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;

import me.silathar.Modules.PlayerUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Warrior extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    public String armorType = "Iron";
    public String ability1Name = "First Aid";
    public String ability2Name = "Combat Injection";
    public String ability3Name = "Lethal Tempo";

    public Material scroll_item = Material.FLINT;
    public Material ability1_material = Material.PAPER;
    public Material ability2_material = Material.REDSTONE;
    public Material ability3_material = Material.SUGAR;

    public int maxAbility = 4;
    public int ability1CD = 8;
    public int ability2CD = 25;
    public int ability3CD = 20;

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
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability1Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.hasResourceType(player, ability1_material, 2)) {
            if (!plugin.cooldowns.containsKey(ability1UUID)) {
                plugin.masterCD = ability1CD;
                plugin.cooldowns.put(ability1UUID, plugin.masterCD);
                player.sendMessage(abilityUse);
                playerUser.setResourceAmount(player, ability1_material, 2);

                player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 60, 2)));
            } else {
                player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
            }
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.hasResourceType(player, ability2_material, 2)) {
            if (!plugin.cooldowns.containsKey(ability2UUID)) {
                plugin.masterCD = ability2CD;
                plugin.cooldowns.put(ability2UUID, plugin.masterCD);
                player.sendMessage(abilityUse);
                playerUser.setResourceAmount(player, ability2_material, 2);

                player.addPotionEffect((new PotionEffect(PotionEffectType.ABSORPTION, 100, 3)));
                player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 100, 1)));
                player.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 100, 0)));
            } else {
                player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
            }
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.hasResourceType(player, ability3_material, 2)) {
            if (!plugin.cooldowns.containsKey(ability3UUID)) {
                plugin.masterCD = ability3CD;
                plugin.cooldowns.put(ability3UUID, plugin.masterCD);
                player.sendMessage(abilityUse);
                playerUser.setResourceAmount(player, ability3_material, 2);

                double baseAttackSpeed = 4.0;
                double modifiedAttackSpeed = 20.0;
                int seconds = 7;

                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        i++;

                        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(modifiedAttackSpeed);

                        if (i == seconds) {
                            this.cancel();
                            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(baseAttackSpeed);
                        }
                    }

                }.runTaskTimer(plugin, 0, 20);

            } else {
                player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
            }
        }
    }

}