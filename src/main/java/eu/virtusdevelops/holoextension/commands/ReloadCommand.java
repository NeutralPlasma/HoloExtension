package eu.virtusdevelops.holoextension.commands;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {
    private LeaderBoardManager manager;

    public ReloadCommand(LeaderBoardManager leaderBoardManager) {
        super(CommandType.BOTH, "reload");
        this.manager = leaderBoardManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender commandSender, String... strings) {
        long time = System.nanoTime();
        long managerTime = manager.reload();
        time = (System.nanoTime() - time);
        commandSender.sendMessage(TextUtils.colorFormat("&8[&bHE&8] &aReloaded plugin! &8(&e" + (time / 1000000.0) + "&7ms&8)"));
        commandSender.sendMessage(TextUtils.colorFormat("&8-> &7Manager: &e" + (managerTime/1000000.0) + "&7ms"));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... strings) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "holoextension.command.reload";
    }

    @Override
    public String getSyntax() {
        return "/he reload";
    }

    @Override
    public String getDescription() {
        return "Reloads plugins configuration";
    }
}
