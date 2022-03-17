package eu.virtusdevelops.holoextension.commands;

import eu.virtusdevelops.holoextension.gui.MainGUI;
import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends AbstractCommand {
    private LeaderBoardManager manager;

    public MenuCommand(LeaderBoardManager leaderBoardManager) {
        super(CommandType.PLAYER_ONLY, "menu");
        this.manager = leaderBoardManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender commandSender, String... strings) {
        //
        new MainGUI((Player) commandSender, manager);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... strings) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "holoextension.command.menu";
    }

    @Override
    public String getSyntax() {
        return "/he menu";
    }

    @Override
    public String getDescription() {
        return "Opens configuration menu";
    }
}
