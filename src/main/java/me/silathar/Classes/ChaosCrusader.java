package me.silathar.Classes;

import me.silathar.Main;
import me.silathar.Modules.*;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Set;

public class ChaosCrusader extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    FactionMethods factionMethods = new FactionMethods();

    public String armorType = "Diamond";
    public String ability1Name = "Chaos Stun";
    public String ability2Name = "Chaos Strike";
    public String ability3Name = "Chaos Switch";

    public Material scroll_item = Material.RAW_GOLD;

    public int maxAbility = 4;
    public int ability1CD = 15;
    public int ability2CD = 20;
    public int ability3CD = 20;

    public int ability1Range = 50;
    public int ability2Range = 100;
    public int ability3Range = 100;
    public int targetBlockDistance = 1;

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
                    ability3(player);
                }
            } else {
                player.sendMessage(ChatColor.RED + "You're not wearing the right armor!");
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
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability1Name + "!";
        PlayerUser playerUser = Main.players.get(player);

        World world = player.getWorld();
        Location position = player.getEyeLocation();
        Vector3D direction = new Vector3D(position.getDirection());
        Vector3D start = new Vector3D(position);
        Vector3D end = start.add(direction.multiply(ability1Range));

        Block targetBlock = player.getTargetBlock((Set<Material>) null, targetBlockDistance);

        ArrayList<Entity> List = playerUser.getNearbyPlayers(player, ability1Range);
        for (Entity entity : List) {
            Player otherPlayer = (Player) entity;
            Vector3D targetPos = new Vector3D(otherPlayer.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (factionMethods.isSameFaction(player, otherPlayer)) {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        if (otherPlayer != player && player.hasLineOfSight(otherPlayer)) {
                            player.sendMessage(ChatColor.RED + "Can't stun a friendly player!");
                        }
                    }
                }
            } else {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        if (!plugin.cooldowns.containsKey(ability1UUID)) {
                            plugin.masterCD = ability1CD;
                            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
                            player.sendMessage(abilityUse);
                            //player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_DEATH, 2F, 1F);

                            player.sendMessage(plugin.abilityUseColor + "You Chaos Stunned " + plugin.stunColor + otherPlayer.getName());
                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 100));
                            playerUser.stunForDuration(otherPlayer, 2);
                            world.playSound(otherPlayer.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 2F, 1F);
                        } else {
                            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability1UUID) + " seconds" + ChatColor.RED + " left.");
                        }
                    }
                }
            }
        }
    }



    public void ability2(Player player) {
        String ability2UUID = className + ability2Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability2Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        World world = player.getWorld();
        Location position = player.getEyeLocation();
        Vector3D direction = new Vector3D(position.getDirection());
        Vector3D start = new Vector3D(position);
        Vector3D end =  start.add(direction.multiply(ability2Range));

        Block targetBlock = player.getTargetBlock((Set<Material>) null, targetBlockDistance);

        ArrayList<Entity> List = playerUser.getNearbyPlayers(player, ability2Range);
        for (Entity entity : List) {
            Player otherPlayer = (Player) entity;
            Vector3D targetPos = new Vector3D(otherPlayer.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (factionMethods.isSameFaction(player, otherPlayer)) {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        if (otherPlayer != player && player.hasLineOfSight(otherPlayer)) {
                            player.sendMessage(ChatColor.RED+"Can't Chaos Strike a friendly player!");
                        }
                    }
                }
            } else {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        if (!plugin.cooldowns.containsKey(ability2UUID)) {
                            plugin.masterCD = ability1CD;
                            plugin.cooldowns.put(ability2UUID, plugin.masterCD);

                            player.teleport(otherPlayer.getLocation());
                            world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 1F);

                            player.sendMessage(plugin.abilityUseColor + "You Chaos Striked " + plugin.stunColor + otherPlayer.getName());
                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 100));
                            otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0));
                            otherPlayer.setHealth(otherPlayer.getHealth()-4);
                        } else {
                            player.sendMessage(ChatColor.RED + ability1Name + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(ability2UUID) + " seconds" + ChatColor.RED + " left.");
                        }
                    }
                }
            }
        }
    }

    public void ability3(Player player) {
        String ability3UUID = className + ability3Name + player.getUniqueId();
        String abilityUse = ChatColor.GOLD + "You have used " + ChatColor.BLUE + ChatColor.BOLD + ability3Name+"!";
        PlayerUser playerUser = Main.players.get(player);

        World world = player.getWorld();
        Location position = player.getEyeLocation();
        Vector3D direction = new Vector3D(position.getDirection());
        Vector3D start = new Vector3D(position);
        Vector3D end =  start.add(direction.multiply(ability3Range));

        Block targetBlock = player.getTargetBlock((Set<Material>) null, targetBlockDistance);

        ArrayList<Entity> List = playerUser.getNearbyPlayers(player, ability3Range);
        for (Entity entity : List) {
            Player otherPlayer = (Player) entity;
            Vector3D targetPos = new Vector3D(otherPlayer.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (factionMethods.isSameFaction(player, otherPlayer)) {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        //player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_DEATH, 2F, 1F);
                        Location oldLoc = player.getLocation();

                        player.teleport(otherPlayer.getLocation());
                        world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 1F);

                        otherPlayer.teleport(oldLoc);

                        world.playSound(otherPlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 1F);
                    }
                }
            } else {
                if (playerUser.hasIntersection(start, end, minimum, maximum)) {
                    if (targetBlock.getType().equals(Material.AIR)) {
                        if (!plugin.cooldowns.containsKey(ability3UUID)) {
                            plugin.masterCD = ability1CD;
                            plugin.cooldowns.put(ability3UUID, plugin.masterCD);

                            //player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_DEATH, 2F, 1F);
                            Location oldLoc = player.getLocation();

                            player.teleport(otherPlayer.getLocation());
                            world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 1F);

                            otherPlayer.teleport(oldLoc);

                            world.playSound(otherPlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 1F);
                        }
                    }
                }
            }
        }
    }
}