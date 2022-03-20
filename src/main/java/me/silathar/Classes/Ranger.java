package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Ranger extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    String armorType = "Leather";
    String ability1Name = "Leap";
    String ability2Name = "Temposhot";
    String ability3Name = "Pestilent Arrow";

    Material scroll_item = Material.FEATHER;
    Material ability1_material = Material.SUGAR;    //Healing
    Material ability2_material = Material.SUGAR;    //Injection
    Material ability3_material = Material.REDSTONE; //Lethal Tempo

    private int currentAbility = 1;
    private int maxAbility = 4;

    private int ability1CD = 3;
    private int ability2CD = 3;
    private int ability3CD = 3;
    private String ReadiedAbility = "None";
    String className = this.getClass().getSimpleName();

    public Integer returnAbility(Player player) {
        return currentAbility;
    }
    public String returnReadiedAbility(Player player) {
        return ReadiedAbility;
    }

    public void fireAbility(Player player) {
        boolean hasItem            = (methods.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor  = (methods.isWearingArmorType(player, armorType));

        if (hasItem) {
            if (wearingRightArmor) {
                if (currentAbility == 1) {
                    boolean hasEnoughResources = (methods.hasResourceType(player, ability1_material, 2));

                    if (hasEnoughResources) {
                        ability1(player);
                        methods.setResourceAmount(player, ability1_material, 2);
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough resources!");
                    }
                } else if (currentAbility == 2) {
                    boolean hasEnoughResources = (methods.hasResourceType(player, ability2_material, 2));

                    if (hasEnoughResources) {
                        ability2(player);
                        methods.setResourceAmount(player, ability2_material, 2);
                    }
                } else if (currentAbility == 3) {
                    boolean hasEnoughResources = (methods.hasResourceType(player, ability3_material, 2));

                    if (hasEnoughResources) {
                        ability3(player);
                        methods.setResourceAmount(player, ability3_material, 2);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not wearing the right armor1");
            }
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
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability1Name + ChatColor.AQUA +" | 1");
                } else if (currentAbility == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name + ChatColor.AQUA +" | 2");
                } else if (currentAbility == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name + ChatColor.AQUA +" | 3");
                }
            }
        }
    }

    public void ability1(Player player) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = ChatColor.AQUA + "You have used " + ChatColor.RED + ChatColor.BOLD + ability1Name+"!";

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(ChatColor.AQUA + ability1Name + "!");


        } else {
            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = ChatColor.AQUA + "You have used " + ChatColor.RED + ChatColor.BOLD + ability2Name+"!";

        if (!plugin.cooldowns.containsKey(ability2UUID)) {
            plugin.masterCD = ability2CD;
            plugin.cooldowns.put(ability2UUID, plugin.masterCD);
            player.sendMessage(ChatColor.AQUA + ability2Name + "!");

        } else {
            player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.AQUA + "You have used " + ChatColor.RED + ChatColor.BOLD + ability3Name+"!";

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(ChatColor.AQUA + ability3Name + "!");

            double baseAttackSpeed = 4.0;
            double modifiedAttackSpeed = 8.0;
            int seconds = 4;

            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;

                    player.sendMessage("" + modifiedAttackSpeed);
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