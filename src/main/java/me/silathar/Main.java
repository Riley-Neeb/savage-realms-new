package me.silathar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import me.silathar.Classes.None;
import me.silathar.Classes.Warrior;
import me.silathar.Modules.Commands;
import me.silathar.Modules.Events;
import me.silathar.Modules.PlayerUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public final class Main extends JavaPlugin implements Listener {

    private static Plugin plugin;
    public HashMap<String, Integer> cooldowns = new HashMap<>();
    public HashMap<String, Integer> outgoingInvites = new HashMap<>();
    public HashMap<String, Boolean> invisList = new HashMap<>();
    public HashMap<String[], String> parties = new HashMap<>();
    public static Map<Player, PlayerUser> players = new HashMap<>();

    public int masterCD;

    public ChatColor allyColor = ChatColor.LIGHT_PURPLE;
    public ChatColor enemyColor = ChatColor.YELLOW;
    public ChatColor successColor = ChatColor.GREEN;
    public ChatColor errorColor = ChatColor.RED;
    public ChatColor abilityUseColor = ChatColor.GOLD;
    public ChatColor abilityNameColor = ChatColor.BLUE;
    public ChatColor stunColor = ChatColor.DARK_BLUE;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSavage Realms has Initialized");
        getServer().getPluginManager().registerEvents(new Events(), this);
        //CLASSES
        getServer().getPluginManager().registerEvents(new None(), this);
        getServer().getPluginManager().registerEvents(new Warrior(), this);
        //COMMANDS
        this.getCommand("setconfig").setExecutor(new Commands());
        this.getCommand("resetconfig").setExecutor(new Commands());
        this.getCommand("cleanup").setExecutor(new Commands());
        this.getCommand("heal").setExecutor(new Commands());
        this.getCommand("feed").setExecutor(new Commands());
        this.getCommand("class").setExecutor(new Commands());
        this.getCommand("race").setExecutor(new Commands());
        this.getCommand("setclass").setExecutor(new Commands());
        this.getCommand("setrace").setExecutor(new Commands());
        this.getCommand("party").setExecutor(new Commands());

        //SETTINGS
        cooldowns.clear();
        outgoingInvites.clear();
        plugin = this;
        gameLoop();
        loadConfig();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\nSavage Realms has reloaded! Stuff may break. Restart if so.");
            player.sendMessage(ChatColor.AQUA + "Savage Realms has reloaded! Stuff may break. Restart if so.");
            players.put(player, new PlayerUser(player));

            for (Player otherPlayers : Bukkit.getServer().getOnlinePlayers()) {
                if (player.isInvisible()) {
                    otherPlayers.showPlayer(plugin, otherPlayers);
                    player.sendMessage(ChatColor.YELLOW+"Rejoined invisible, set to visible.");
                }
            }
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSavage Realms has Uninitialized");
        loadConfig();
        reloadConfig();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void gameLoop() {
        final Iterator<Map.Entry<String, Integer>> it = cooldowns.entrySet().iterator();

        new BukkitRunnable() {
            //COOLDOWNS SYSTEM
            @Override
            public void run() {
                if (cooldowns.isEmpty()) {
                    return;
                }

                final Iterator<Map.Entry<String, Integer>> iterator = cooldowns.entrySet().iterator();
                while (iterator.hasNext()) {
                    //COOLDOWNS
                    final Map.Entry<String, Integer> entry = iterator.next();

                    //UUID = ClassName + AbilityName + PlayerUUID
                    //EXAMPLE: NoneAbility1 UUID (yes theres a space between uuid and abilityname)

                    String UUID = entry.getKey();
                    int timeleft = cooldowns.get(UUID);

                    if (timeleft <= 0) {
                        iterator.remove();
                        continue;
                    }

                    entry.setValue(timeleft - 1);

                    //SNEAK
                    /*
                     for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            if (UUID.contains("Invisibility")) {
                                boolean isInvisible = invisList.get(UUID);

                                if (!isInvisible) {
                                    toHide.hidePlayer(plugin, toHide);
                                    player.hidePlayer(plugin, toHide);
                                    invisList.put(""+toHide.getUniqueId(), true);
                                    toHide.sendMessage(ChatColor.GRAY+"You've gone invisible!");
                                }
                            } else {
                                boolean isInvisible = invisList.get(UUID);

                                if (isInvisible) {
                                    toHide.showPlayer(plugin, toHide);
                                    player.showPlayer(plugin, toHide);
                                    toHide.sendMessage(ChatColor.GRAY+"You're now visible!");
                                }
                            }
                        }
                    }
                     */
                }
            }

        }.runTaskTimer(this, 0, 20);

        /*
         new BukkitRunnable() {
            //Invis SYSTEM
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        //SNEAK/ASSASSIN


                        if (cooldowns.containsKey(SneakUUID)) {
                            int sneakLeft = cooldowns.get(SneakUUID);

                            if (invisList.containsKey(SneakUUID)) {
                                if (sneakLeft > 20) {
                                    player.hidePlayer(toHide);
                                    player.hidePlayer(plugin, toHide);
                                } else if (sneakLeft == 20 && player == toHide) {
                                    player.sendMessage(ChatColor.BLUE + "Your sneak has worn off" );
                                } else if (sneakLeft < 20) {
                                    player.showPlayer(toHide);
                                    player.showPlayer(plugin, toHide);
                                }
                            } else if (!invisList.containsKey(SneakUUID)) {
                                player.showPlayer(toHide);
                                player.showPlayer(plugin, toHide);
                            }
                        }
                    }
                }
            }
          }.runTaskTimer(this, 0, 20);
         */




        new BukkitRunnable() {
            //PARTIES/INVITES SYSTEM
            @Override
            public void run() {
                if (outgoingInvites.isEmpty()) {
                    return;
                }

                final Iterator<Map.Entry<String, Integer>> iterator = outgoingInvites.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<String, Integer> entry = iterator.next();

                    String UUID = entry.getKey();
                    int timeleft = outgoingInvites.get(UUID);

                    if (timeleft <= 0) {
                        iterator.remove();
                        continue;
                    }

                    entry.setValue(timeleft - 1);
                }
            }

        }.runTaskTimer(this, 0, 20);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (outgoingInvites.isEmpty()) {
                    return;
                }

                for (String uuid: outgoingInvites.keySet()) {
                    int timeLeft = outgoingInvites.get(uuid);

                    if (timeLeft < 1) {
                        outgoingInvites.remove(uuid);
                    } else {
                        outgoingInvites.put(uuid, timeLeft - 1);
                    }
                }


            }

        }.runTaskTimer(this, 0, 20);
    }
}
