package me.silathar.Modules;

import java.util.UUID;
import java.util.function.Function;

import me.silathar.Classes.Berserker;
import me.silathar.Classes.None;
import me.silathar.Classes.Ranger;
import me.silathar.Classes.Warrior;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayerUser implements Listener {
    Methods methods = new Methods();

    Player player;
    UUID uuid;
    String className;
    Classes playerClass;
    int currentAbility = 1;

    public PlayerUser(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.className = methods.getClass(player);

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


}