package eu.virtusdevelops.holoextension.commands;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoCommand extends AbstractCommand {
    private LeaderBoardManager manager;

    public InfoCommand(LeaderBoardManager leaderBoardManager) {
        super(CommandType.BOTH, "info");
        this.manager = leaderBoardManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender commandSender, String... strings) {
        //
        var timers = manager.getTimers();
        long tickCache = getAverage(timers.get("tickCache"));
        long tickModules = getAverage(timers.get("tickModules"));
        long tickCacheLimited = getAverageLimited(timers.get("tickCache"), 10);
        long tickModulesLimited = getAverageLimited(timers.get("tickModules"), 10);

        commandSender.sendMessage(TextUtils.colorFormat("&8[&bHE&8] &7Diagnostics for plugin:"));
        //commandSender.sendMessage(TextUtils.colorFormat(" &8-> &7Tick cache (10): &e" + (tickCacheLimited/1000000.0) + "&7ms"));
        commandSender.sendMessage(TextUtils.colorFormat(" &8-> &7Tick cache (100): &e" + (tickCache/1000000.0) + "&7ms"));
        //commandSender.sendMessage(TextUtils.colorFormat(" &8-> &7Tick modules (10): &e" + (tickModulesLimited/1000000.0) + "&7ms"));
        commandSender.sendMessage(TextUtils.colorFormat(" &8-> &7Tick modules (100): &e" + (tickModules/1000000.0) + "&7ms"));

        for(String timer : timers.keySet()){
            if(timer.equalsIgnoreCase("tickCache") || timer.equalsIgnoreCase("tickModules"))
                continue;
            long time = getAverage(timers.get(timer));
            commandSender.sendMessage(TextUtils.colorFormat(" &8-> &7" + timer + " (100): &e" + (time/1000000.0) + "&7ms"));

        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... strings) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "holoextension.command.info";
    }

    @Override
    public String getSyntax() {
        return "/he info";
    }

    @Override
    public String getDescription() {
        return "Reloads plugins configuration";
    }

    private long getAverage(List<Long> times){
        int count = 0;
        long max = 0L;
        for(long time : times){
            count++;
            max += time;
        }
        return max / count;
    }

    private long getAverageLimited(List<Long> times, int amount){
        long max = 0L;
        if(times.size() < 10){
            return getAverage(times);
        }else{
            for(int i = 0; i < amount; i++){
                max += times.get(times.size() - 1 - i);
            }
            return max / amount;
        }
    }
}
