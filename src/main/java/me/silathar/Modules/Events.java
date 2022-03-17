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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Events implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    Methods methods;
    None none;
    Warrior warrior;

    private int currentAbility = 0;


    @EventHandler(priority= EventPriority.HIGH)
    private void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        String Class = methods.getClass(player);

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
                if (none.returnAbility(player) == 1) {

                }
            } else if (Class.equals("Warrior")) {
                none.scrollAbility(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();


    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getEntity();

    }

    @EventHandler
    public void onDied(PlayerDeathEvent event) {
        Player player = event.getEntity();

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

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
