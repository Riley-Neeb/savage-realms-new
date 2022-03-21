package me.silathar.Modules;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.None;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerUser implements Listener {
    private Main plugin = Main.getPlugin(Main.class);
    Random random = new Random();

    Player player;
    UUID uuid;
    String className;
    Classes playerClass;

    Integer currentAbility = 1;
    String readiedAbility = "None";;
    double arrowSpeed;

    public PlayerUser(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.className = getClass(player);

        if (className.equals("Warrior")) {
            this.playerClass = new Warrior();
        } else if (className.equals("Berserker")) {
            this.playerClass = new Berserker();
        } else if (className.equals("Ranger")) {
            this.playerClass = new Ranger();
        } else {
            this.playerClass = new None();
        }
    }

    public Object getPlayerClass() {
        return this.playerClass;
    }



    //Getters
    public int getCurrentAbility() {
        return this.currentAbility;
    }
    public void addCurrentAbility() {
        this.currentAbility++;
    }
    public void setCurrentAbility(int value) {
        this.currentAbility = value;
    }


    public String getReadiedAbility() {
        return this.readiedAbility;
    }
    public void setReadiedAbility(String newAbility) {
        this.readiedAbility = newAbility;
    }

    public void setClass(Classes newClass) {
        this.playerClass = newClass;
    }

    public double getArrowSpeed() {
        return this.arrowSpeed;
    }

    public void setArrowSpeed(Double amount) {
        this.arrowSpeed = amount;
    }
    //Methods
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
    public boolean getRandomChance(Player player, int threshold) {
        int chance = (int)(Math.random() * 99 + 1);

        if (chance <= threshold) {
            return true;
        } else {
            return false;
        }
    }

    public int getMinMax(Player player, int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }


    //Config
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

        this.className = null;
        this.playerClass = null;
        this.currentAbility = 1;
        this.readiedAbility = "None";
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

        this.className = null;
        this.playerClass = null;
        this.currentAbility = 1;
        this.readiedAbility = "None";
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


    public int getKills(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Kills");
    }
    public int getDeaths(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Deaths");
    }
    public String getClass(Player player) {
        return plugin.getConfig().getString("Users." + player.getUniqueId() + ".Class");
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
    public void setLeaping(Player player, boolean state) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".isLeaping", state);
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

        player.sendMessage(ChatColor.RED+"You're not wearing the right armor!");
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


    //Class Methods
    public int rangerBonusDMG(Player player, Projectile projectile) {
        ItemStack Helm = player.getInventory().getHelmet();
        ItemStack Chest = player.getInventory().getChestplate();
        ItemStack Legs = player.getInventory().getLeggings();
        ItemStack Boots = player.getInventory().getBoots();

        int helmBonus = 0;
        int chestBonus = 0;
        int legsBonus = 0;
        int bootsBonus = 0;
        int totalBonus = 0;

        if (Helm != null && Helm.getType().equals(Material.LEATHER_HELMET)) {
            helmBonus = 1;
        } else {
            helmBonus = 0;
        }

        if (Chest != null && Chest.getType().equals(Material.LEATHER_CHESTPLATE)) {
            chestBonus = 1;
        } else {
            chestBonus = 0;
        }

        if (Legs != null && Legs.getType().equals(Material.LEATHER_LEGGINGS)) {
            legsBonus = 1;
        } else {
            legsBonus = 0;
        }

        if (Boots != null && Boots.getType().equals(Material.LEATHER_BOOTS)) {
            bootsBonus = 1;
        } else {
            bootsBonus = 0;
        }
        totalBonus = (helmBonus+chestBonus+legsBonus+bootsBonus);

        return totalBonus;
    }
    public boolean didArrowHeadshot(Projectile projectile, Player shooter, Player damagedEntity) {
        double y = projectile.getLocation().getY();
        double hitPosition = damagedEntity.getLocation().getY();
        boolean headshot = y - hitPosition > 1.35d;

        if (headshot == true) {
            return true;
        } else {
            return false;
        }
    }

}