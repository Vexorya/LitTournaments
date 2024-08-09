package me.waterarchery.littournaments.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.libs.gui.guis.Gui;
import me.waterarchery.littournaments.LitTournaments;
import me.waterarchery.littournaments.guis.TournamentGUI;
import me.waterarchery.littournaments.handlers.LoadHandler;
import me.waterarchery.littournaments.handlers.PlayerHandler;
import me.waterarchery.littournaments.handlers.TournamentHandler;
import me.waterarchery.littournaments.models.Tournament;
import me.waterarchery.littournaments.models.TournamentPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "littournaments", alias = {"tournaments", "tournament"})
public class TournamentCommand extends BaseCommand {

    @Default
    public void defaultCmd(CommandSender sender) {
        LitLibs libs = LitTournaments.getLitLibs();

        if (sender instanceof Player player) {
            Gui gui = TournamentGUI.of(player);
            gui.open(player);
        }
        else {
            libs.getMessageHandler().sendLangMessage(sender, "InGameOnly");
        }
    }

    @SubCommand("join")
    public void join(CommandSender sender, @Suggestion("tournaments") String tournamentName) {
        LitLibs libs = LitTournaments.getLitLibs();

        if (sender instanceof Player player) {
            TournamentHandler tournamentHandler = TournamentHandler.getInstance();
            PlayerHandler playerHandler = PlayerHandler.getInstance();
            TournamentPlayer tournamentPlayer = playerHandler.getPlayer(player.getUniqueId());
            Tournament tournament = tournamentHandler.getTournament(tournamentName);

            if (!sender.hasPermission("littournaments.player.join." + tournamentName) &&
                    !sender.hasPermission("littournaments.player.join.*")) {
                libs.getMessageHandler().sendLangMessage(sender, "NoPermission");
                libs.getSoundHandler().sendSound(player, "Sounds.NoPermission");
                return;
            }

            if (tournament != null) {
                if (!tournamentPlayer.isRegistered(tournament)) {
                    tournamentPlayer.join(tournament);
                    libs.getMessageHandler().sendLangMessage(sender, "SuccessfullyRegistered");
                    libs.getSoundHandler().sendSound(player, "Sounds.SuccessfullyJoined");
                }
                else {
                    libs.getSoundHandler().sendSound(player, "Sounds.AlreadyJoined");
                    libs.getMessageHandler().sendLangMessage(sender, "AlreadyJoined");
                }
            }
            else {
                libs.getMessageHandler().sendLangMessage(sender, "NoTournamentWithName");
            }
        }
        else {
            libs.getMessageHandler().sendLangMessage(sender, "InGameOnly");
        }
    }

    @SubCommand("leave")
    public void leave(CommandSender sender, @Suggestion("tournaments") String tournamentName) {
        LitLibs libs = LitTournaments.getLitLibs();

        if (sender instanceof Player player) {
            TournamentHandler tournamentHandler = TournamentHandler.getInstance();
            PlayerHandler playerHandler = PlayerHandler.getInstance();
            TournamentPlayer tournamentPlayer = playerHandler.getPlayer(player.getUniqueId());
            Tournament tournament = tournamentHandler.getTournament(tournamentName);

            if (!sender.hasPermission("littournaments.player.leave." + tournamentName) &&
                    !sender.hasPermission("littournaments.player.leave.*")) {
                libs.getMessageHandler().sendLangMessage(sender, "NoPermission");
                libs.getSoundHandler().sendSound(player, "Sounds.NoPermission");
                return;
            }

            if (tournament != null) {
                if (tournamentPlayer.isRegistered(tournament)) {
                    tournamentPlayer.leave(tournament);
                    libs.getMessageHandler().sendLangMessage(sender, "SuccessfullyLeaved");
                }
                else {
                    libs.getMessageHandler().sendLangMessage(sender, "JoinFirst");
                }
            }
            else {
                libs.getMessageHandler().sendLangMessage(sender, "NoTournamentWithName");
            }
        }
        else {
            libs.getMessageHandler().sendLangMessage(sender, "InGameOnly");
        }
    }

    @SubCommand("reload")
    @Permission("littournaments.admin.reload")
    public void leave(CommandSender sender) {
        LitLibs libs = LitTournaments.getLitLibs();
        LoadHandler.getInstance().load();
        libs.getMessageHandler().sendLangMessage(sender, "FilesReloaded");
    }

}
