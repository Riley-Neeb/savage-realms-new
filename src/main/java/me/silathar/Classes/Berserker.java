package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
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

public class Berserker extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    String armorType = "Berserker";
    String ability1Name = "Headbutt";
    String ability2Name = "Stunning Shout";
    String ability3Name = "Berserker's Rage";

    Material scroll_item = Material.ROTTEN_FLESH;
    Material ability1_material = Material.PAPER;    //Healing
    Material ability2_material = Material.SUGAR;    //Injection
    Material ability3_material = Material.REDSTONE; //Lethal Tempo

    private int currentAbility = 1;
    private int maxAbility = 4;

    private int ability1CD = 10;
    private int ability2CD = 30;
    private int ability3CD = 30;
    private String ReadiedAbility = "None";
    String className = this.getClass().getSimpleName();

    public Integer returnAbility(Player player) {
        return currentAbility;
    }
    public String returnReadiedAbility(Player player) {
        return ReadiedAbility;
    }

    public void fireAbility(Player player) {
        boolean hasItem = (methods.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor = (methods.isWearingArmorType(player, armorType));

        if (hasItem) {
            if (wearingRightArmor) {
                if (currentAbility == 1) {
                    return; //Because it's a hit ability, call the ability1() etc, instead.
                } else if (currentAbility == 2) {
                    ability2(player);
                } else if (currentAbility == 3) {
                    ability3(player);
                }
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
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability1Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    ReadiedAbility = "Headbutt";

                } else if (currentAbility == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    ReadiedAbility = "None";

                } else if (currentAbility == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    ReadiedAbility = "None";
                }
            }
        }
    }

    public void ability1(Player player, Player player2) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
            String abilityUse = ChatColor.AQUA + "You have used " + ChatColor.RED + ChatColor.BOLD + ability1Name+"!";

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            for (double z = 0; z <= 5; z += 0.01) {
                double adjustX = 3 * Math.cos(z);
                double adjustY = 3 * Math.sin(z);

                player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), (int) adjustX, (int) adjustY, (int) z, 0, 0);
                player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), (int) adjustX, (int) adjustY, (int) z, 0, 0);
            }

            Vector dir = player.getEyeLocation().getDirection();
            Vector vec = new Vector(dir.getX() * 1.8D, 0.2D, dir.getZ() * 1.8D);

            Vector dir2 = player.getEyeLocation().getDirection();
            Vector vec2 = new Vector(dir2.getX() * 1.8D, 0D, dir2.getZ() * 1.8D);


            player2.setVelocity(vec);
            player2.damage(4.0);
            player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 100));
            player.setVelocity(vec);
            player.setVelocity(vec2);

            player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 40, 2)));
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
            player.sendMessage(abilityUse);

            ArrayList<Entity> List = methods.getNearbyPlayers(player, 12);

            for (Entity entity : List) {
                Player otherPlayer = (Player) entity;

                if (methods.isSameParty(player, otherPlayer)) {

                } else {
                    player.sendMessage(ChatColor.GOLD + "You casted shout on " + ChatColor.GOLD + otherPlayer.getName());
                    otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 100));
                }
            }

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
            player.sendMessage(abilityUse);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 4));
        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }


}