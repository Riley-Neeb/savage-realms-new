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

public class Warrior extends Classes implements Listener {

    public Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    public String armorType = "Iron";
    public String ability1Name = "First Aid";
    public String ability2Name = "Combat Injection";
    public String ability3Name = "Lethal Tempo";

    public Material scroll_item = Material.FLINT;
    public Material ability1_material = Material.PAPER;    //Healing
    public Material ability2_material = Material.SUGAR;    //Injection
    public Material ability3_material = Material.REDSTONE; //Lethal Tempo
    public boolean ability1_requiresResources = true;
    public boolean ability2_requiresResources = true;
    public boolean ability3_requiresResources = true;

    public int abilityUsageAmount = 0;
    public int maxAbility = 4;
    public int ability1CD = 8;
    public int ability2CD = 25;
    public int ability3CD = 20;

    String className = this.getClass().getSimpleName();

    private void fireAbility(Player player, int ability) {
        boolean hasItem            = (methods.hasItemInHand(player, scroll_item));
        boolean wearingRightArmor  = (methods.isWearingArmorType(player, armorType));

        if (hasItem) {
            if (wearingRightArmor) {
                if (ability == 1) {
                    boolean hasEnoughResources = (methods.hasResourceType(player, ability1_material, 2));

                    if (hasEnoughResources) {
                        ability1(player);
                        methods.setResourceAmount(player, ability1_material, 2);
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough resources!");
                    }
                } else  if (ability == 2) {
                    boolean hasEnoughResources = (methods.hasResourceType(player, ability2_material, 2));

                    if (hasEnoughResources) {
                        ability2(player);
                        methods.setResourceAmount(player, ability2_material, 2);
                    }
                } else  if (ability == 3) {
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

    public void ability1(Player player) {
        String ability1UUID = className + ability1Name + player.getUniqueId();
        String abilityUse = ChatColor.AQUA + "You have used " + ChatColor.RED + ChatColor.BOLD + ability1Name+"!";

        if (!plugin.cooldowns.containsKey(ability1UUID)) {
            plugin.masterCD = ability1CD;
            plugin.cooldowns.put(ability1UUID, plugin.masterCD);
            player.sendMessage(abilityUse);

            player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 60, 2)));
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

            player.addPotionEffect((new PotionEffect(PotionEffectType.ABSORPTION, 100, 3)));
            player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 100, 1)));
            player.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 100, 0)));
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