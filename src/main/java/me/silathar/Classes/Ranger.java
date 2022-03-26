package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.Classes;
import me.silathar.Modules.Methods;
import me.silathar.Modules.PlayerUser;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Ranger extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    String armorType = "Leather";
    String ability1Name = "Leap";
    String ability2Name = "Quick Dash";
    String ability3Name = "Multi Shot";

    Material scroll_item = Material.FEATHER;

    private int maxAbility = 4;

    private int ability1CD = 15;
    private int ability2CD = 2;
    private int ability3CD = 7;

    String className = this.getClass().getSimpleName();

    public void fireAbility(Player player) {
        PlayerUser playerUser = Main.players.get(player);
        boolean hasItem = (playerUser.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor = (playerUser.isWearingArmorType(player, armorType));

        if (hasItem) {
            if (wearingRightArmor) {
                if (playerUser.getCurrentAbility() == 1) {
                    ability1(player);
                } else if (playerUser.getCurrentAbility() == 2) {
                    ability2(player);
                } else if (playerUser.getCurrentAbility() == 3) {
                    return;
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
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    playerUser.setReadiedAbility("None");

                } else if (playerUser.getCurrentAbility() == 2) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability2Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    playerUser.setReadiedAbility("None");

                } else if (playerUser.getCurrentAbility() == 3) {
                    player.sendMessage(ChatColor.GREEN + "You ready your " + ChatColor.YELLOW + ability3Name);
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 2F, 1F);
                    playerUser.setReadiedAbility("Multi Shot");
                }
            }
        }
    }

    public void ability1(Player player) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability1Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            Vector dir = player.getEyeLocation().getDirection();
            Vector vec = new Vector(dir.getX() * 1.8D, 1D, dir.getZ() * 1.6D);

            player.setVelocity(vec);
            playerUser.setLeaping(player, true);
        } else {
            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability2UUID)) {
            plugin.masterCD = ability2CD;
            plugin.cooldowns.put(ability2UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            Vector dir = player.getEyeLocation().getDirection();
            Vector vec = new Vector(dir.getX() * 1D, 0D, dir.getZ() * 1.8D);
            player.setVelocity(vec);

        } else {
            player.sendMessage(ChatColor.RED + ability2Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        if (!plugin.cooldowns.containsKey(ability3UUID)) {
            plugin.masterCD = ability3CD;
            plugin.cooldowns.put(ability3UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            World world = player.getWorld();
            Location loc = player.getLocation();

            Vector lookDir = player.getEyeLocation().getDirection();

            Float drawStrength = (float) playerUser.getArrowSpeed();

            Projectile arrow1 = world.spawnArrow(loc.clone().add(1,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow2 = world.spawnArrow(loc.clone().add(0.75,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow3 = world.spawnArrow(loc.clone().add(0.5,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow4 = world.spawnArrow(loc.clone().add(0.25,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow5 = world.spawnArrow(loc.clone().add(0,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow6 = world.spawnArrow(loc.clone().add(-0.25,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow7 = world.spawnArrow(loc.clone().add(-0.5,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow8 = world.spawnArrow(loc.clone().add(-0.75,1.5,0), lookDir, drawStrength, 0);
            Projectile arrow9 = world.spawnArrow(loc.clone().add(-1,1.5,0), lookDir, drawStrength, 0);

            playerUser.setResourceAmount(player, Material.ARROW, 9);


        } else {
            player.sendMessage(ChatColor.RED + ability3Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability3UUID) + " seconds" + ChatColor.RED + " left.");
        }
    }

}