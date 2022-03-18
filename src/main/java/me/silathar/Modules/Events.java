package me.silathar.Modules;

import me.silathar.Classes.Warrior;
import me.silathar.Main;
import me.silathar.Classes.None;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class Events implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();
    None none = new None();
    Warrior warrior = new Warrior();

    private int currentAbility = 0;


    @EventHandler(priority= EventPriority.HIGH)
    private void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        String Class = methods.getClass(player);

        //If player is stunned return null
        if(event.getItem() == null) {
            return;
        }

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (Class.equals("None")) {
                none.scrollAbility(player);
            } else if (Class.equals("Warrior")) {
                none.scrollAbility(player);
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (Class.equals("None")) {
                none.fireAbility(player);
            } else if (Class.equals("Warrior")) {
                none.fireAbility(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
        }

    }

    @EventHandler
    public void EntityDamageEntity(PlayerToggleSprintEvent event) {
        Player player = (Player) event.getPlayer();

        //Stop sprinting if slowed
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
        }

        if (event.getEntity() instanceof Player) {
            Player entity = (Player) event.getEntity();
        }

        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player entity = (Player) event.getEntity();
                Player damager = (Player) event.getEntity();
            }

        }
    }

    @EventHandler
    public void onDied(PlayerDeathEvent event) {
        Player player = event.getEntity();

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean hasPlayed = player.hasPlayedBefore();

        if (hasPlayed) {

        } else {
            methods.setConfig(player);
        }

    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

    }
}
