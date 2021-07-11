package eu.virtusdevelops.holoextension.commands;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.guis.MainMenu;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TemporaryCommand implements CommandExecutor {
    private final HoloExtension plugin;
    private final ModuleManager manager;

    public TemporaryCommand(HoloExtension plugin,ModuleManager manager){
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("reload")){
                plugin.reload();
            }
        }else {
            if (commandSender instanceof Player) {
                new MainMenu((Player) commandSender, manager, plugin);
            }else{
                commandSender.sendMessage(TextUtils.colorFormat("&cOnly player can execute this command."));
            }
        }
        return true;
    }
}
