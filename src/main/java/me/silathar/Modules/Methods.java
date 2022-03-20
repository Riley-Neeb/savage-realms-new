package me.silathar.Modules;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Methods implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    //Getters
    public int getKills(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Kills");
    }
    public int getDeaths(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Deaths");
    }
    public String getClass(Player player) {
        return plugin.getConfig().getString("Users." + player.getUniqueId() + ".Class");
    }
    public String getStatusEffect(Player player, String status) {
        return plugin.getConfig().getString("Users." + player.getUniqueId() + "."+status);
    }
    public Boolean isStunned(Player player) {
        if (plugin.getConfig().getBoolean("Users." + player.getUniqueId() + ".isStunned") == true) {
            return true;
        } else {
            return false;
        }
    }
    public Boolean isLeaping(Player player) {
        if (plugin.getConfig().getBoolean("Users." + player.getUniqueId() + ".isLeaping") == true) {
            return true;
        } else {
            return false;
        }
    }

    //Setters
    public void resetConfig(Player player) {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nResetting "+player.getName()+"'s config");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Name", player.getName());
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Class" , "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Slowed", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isLeaping", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Ability", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".currentAbility", 1);
        plugin.saveConfig();
    }
    public void setConfig(Player player) {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSetting "+player.getName()+"'s config");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Setting " + player.getName() + "'s config");
        plugin.getConfig().set("Users." + player.getUniqueId(), player);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Name", player.getName());
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Class" , "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Kills" , 0);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Deaths" , 0);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Race", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Slowed", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isLeaping", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", false);
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Ability", "None");
        plugin.getConfig().set("Users." + player.getUniqueId() + ".currentAbility", 1);
        plugin.saveConfig();
    }
    public String setConfigValue(Player player, String nameOfValue, Object value) {
        if (value instanceof Integer) {
            int checkedValue = (int) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, value);
        } else if (value instanceof Boolean) {
            boolean checkedValue = (boolean) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, checkedValue);
        } else if (value instanceof String) {
            String checkedValue = (String) value;

            plugin.getConfig().set("Users." + player.getUniqueId() + "."+nameOfValue, checkedValue);
        }

        return "None";
    }
    public void stunForDuration(Player player, int seconds) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", true);
        player.sendMessage(ChatColor.DARK_BLUE + "You were " + ChatColor.GOLD + "stunned!");

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;

                if (i == seconds) {
                    this.cancel();
                    plugin.getConfig().set("Users." + player.getUniqueId() + ".isStunned", false);
                    player.sendMessage(ChatColor.DARK_BLUE + "You're no longer " + ChatColor.GOLD + "stunned!");
                }
            }

        }.runTaskTimer(plugin, 0, 20);
    }

    //Class Items
    public boolean hasItemInHand(Player player, Material material) {
        Material mainHand = player.getInventory().getItemInMainHand().getType();

        if (mainHand.equals(material)) {
            return true;
        }

        return false;
    }
    public boolean hasItemInOffHand(Player player, Material material) {
        Material mainHand = player.getInventory().getItemInOffHand().getType();

        if (mainHand.equals(material)) {
            return true;
        }

        return false;
    }
    public boolean hasResourceType(Player player, Material material, int amount) {
        if(player.getInventory().containsAtLeast(new ItemStack(material), amount)){
            return true;
        } else {
            player.sendMessage(ChatColor.RED+"You don't have enough resources!");
            return false;
        }
    }
    public void setResourceAmount(Player player, Material material, int amount) {
        for (ItemStack item: player.getInventory().getContents()) {
            if (item != null && item.getType().equals(material)) {
                item.setAmount(item.getAmount()-amount);
            }
        }
    }
    //Party

    public boolean isSameParty(Player player, Player otherPlayer) {
        String party1 = getPartyName(player);
        String party2 = getPartyName(otherPlayer);

        if (party1 != null && party2 != null) {
            if (party1.equals(party2)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
    public String getPartyName(Player player) {
        if (isInParty(player)) {
            for (String[] PartiesByLeader: plugin.parties.keySet()) {
                for(int i = 0; i < PartiesByLeader.length; i++){
                    if (PartiesByLeader[i].equals(player.getName())) {
                        String partyName = plugin.parties.get(PartiesByLeader);

                        return partyName;
                    }
                }
            }
        } else {
            return null;
        }


        return null;
    }
    public boolean isInParty(Player player) {
        for (String[] PartiesByLeader: plugin.parties.keySet()) {
            for(int i = 0; i < PartiesByLeader.length; i++){
                if (PartiesByLeader[i].equals(player.getName())) {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean isWearingArmorType(Player player, String armorType) {
        ItemStack Helm = player.getInventory().getHelmet();
        ItemStack Chest = player.getInventory().getChestplate();
        ItemStack Legs = player.getInventory().getLeggings();
        ItemStack Boots = player.getInventory().getBoots();

        if (armorType.equals("Any")) {
            return true;

        } else if (armorType.equals("Berserker")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.IRON_HELMET)) {
                    if (Chest.getType().equals(Material.LEATHER_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.LEATHER_LEGGINGS)) {
                            if (Boots.getType().equals(Material.IRON_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Mixed")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                return true;
            }

        } else if (armorType.equals("Leather")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.LEATHER_HELMET)) {
                    if (Chest.getType().equals(Material.LEATHER_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.LEATHER_LEGGINGS)) {
                            if (Boots.getType().equals(Material.LEATHER_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Chainmail")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.CHAINMAIL_HELMET)) {
                    if (Chest.getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.CHAINMAIL_LEGGINGS)) {
                            if (Boots.getType().equals(Material.CHAINMAIL_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Iron")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.IRON_HELMET)) {
                    if (Chest.getType().equals(Material.IRON_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.IRON_LEGGINGS)) {
                            if (Boots.getType().equals(Material.IRON_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Golden")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.GOLDEN_HELMET)) {
                    if (Chest.getType().equals(Material.GOLDEN_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.GOLDEN_LEGGINGS)) {
                            if (Boots.getType().equals(Material.GOLDEN_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Diamond")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.DIAMOND_HELMET)) {
                    if (Chest.getType().equals(Material.DIAMOND_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.DIAMOND_LEGGINGS)) {
                            if (Boots.getType().equals(Material.DIAMOND_BOOTS)) {
                                return true;
                            }
                        }
                    }
                }
            }

        } else if (armorType.equals("Netherite")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.NETHERITE_HELMET)) {
                    if (Chest.getType().equals(Material.NETHERITE_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.NETHERITE_LEGGINGS)) {
                            if (Boots.getType().equals(Material.NETHERITE_BOOTS)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

        }

        return false;
    }

    //Nearby Functions
    public ArrayList<String> getNearbyParty(Player player, int Distance) {
        ArrayList<String> nearbyPlayers = new ArrayList<String>();

        for(Entity entity : player.getNearbyEntities(Distance, Distance, Distance)) {

            for (String[] PartiesByLeader: plugin.parties.keySet()) {
                for(int i = 0; i < PartiesByLeader.length; i++){
                    if (PartiesByLeader[i] != null && PartiesByLeader[i].equals(entity.getName())) {
                        nearbyPlayers.add(entity.getName());
                    }
                }
            }

        }

        return nearbyPlayers;
    }
    public ArrayList<Entity> getNearbyPlayers(Player player, int Distance) {
        ArrayList<Entity> nearbyPlayers = new ArrayList<>();

        for(Entity entity : player.getNearbyEntities(Distance, Distance, Distance)) {
            if (entity instanceof Player) {
                nearbyPlayers.add(entity);
            }
        }

        return nearbyPlayers;
    }


}
