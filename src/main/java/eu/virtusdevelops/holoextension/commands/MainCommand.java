package eu.virtusdevelops.holoextension.commands;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    private LeaderBoardManager manager;

    public MainCommand(LeaderBoardManager manager){
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        manager.reload();

        commandSender.sendMessage(TextUtils.colorFormat("&8[&bHE&8]&7 Reloaded plugin."));

        return true;
    }
}
