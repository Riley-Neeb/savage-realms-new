package me.silathar.Modules;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import me.silathar.Main;
import me.silathar.Classes.None;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Events implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();
    FactionMethods factionMethods = new FactionMethods();

    private int currentAbility = 0;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerUser playerUser = Main.players.get(player);
        Material classItem = playerUser.getClassItem();

        if (event.getBlock().getType().equals(classItem)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = (Player) event.getPlayer();
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.getClass(player).equals("Scout")) {
            player.setWalkSpeed((float) 0.4);
        }

        if (playerUser.isStunned(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You're stunned, you cant move!");
        }
    }


    @EventHandler(priority= EventPriority.HIGH)
    private void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        PlayerUser playerUser = Main.players.get(player);

        if (playerUser.isStunned(player)) {
            event.setCancelled(true);
            return;
        }

        if (event.getItem() == null) {
            return;
        }


        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            playerUser.playerClass.scrollAbility(player);
        } else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            playerUser.playerClass.fireAbility(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerUser playerUser = Main.players.get(player);
            String Class = playerUser.className;
            Boolean isLeaping = playerUser.isLeaping(player);

            if (event.getCause() == DamageCause.FALL){
                if (Class.equals("Ranger")) {
                    if (isLeaping== true) {
                        event.setCancelled(true);
                        playerUser.setLeaping(player, false);
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
    public void EntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.PROJECTILE){
            Projectile proj = (Projectile) event.getDamager();

            if (proj.getShooter() instanceof Player) {
                Player entity = (Player) event.getEntity();
                Player shooter = (Player) proj.getShooter();
                PlayerUser playerUser = Main.players.get(shooter);
                PlayerUser entityUser = Main.players.get(entity);

                if (factionMethods.isSameFaction(shooter, entity)) {
                    event.setCancelled(true);
                    shooter.sendMessage(ChatColor.RED+"Can't hurt a friendly player!");
                } else {
                    double multiplier = 1;
                    int addedDamage = 0;

                    if (playerUser.didArrowHeadshot(proj, shooter, entity)) {
                        double distance = shooter.getLocation().distance(entity.getLocation());

                        if (distance <= 25) {
                            multiplier = 1.25;
                        } else if (distance >= 25 && distance <= 50) {
                            multiplier = 1.5;
                        } else if (distance >= 50 && distance <= 100) {
                            multiplier = 2;
                        } else if (distance >= 100 && distance <= 150) {
                            multiplier = 3;
                        } else if (distance >= 150) {
                            multiplier = 5;
                        }

                        shooter.sendMessage(ChatColor.GOLD+"You got a headshot! | Multiplier: "+multiplier+"x");
                    }

                    if (proj.hasMetadata("Pestilent Arrow")) {
                        Location loc = entity.getLocation();

                        loc.setPitch(entityUser.getMinMax(entity, -90, 90)); //min of -90 and max of 90
                        loc.setYaw(entityUser.getMinMax(entity, 0, 360)); //min of -90 and max of 90
                        entity.teleport(loc);
                    }

                    if (proj.hasMetadata("Poison Arrow")) {
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 2));
                    }

                    if (proj.hasMetadata("bonusDamage1")) {
                        event.setDamage(event.getDamage()+1);
                    } else if (proj.hasMetadata("bonusDamage2")) {
                        event.setDamage(event.getDamage()+2);
                    } else if (proj.hasMetadata("bonusDamage3")) {
                        event.setDamage(event.getDamage()+3);
                    } else if (proj.hasMetadata("bonusDamage4")) {
                        event.setDamage(event.getDamage()+4);
                    }

                    event.setDamage((event.getDamage()+addedDamage)*multiplier);
                }
            }
        } else {
            if (event.getEntity() instanceof Player) {
                if (event.getDamager() instanceof Player) {
                    Player entity = (Player) event.getEntity();
                    PlayerUser entityUser = Main.players.get(entity);

                    Player damager = (Player) event.getDamager();
                    PlayerUser damagerUser = Main.players.get(damager);
                    String damagerClass = damagerUser.getClass(damager);


                    if (factionMethods.isSameFaction(damager, entity)) {
                        event.setCancelled(true);
                        return;
                    } else {

                        if (entityUser.getLinkedPlayer() != null) {
                            event.setCancelled(true);
                            Player linkedEntity = entityUser.getLinkedPlayer();

                            entityUser.damagePlayer(entity, event.getDamage()/2);
                            entityUser.damagePlayer(linkedEntity, event.getDamage()/2);

                            entity.sendMessage(ChatColor.GREEN+"Your damage was shared with your link!");
                            linkedEntity.sendMessage(ChatColor.GREEN+"Your damage was shared with your link!");
                        }

                        if (damagerUser.isStunned(damager)) {
                            event.setCancelled(true);
                            damager.sendMessage(ChatColor.RED+"You can't attack, you're stunned!");
                            return;
                        }

                        if (damagerClass.equals("Berserker")) {
                            if (damagerUser.getReadiedAbility().equals("Headbutt")) {
                                damagerUser.playerClass.ability1(damager, entity);
                            }
                        } else if (damagerClass.equals("Assassin")) {
                            double multiplier = 1;

                            if (damagerUser.getReadiedAbility().equals("Ankle Cutter")) {
                                damagerUser.playerClass.ability1(damager, entity);
                            } else if (damagerUser.getReadiedAbility().equals("Flicker")) {
                                damagerUser.playerClass.ability2(damager, entity);
                            }

                            double dot = damagerUser.getDotVector(damager, entity);
                            if (dot > 0.5) {
                                damager.sendMessage(ChatColor.GOLD+"Backstab!");
                                multiplier = multiplier*2.5;
                            }

                            if (damagerUser.isVanished()) {
                                damagerUser.setVanished(false);
                                damagerUser.showPlayer(damager);
                                damager.removePotionEffect(PotionEffectType.INVISIBILITY);
                                damager.sendMessage(ChatColor.GOLD+"First Strike!");
                                multiplier = multiplier*1.5;
                            }

                            event.setDamage(event.getDamage()*multiplier);
                        } else if (damagerClass.equals("Scout")) {
                            if (damagerUser.getReadiedAbility().equals("Bat to The Face!")) {
                                damagerUser.playerClass.ability1(damager, entity);
                            }
                        }
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

            PlayerUser playerUser = Main.players.get(shooter);
            String className = playerUser.getClass(shooter);

            if (className.equals("Ranger")) {
                int bonusDamage = playerUser.rangerBonusDMG(shooter, proj);
                proj.setMetadata("bonusDamage"+bonusDamage, new FixedMetadataValue(plugin, true));

                if (playerUser.getReadiedAbility().equals("Multi Shot")) {
                    playerUser.arrowSpeed = proj.getVelocity().length(); //Hacky way of getting the draw of the bow
                    playerUser.playerClass.ability3(shooter);
                }

                String PestilentArrowUUID = shooter.getUniqueId()+"Pestilent Arrow";
                boolean canPestilent = playerUser.getRandomChance(shooter, 25);
                if (canPestilent == true) {
                    if (!plugin.cooldowns.containsKey(PestilentArrowUUID)) {
                        plugin.masterCD = 3;
                        plugin.cooldowns.put(PestilentArrowUUID, plugin.masterCD);
                        shooter.sendMessage(ChatColor.AQUA + "Pestilent Arrow" + "!");

                        proj.setMetadata("Pestilent Arrow", new FixedMetadataValue(plugin, true));
                    } else {
                        shooter.sendMessage(ChatColor.RED + "Pestilent Arrow" + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(PestilentArrowUUID) + " seconds" + ChatColor.RED + " left.");
                    }
                }

                String PoisonArrowUUID = shooter.getUniqueId()+"Poison Arrow";
                boolean canPoisonArrow = playerUser.getRandomChance(shooter, 25);
                if (canPoisonArrow == true) {
                    if (!plugin.cooldowns.containsKey(PoisonArrowUUID)) {
                        plugin.masterCD = 3;
                        plugin.cooldowns.put(PoisonArrowUUID, plugin.masterCD);
                        shooter.sendMessage(ChatColor.AQUA + "Poison Arrow" + "!");

                        proj.setMetadata("Poison Arrow", new FixedMetadataValue(plugin, true));
                    } else {
                        shooter.sendMessage(ChatColor.RED + "Poison Arrow" + " On cooldown, " + ChatColor.YELLOW + plugin.cooldowns.get(PoisonArrowUUID) + " seconds" + ChatColor.RED + " left.");
                    }
                }
            }
        }
    }

    public void onEntityInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PlayerUser playerUser = Main.players.get(player);
        String className = playerUser.getClass(player);

        if (event.getRightClicked().getType().equals(EntityType.PLAYER)) {
            Player clickedPlayer = (Player) event.getRightClicked();

            if (className.equals("Guardian")) {
                playerUser.playerClass.ability1(player, clickedPlayer);
            }
        }
    }
    @EventHandler
    public void onDied(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player killedPlayer = event.getEntity();
            PlayerUser playerUser = Main.players.get(killedPlayer);
            int Deaths = playerUser.getDeaths(killedPlayer);

            if (event.getEntity().getKiller() instanceof Player) {
                Player killer = killedPlayer.getKiller();
                int Kills = playerUser.getKills(killer);

                plugin.getConfig().set("Users." + killer.getUniqueId() + ".Kills", Kills+1);
            }

            plugin.getConfig().set("Users." + killedPlayer.getUniqueId() + ".Deaths", Deaths+1);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.players.put(player, new PlayerUser(player));

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        if (!player.hasPlayedBefore()) {
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
        PlayerUser playerUser = Main.players.get(player);
        playerUser.resetCurrentActiveActiveAbilities();

        if (playerUser.getClass(player).equals("Scout")) {
            player.setWalkSpeed((float) 0.4);
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
    }
}
