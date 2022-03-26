package me.silathar.Modules;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import me.silathar.Classes.*;
import me.silathar.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerUser implements Listener {
    private Main plugin = Main.getPlugin(Main.class);
    Random random = new Random();

    Player player;
    UUID uuid;
    String className;
    Classes playerClass;
    Material classItem;

    Integer currentAbility = 1;
    Integer currentActiveAbilities = 0;
    String readiedAbility = "None";;
    Player linkedPlayer;
    Boolean isVanished = false;

    double arrowSpeed;

    public PlayerUser(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.className = getClass(player);

        //Make sure class is in here AND Commands /setclass
        if (className.equals("Warrior")) {
            this.playerClass = new Warrior();
        } else if (className.equals("Berserker")) {
            this.playerClass = new Berserker();
        } else if (className.equals("Ranger")) {
            this.playerClass = new Ranger();
        } else if (className.equals("Bard")) {
            this.playerClass = new Bard();
        } else if (className.equals("Priest")) {
            this.playerClass = new Priest();
        } else if (className.equals("Scout")) {
            this.playerClass = new Scout();
        } else if (className.equals("Assassin")) {
            this.playerClass = new Assassin();
        } else if (className.equals("ChaosCrusader")) {
            this.playerClass = new ChaosCrusader();
        } else if (className.equals("Guardian")) {
            this.playerClass = new Guardian();

        } else {
            this.playerClass = new None();
        }
    }

    public Object getPlayerClass() {
        return this.playerClass;
    }

    //Getters
    public void resetCurrentActiveActiveAbilities() {
        this.currentActiveAbilities = 0;
    }
    public int getCurrentActiveAbilities() {
        return this.currentActiveAbilities;
    }

    public void addActiveAbility(int seconds) {
        this.currentActiveAbilities += 1;

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;

                if (i >= seconds) {
                    cancel();
                    currentActiveAbilities -= 1;
                    player.sendMessage(ChatColor.GREEN + "You can use another ability!");
                }
            }

        }.runTaskTimer(plugin, 0, 20);
    }

    public int getCurrentAbility() {
        return this.currentAbility;
    }
    public void addCurrentAbility() {
        this.currentAbility++;
    }
    public void setCurrentAbility(int value) {
        this.currentAbility = value;
    }

    public Boolean isVanished() {
        return this.isVanished;
    }
    public void setVanished(boolean state) {
        this.isVanished = state;
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

    public Material getClassItem() {
        return this.classItem;
    }

    public void setClassItem(Material material) {
        this.classItem = material;
    }

    public Player getLinkedPlayer() {
        return this.linkedPlayer;
    }
    public void setLinkedPlayer(Player player) {
        this.linkedPlayer = player;
    }

    public double getArrowSpeed() {
        return this.arrowSpeed;
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

    public boolean isPositiveNumber(int num) {
        if (num > 0) {
            return true;
        } else if (num < 0) {
            return false;
        }

        return false;
    }

    public double getDotVector(Player player, Player otherPlayer) {
        Vector damagerEye = player.getEyeLocation().getDirection();
        Vector entityEye = otherPlayer.getEyeLocation().getDirection();
        return damagerEye.normalize().dot(entityEye);
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
    public void setClassName(Player player) {
        this.className = getClass(player);
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
            plugin.getServer().getConsoleSender().sendMessage(PartiesByLeader);
        }

        for (String[] PartiesByLeader: plugin.parties.keySet()) {
            for(int i = 0; i < PartiesByLeader.length; i++){
                if (PartiesByLeader[i].equals(player.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;

        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();

        if (Math.abs(c.x) > e.x + ad.x)
            return false;
        if (Math.abs(c.y) > e.y + ad.y)
            return false;
        if (Math.abs(c.z) > e.z + ad.z)
            return false;

        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
            return false;
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
            return false;
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
            return false;

        return true;
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

        } else if (armorType.equals("Scout")) {
            if (Helm != null && Chest != null && Legs != null && Boots != null) {
                if (Helm.getType().equals(Material.GOLDEN_HELMET)) {
                    if (Chest.getType().equals(Material.IRON_CHESTPLATE)) {
                        if (Legs.getType().equals(Material.IRON_LEGGINGS)) {
                            if (Boots.getType().equals(Material.GOLDEN_BOOTS)) {
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

    public ArrayList<Entity> getNearbyPlayers(Player player, int Distance) {
        ArrayList<Entity> nearbyPlayers = new ArrayList<>();

        for(Entity entity : player.getNearbyEntities(Distance, Distance, Distance)) {
            if (entity instanceof Player) {
                nearbyPlayers.add(entity);
            }
        }

        return nearbyPlayers;
    }

    //Damage Methods
    public void damagePlayer(Player player, double damage) {
        double points = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        double toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
        PotionEffect effect = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        int resistance = effect == null ? 0 : effect.getAmplifier();
        int epf = getEPF(player);

        player.damage(calculateDamageApplied(damage, points, toughness, resistance, epf));
    }

    public double calculateDamageApplied(double damage, double points, double toughness, int resistance, int epf) {
        double withArmorAndToughness = damage * (1 - Math.min(20, Math.max(points / 5, points - damage / (2 + toughness / 4))) / 25);
        double withResistance = withArmorAndToughness * (1 - (resistance * 0.2));
        double withEnchants = withResistance * (1 - (Math.min(20.0, epf) / 25));
        return withEnchants;
    }

    public static int getEPF(Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack helm = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack legs = inv.getLeggings();
        ItemStack boot = inv.getBoots();

        return (helm != null ? helm.getEnchantmentLevel(Enchantment.DAMAGE_ALL) : 0) +
                (chest != null ? chest.getEnchantmentLevel(Enchantment.DAMAGE_ALL) : 0) +
                (legs != null ? legs.getEnchantmentLevel(Enchantment.DAMAGE_ALL) : 0) +
                (boot != null ? boot.getEnchantmentLevel(Enchantment.DAMAGE_ALL) : 0);
    }

    //Class Methods



    public void showPlayer(Player player) {
        for (Player otherPlayers : Bukkit.getServer().getOnlinePlayers()) {
            otherPlayers.showPlayer(plugin, player);
        }
    }

    public void vanishPlayer(Player player, int seconds) {
        for (Player otherPlayers : Bukkit.getServer().getOnlinePlayers()) {
            if (otherPlayers != player) {
                otherPlayers.hidePlayer(plugin, player);
            }
        }

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;

                if (i == seconds) {
                    this.cancel();
                    for (Player otherPlayers : Bukkit.getServer().getOnlinePlayers()) {
                        if (otherPlayers != player) {
                            otherPlayers.showPlayer(plugin, player);
                        }
                    }
                }
            }

        }.runTaskTimer(plugin, 0, 20);
    }
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
        boolean headshot = y - hitPosition > 1.5d;

        if (headshot == true) {
            return true;
        } else {
            return false;
        }
    }

}