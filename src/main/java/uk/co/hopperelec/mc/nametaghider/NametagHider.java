package uk.co.hopperelec.mc.nametaghider;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class NametagHider extends JavaPlugin implements Listener {
    Team showNametag;
    Team hideNametag;

    @Override
    public void onEnable() {
        final ScoreboardManager boardManager = Bukkit.getScoreboardManager();
        if (boardManager == null) {
            System.out.println("Failed to get scoreboard manager");
            setEnabled(false);
            return;
        }
        final Scoreboard board = boardManager.getMainScoreboard();
        try {
            showNametag = board.registerNewTeam("showNametag");
            showNametag.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        } catch (IllegalArgumentException e) {
            showNametag = board.getTeam("showNametag");
        }
        try {
            hideNametag = board.registerNewTeam("hideNametag");
            hideNametag.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        } catch (IllegalArgumentException e) {
            hideNametag = board.getTeam("hideNametag");
        }

        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        showNametag.addEntry(event.getPlayer().getName());
        hideNametag.removeEntry(event.getPlayer().getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("togglenametag")) {
            if (showNametag.getEntries().contains(sender.getName())) {
                showNametag.removeEntry(sender.getName());
                hideNametag.addEntry(sender.getName());
                sender.sendMessage("Nametag hidden");
            } else {
                showNametag.addEntry(sender.getName());
                hideNametag.removeEntry(sender.getName());
                sender.sendMessage("Nametag shown");
            }
            return true;
        }
        return false;
    }
}
