package me.silathar.Modules;

import me.silathar.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Methods implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    public boolean isSamePart(Player player, Player otherPlayer) {
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
                            }
                        }
                    }
                }
            }

        }

        return false;
    }

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

    public void hasResourceType(Player player, ItemStack itemStack, int amount) {
        if (player.getInventory().containsAtLeast(itemStack, amount)) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item.equals(itemStack)) {
                    item.setAmount(item.getAmount()-amount);
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You dont have enough " + itemStack.getType() + " to use this ability!");
        }
    }

    public String getClass(Player player) {
        return plugin.getConfig().getString("Users." + player.getUniqueId() + ".Class");
    }
}
