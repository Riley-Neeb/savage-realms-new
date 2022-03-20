package me.silathar.Modules;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import me.silathar.Classes.None;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;

public class Events implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    private int currentAbility = 0;

    @EventHandler(priority= EventPriority.HIGH)
    private void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        String className = methods.getClass(player);

        PlayerUser playerUser = Main.players.get(player);
        //If player is stunned return null
        if(event.getItem() == null) {
            return;
        }

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            playerUser.playerClass.scrollAbility(player);
        } else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (playerUser.currentAbility == 1) {
                playerUser.playerClass.ability1(player);
            } else if (playerUser.currentAbility == 2) {
                playerUser.playerClass.ability2(player);
            } else if (playerUser.currentAbility == 3) {
                playerUser.playerClass.ability3(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            String Class = methods.getClass(player);

            if (event.getCause() == DamageCause.FALL){
                if (Class.equals("Ranger")) {
                    if (methods.isLeaping(player)) {
                        event.setCancelled(true);
                        plugin.getConfig().set("Users." + event.getEntity().getUniqueId() + ".isLeaping", false);
                    } else {
                        int rollRandom = (int)(Math.random() * 99 + 1);

                        if (rollRandom <= 25) {
                            event.setCancelled(true);
                            event.getEntity().sendMessage(ChatColor.GREEN + "Rolled fall damage away");
                            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2F, 1F);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void EntityDamageEntity(PlayerMoveEvent event) {
        Player player = (Player) event.getPlayer();

        if (methods.isStunned(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You're stunned, you cant move!");
        }
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player entity = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();

                String entityClass = methods.getClass(entity);
                String damagerClass = methods.getClass(damager);

                if (methods.isSameParty(damager, entity)) {
                    event.setCancelled(true);
                    return;
                } else {
                    if (methods.isStunned(damager)) {
                        event.setCancelled(true);
                        damager.sendMessage(ChatColor.RED+"You can't attack, you're stunned!");
                        return;
                    }

                    if (damagerClass.equals("Berserker")) {
                        //if (berserker.returnReadiedAbility(damager).equals("Headbutt")) {
                            //berserker.ability1(damager, entity);
                        //}
                    }
                }
            }

        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            Projectile proj = (Projectile) event.getEntity();
            String Class = methods.getClass(shooter);

            if (Class.equals("Ranger")) {
                //String tempShotUUID = shooter.getUniqueId()+ranger.ability2Name;
                //String PoisonUUID = shooter.getUniqueId()+ranger.ability3Name;

            }
        }
    }

    @EventHandler
    public void onDied(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player killedPlayer = event.getEntity();
            int Deaths = methods.getDeaths(killedPlayer);

            if (event.getEntity().getKiller() instanceof Player) {
                Player killer = killedPlayer.getKiller();
                int Kills = methods.getKills(killer);

                plugin.getConfig().set("Users." + killer.getUniqueId() + ".Kills", Kills+1);
            }

            plugin.getConfig().set("Users." + killedPlayer.getUniqueId() + ".Deaths", Deaths+1);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean hasPlayed = player.hasPlayedBefore();

        plugin.players.put(player, new PlayerUser(player));

        if (hasPlayed) {
            for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                    if (toHide.isInvisible()) {
                        players.showPlayer(plugin, toHide);
                        toHide.sendMessage(ChatColor.YELLOW+"Rejoined invisible, set to visible.");
                        players.sendMessage(ChatColor.YELLOW+toHide.getName()+" Rejoined invisible, set to visible.");
                    }
                }
            }
        } else {
            methods.setConfig(player);
        }

    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.players.remove(player);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
    }
}
