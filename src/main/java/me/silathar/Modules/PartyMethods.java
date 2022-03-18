package me.silathar.Modules;

import me.silathar.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class PartyMethods implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    Methods methods = new Methods();

    public void partyCreate(Player player, String[] args) {
        String partyString = "";

        for (String[] PartiesByLeader : plugin.parties.keySet()) {
            String partyName = plugin.parties.get(PartiesByLeader);

            for (int i = 0; i < PartiesByLeader.length; i++) {
                if (PartiesByLeader[i] == player.getName()) {
                    player.sendMessage(ChatColor.RED + "You're already in a party!");
                    return;
                }
            }
        }

        if (args.length <= 1) {
            player.sendMessage(ChatColor.RED + "Party must include a name! You can have up to 3 words");
            return;
        }

        if (args.length > 5) {
            player.sendMessage(ChatColor.RED + "Party has too many words in its name!");
            return;
        }

        if (args.length < 5) {
            for (int i = 1; i < args.length; i++) { //loop threw all the arguments
                String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
                partyString = partyString + "" + arg; //add the argument to myString
            }

            String partyUUID = partyString;

            if (plugin.parties.size() == 0) {
                if (!plugin.parties.containsKey(partyUUID)) {
                    String[] defaultString = new String[30];
                    defaultString[0] = player.getName();
                    plugin.parties.put(defaultString, partyUUID);

                    player.sendMessage(ChatColor.GOLD + "You have made the party: " + ChatColor.BLUE + partyString);
                    return;
                }
            } else if (plugin.parties.size() > 0) {
                for (String[] getMembers : plugin.parties.keySet()) {
                    String getPartyName = plugin.parties.get(getMembers);

                    if (getPartyName.equals(partyString)) {
                        player.sendMessage(ChatColor.RED + "There's already a party with this name!");
                        return;
                    } else {
                        if (!plugin.parties.containsKey(partyUUID)) {
                            String[] defaultString = new String[30];
                            defaultString[0] = player.getName();
                            plugin.parties.put(defaultString, partyUUID);

                            player.sendMessage(ChatColor.GOLD + "You have made the party: " + ChatColor.BLUE + partyString);
                            ;
                        }
                    }
                }
            }
        }
    }

    public void partyInfo(Player player) {
        if (methods.getPartyName(player) == null) {
            player.sendMessage(ChatColor.RED + "You cant check party info if you're not in a party!");
            return;
        }

        for (String[] PartiesByLeader : plugin.parties.keySet()) {

            String partyName = plugin.parties.get(PartiesByLeader);
            int memberCount = 1;

            for (int i = 0; i < PartiesByLeader.length; i++) {
                if (PartiesByLeader[i].equals(player.getName())) {
                    player.sendMessage(net.md_5.bungee.api.ChatColor.GOLD + "Party Name: " + net.md_5.bungee.api.ChatColor.BLUE + partyName);
                    player.sendMessage(net.md_5.bungee.api.ChatColor.GOLD + "Party Leader: " + net.md_5.bungee.api.ChatColor.BLUE + PartiesByLeader[0]);
                    player.sendMessage(net.md_5.bungee.api.ChatColor.GOLD + "Members: " + memberCount);
                    player.sendMessage(Arrays.toString(PartiesByLeader));
                    return;
                }
            }
        }
    }

    public void partyLeave(Player player) {
        if (methods.getPartyName(player) == null) {
            player.sendMessage(ChatColor.RED + "You cant leave if you're not in a party!");
            return;
        }

        for (String[] PartiesByLeader : plugin.parties.keySet()) {
            for (int i = 0; i < PartiesByLeader.length; i++) {
                if (PartiesByLeader[i].equals(player.getName())) {

                    if (i == 0) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "You must disband the party or transfer ownership!");
                        return;
                    } else {
                        PartiesByLeader[i] = null;
                        player.sendMessage(ChatColor.DARK_PURPLE + "You left the party!");
                        return;
                    }
                }
            }
        }
    }

    public void partyDisband(Player player) {
        if (methods.getPartyName(player) == null) {
            player.sendMessage(ChatColor.RED + "You cant disband if you're not in a party!");
            return;
        }

        for (String[] PartiesByLeader : plugin.parties.keySet()) {
            if (PartiesByLeader[0].equals(player.getName())) {
                plugin.parties.remove(PartiesByLeader);
                player.sendMessage(ChatColor.DARK_PURPLE + "You disbanded the party!");

                for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
                    for (int i = 1; i < PartiesByLeader.length; i++) {
                        if (PartiesByLeader[i] != null) {
                            if (PartiesByLeader[i].equals(playerOnline.getName())) {
                                player.sendMessage(ChatColor.DARK_PURPLE + PartiesByLeader[0] + " has disbanded the party!");
                            }
                        }
                    }
                }
            }
        }
    }

    public void partyInvite(Player player, String[] args) {
        if (methods.getPartyName(player) == null) {
            player.sendMessage(ChatColor.RED + "You cant invite if you're not in a party!");
            return;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Must include a players name!");
            return;
        }

        for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
            if (playerOnline.getName().equals(args[1])) {
                for (String[] PartiesByLeader : plugin.parties.keySet()) {

                    if (PartiesByLeader[0].equals(player.getName())) {
                        TextComponent acceptMessage = new TextComponent(player.getName() + " has invited you to their party " + "[ACCEPT]");

                        acceptMessage.setColor(ChatColor.DARK_PURPLE);
                        acceptMessage.setBold(true);
                        acceptMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + player.getName()));
                        acceptMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder("Click here to accept").color(ChatColor.GOLD).italic(true).create()));


                        player.sendMessage(ChatColor.DARK_PURPLE + "You have invited " + playerOnline.getName());
                        playerOnline.spigot().sendMessage(acceptMessage);

                        String inviteUUID = playerOnline.getName() + " Invite From " + player.getName();
                        plugin.outgoingInvites.put(inviteUUID, 20);

                        return;
                    }
                }
            }
        }
    }

    public void partyAccept(Player player, String[] args) {
        for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
            if (Bukkit.getPlayer(args[1]).getName() == playerOnline.getName()) {
                String inviteUUID = player.getName() + " Invite From " + playerOnline.getName();

                if (plugin.outgoingInvites.containsKey(inviteUUID)) {
                    plugin.outgoingInvites.remove(inviteUUID);

                    for (String[] PartiesByLeader : plugin.parties.keySet()) {
                        for (int i = 0; i < PartiesByLeader.length; i++) {
                            if (PartiesByLeader[i] != null) {
                                if (PartiesByLeader[i].equals(player.getName())) {
                                    player.sendMessage(ChatColor.DARK_PURPLE + "You're already in a party!");
                                    return;
                                }
                            }
                        }
                    }

                    for (String[] PartiesByLeader : plugin.parties.keySet()) {
                        if (PartiesByLeader[0].equals(playerOnline.getName())) {
                            String partyName = plugin.parties.get(PartiesByLeader);

                            for (int i = 1; i < PartiesByLeader.length; i++) {
                                if (i == PartiesByLeader.length) {
                                    player.sendMessage(ChatColor.DARK_PURPLE + "Party is full!");
                                    return;
                                }

                                if (PartiesByLeader[i] == null) {
                                    PartiesByLeader[i] = player.getName();
                                    player.sendMessage(ChatColor.DARK_PURPLE + "You have joined " + ChatColor.YELLOW + playerOnline.getName() + "'s party!");
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "The invite for party has expired!");
                }
            }
        }
    }

}
