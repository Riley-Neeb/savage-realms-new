package me.silathar.Modules;

import java.util.Arrays;
import java.util.List;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.None;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Commands implements Listener, CommandExecutor {

    private Main plugin = Main.getPlugin(Main.class);
    PartyMethods partyMethods = new PartyMethods();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            PlayerUser playerUser = Main.players.get(sender);

            if (command.getName().equalsIgnoreCase("setconfig")) {
                if (args.length == 0) {
                    Player player = (Player) sender;
                    playerUser.setConfig(player);
                }
            }

            if (command.getName().equalsIgnoreCase("resetconfig")) {
                if (args.length == 0) {
                    Player player = (Player) sender;
                    playerUser.resetConfig(player);
                } else {
                    for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
                        if (Bukkit.getPlayer(args[0]).getName() == playerOnline.getName()) {
                            playerOnline.sendMessage(ChatColor.GREEN+sender.getName()+" /reset your config!");
                            sender.sendMessage(ChatColor.YELLOW+"You reset "+playerOnline.getName()+"'s config");

                            playerUser.resetConfig(playerOnline);
                        }
                    }
                }
            }

            if (command.getName().equalsIgnoreCase("cleanup")) {
                Player player = (Player) sender;

                World world = player.getWorld();
                List<Entity> entList = world.getEntities();
                for(Entity current : entList){
                    if (current.getType() == EntityType.DROPPED_ITEM){
                        current.remove();
                    }
                }
            }

            if (command.getName().equalsIgnoreCase("heal")) {
                if (args.length == 0) {
                    Player player = (Player) sender;
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                } else {
                    for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
                        if (Bukkit.getPlayer(args[0]).getName() == playerOnline.getName()) {
                            playerOnline.setHealth(playerOnline.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                            playerOnline.sendMessage(ChatColor.GREEN+sender.getName()+" /Healed you!");

                            sender.sendMessage(ChatColor.GREEN+"You healed "+playerOnline.getName());
                        }
                    }
                }
            }

            if (command.getName().equalsIgnoreCase("feed")) {
                Player player = (Player) sender;
                player.setFoodLevel(20);
            }

            if (command.getName().equalsIgnoreCase("class")) {
                Player player = (Player) sender;

                String isClass = plugin.getConfig().getString("Users." + player.getUniqueId() + ".Class");
                player.sendMessage(ChatColor.GREEN + "Your class is: " + ChatColor.GOLD + isClass);

            }

            if (command.getName().equalsIgnoreCase("race")) {
                Player player = (Player) sender;

                String isRace = plugin.getConfig().getString("Users." + player.getUniqueId() + ".Race");
                player.sendMessage(ChatColor.GREEN + "Your race is: " + ChatColor.GOLD + isRace);

            }

            if (command.getName().equalsIgnoreCase("setclass")) {
                Player player = (Player) sender;
                player.setWalkSpeed(0.2F);

                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "You need to include a class name.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("None")) { //0
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "None");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "None");
                    playerUser.setClass(new None());

                } else if (args[0].equalsIgnoreCase("Warrior")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Warrior");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Warrior");
                    playerUser.setClass(new Warrior());

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.IRON_SWORD, 1),
                            new ItemStack(Material.FLINT, 1),

                            new ItemStack(Material.IRON_HELMET, 1),
                            new ItemStack(Material.IRON_CHESTPLATE, 1),
                            new ItemStack(Material.IRON_LEGGINGS, 1),
                            new ItemStack(Material.IRON_BOOTS, 1),

                            new ItemStack(Material.SUGAR, 64),
                            new ItemStack(Material.PAPER, 64),
                            new ItemStack(Material.REDSTONE, 64),
                    };

                    player.getInventory().addItem(items);
                } else if (args[0].equalsIgnoreCase("Berserker")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Berserker");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Berserker");
                    playerUser.setClass(new Berserker());

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.DIAMOND_AXE, 1),
                            new ItemStack(Material.ROTTEN_FLESH, 1),

                            new ItemStack(Material.IRON_HELMET, 1),
                            new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                            new ItemStack(Material.LEATHER_LEGGINGS, 1),
                            new ItemStack(Material.IRON_BOOTS, 1),
                    };
                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Ranger")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Ranger");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Ranger");
                    playerUser.setClass(new Ranger());

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.BOW),
                            new ItemStack(Material.FEATHER, 16),

                            new ItemStack(Material.LEATHER_HELMET, 1),
                            new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                            new ItemStack(Material.LEATHER_LEGGINGS, 1),
                            new ItemStack(Material.LEATHER_BOOTS, 1),

                            new ItemStack(Material.ARROW, 64),
                            new ItemStack(Material.ARROW, 64),
                    };
                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Bard")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Bard");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Bard");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.DIAMOND_SWORD, 1),
                            new ItemStack(Material.NOTE_BLOCK, 1),

                            new ItemStack(Material.IRON_HELMET, 1),
                            new ItemStack(Material.IRON_CHESTPLATE, 1),
                            new ItemStack(Material.IRON_LEGGINGS, 1),
                            new ItemStack(Material.IRON_BOOTS, 1),
                    };
                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Priest")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Priest");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Priest");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.DIAMOND_SWORD, 1),
                            new ItemStack(Material.CLOCK, 1),

                            new ItemStack(Material.IRON_HELMET, 1),
                            new ItemStack(Material.IRON_CHESTPLATE, 1),
                            new ItemStack(Material.IRON_LEGGINGS, 1),
                            new ItemStack(Material.IRON_BOOTS, 1),
                    };
                    player.getInventory().addItem(items);
                } else if (args[0].equalsIgnoreCase("Gladiator")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Gladiator");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Gladiator");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.FEATHER, 1),
                            new ItemStack(Material.DIAMOND_SWORD, 1),

                            new ItemStack(Material.DIAMOND_HELMET, 1),
                            new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                            new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                            new ItemStack(Material.DIAMOND_BOOTS, 1),
                    };
                    player.getInventory().addItem(items);




                } else if (args[0].equalsIgnoreCase("Shaman")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Shaman");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Shaman");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.WARPED_STEM, 1),
                            new ItemStack(Material.COAL_BLOCK, 1),

                            new ItemStack(Material.TURTLE_HELMET, 1),
                            new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                            new ItemStack(Material.LEATHER_LEGGINGS, 1),
                            new ItemStack(Material.LEATHER_BOOTS, 1),
                    };
                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Assassin")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Assassin");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Assassin");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.GHAST_TEAR, 1),

                            new ItemStack(Material.LEATHER_HELMET, 1),
                            new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                            new ItemStack(Material.LEATHER_LEGGINGS, 1),
                            new ItemStack(Material.LEATHER_BOOTS, 1),

                            new ItemStack(Material.DIAMOND_SWORD, 1),
                    };
                    player.getInventory().addItem(items);


                } else if (args[0].equalsIgnoreCase("Gatherer")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Gatherer");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Gatherer");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.STONE_PICKAXE, 1),
                            new ItemStack(Material.STONE_AXE, 1),
                            new ItemStack(Material.STONE_SHOVEL, 1),
                            new ItemStack(Material.STONE_HOE, 1),
                            new ItemStack(Material.SHEARS, 1),
                            new ItemStack(Material.OAK_LOG, 16),
                    };
                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Farmer")) { //100%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Farmer");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Farmer");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.STONE_HOE, 1),
                            new ItemStack(Material.BONE, 32),
                            new ItemStack(Material.BEETROOT_SEEDS, 32),
                            new ItemStack(Material.MELON_SEEDS, 32),
                            new ItemStack(Material.PUMPKIN_SEEDS, 32),
                            new ItemStack(Material.WHEAT_SEEDS, 32),
                            new ItemStack(Material.CACTUS, 8),
                            new ItemStack(Material.SUGAR_CANE, 8),
                    };

                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("Scout")) { //0%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Scout");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "Scout");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.GOLDEN_HELMET, 1),
                            new ItemStack(Material.IRON_CHESTPLATE, 1),
                            new ItemStack(Material.IRON_LEGGINGS, 1),
                            new ItemStack(Material.GOLDEN_BOOTS, 1),

                            new ItemStack(Material.DIAMOND_SWORD, 1),
                            new ItemStack(Material.TROPICAL_FISH, 1),
                            new ItemStack(Material.PUFFERFISH, 1),
                    };

                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("MartialArtist")) { //0%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Martial Artist");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "MartialArtist");

                    player.getInventory().clear();
                    ItemStack[] items = {

                            new ItemStack(Material.STICK, 1),
                    };

                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("ChaosCrusader")) { //0%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Chaos Crusader");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "ChaosCrusader");

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.GOLDEN_SWORD, 1),

                            new ItemStack(Material.DIAMOND_HELMET, 1),
                            new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                            new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                            new ItemStack(Material.DIAMOND_BOOTS, 1),

                    };

                    player.getInventory().addItem(items);

                } else if (args[0].equalsIgnoreCase("DeathKnight")) { //0%
                    player.sendMessage(ChatColor.GOLD + "Your class is now: " + ChatColor.GOLD + "Death Knight");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Class", "DeathKnight");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".SoulPower", (int) 100);

                    player.getInventory().clear();
                    ItemStack[] items = {
                            new ItemStack(Material.WOODEN_SWORD, 1),
                            new ItemStack(Material.QUARTZ, 1),

                            new ItemStack(Material.NETHERITE_HELMET, 1),
                            new ItemStack(Material.NETHERITE_CHESTPLATE, 1),
                            new ItemStack(Material.NETHERITE_LEGGINGS, 1),
                            new ItemStack(Material.NETHERITE_BOOTS, 1),

                    };

                    player.getInventory().addItem(items);

                } else {
                    player.sendMessage(ChatColor.RED + args[0] + " is not a class.");
                }
            }

            if (command.getName().equalsIgnoreCase("setrace")) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You need to include a race name.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("Human")) { //0%
                    player.sendMessage(ChatColor.BLUE + "Your race is now: " + ChatColor.GOLD + "Human");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "Human");
                    player.setHealth(0);
                    player.setAbsorptionAmount(0);

                } else if (args[0].equalsIgnoreCase("Orc")) { //0%
                    player.sendMessage(ChatColor.BLUE + "Your race is now: " + ChatColor.GOLD + "Orc");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "Orc");
                    player.setHealth(0);
                    player.setAbsorptionAmount(0);

                } else if (args[0].equalsIgnoreCase("Dwarf")) { //0%
                    player.sendMessage(ChatColor.BLUE + "Your race is now: " + ChatColor.GOLD + "Dwarf");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "Dwarf");
                    player.setHealth(0);
                    player.setAbsorptionAmount(0);

                } else if (args[0].equalsIgnoreCase("Undead")) { //0%
                    player.sendMessage(ChatColor.BLUE + "Your race is now: " + ChatColor.GOLD + "Undead");
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "Undead");
                    player.setHealth(0);
                    player.setAbsorptionAmount(0);

                } else {
                    player.sendMessage(ChatColor.RED + args[0] + " is not a race.");
                }
            }

            if (command.getName().equalsIgnoreCase("party")) {
                Player player = (Player) sender;

                //0 = no subCommand
                //1 = subCommand
                //2+ = playerName or party name

                if (args.length == 0) {
                    partyMethods.partyInfo(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("Invite")) {
                    partyMethods.partyInvite(player, args);

                } else if (args[0].equalsIgnoreCase("Accept")) {
                    partyMethods.partyAccept(player, args);

                } else if (args[0].equalsIgnoreCase("Disband")) {
                    partyMethods.partyDisband(player);

                } else if (args[0].equalsIgnoreCase("Leave")) {
                    partyMethods.partyLeave(player);

                } else if (args[0].equalsIgnoreCase("Create")) {
                    player.sendMessage(""+args.length);
                    partyMethods.partyCreate(player, args);

                } else if (args[0].equalsIgnoreCase("info")) {
                    partyMethods.partyInfo(player);

                } else if (args[0].equalsIgnoreCase("transfer")) {

                }
            }

        } return true;
    }
}
