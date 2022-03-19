package eu.virtusdevelops.holoextension.gui;

import eu.virtusdevelops.holoextension.leaderboards.LeaderBoardManager;
import eu.virtusdevelops.holoextension.leaderboards.modules.DefaultModule;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.Paginator;
import eu.virtusdevelops.virtuscore.utils.ItemUtils;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainGUI {
    private Player player;
    private Paginator inventory;
    private LeaderBoardManager manager;

    public MainGUI(Player player, LeaderBoardManager manager){
        this.player = player;
        this.manager = manager;
        this.inventory = new Paginator(player,List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34), TextUtils.colorFormat(""), 54);

        load();
    }



    public void load(){
        var timers = manager.getTimers();
        var modules = manager.getModules();
        List<Icon> icons = new ArrayList<Icon>();

        for(DefaultModule module : modules){
            var item = new ItemStack(Material.BOOK);
            item = ItemUtils.setName(item, TextUtils.colorFormat("&e" + module.getNameFormated()));
            item = ItemUtils.setLore(item, TextUtils.colorFormatList( List.of(
                    "&8| &7Tick: &e" + (getAverage(timers.get(module.getName())) / 1000000.0) + "&7ms",
                    "&8| &7Placeholder: &e" + module.getName(),
                    "&8| &7Format: &e" + module.getFormat())));
            Icon icon = new Icon(item);
            icon.addClickAction((player) -> {

            });
            icons.add(icon);
        }
        inventory.setIcons(icons);
        staticIcons();

        inventory.page();
    }


    private void staticIcons(){
        // pre-made leaderboards menu
        ItemStack premade = new ItemStack(Material.CHEST);
        premade = ItemUtils.setName(premade, TextUtils.colorFormat("&8[&bPremade leaderboards&8]"));
        premade = ItemUtils.setLore(premade, TextUtils.colorFormatList(List.of("&7Click to see all premade leaderboards")));
        Icon premadeIcon = new Icon(premade);
        premadeIcon.addClickAction((player) -> {
           new PremadeModulesGUI(player, manager);
        });
        inventory.addIcon(52, premadeIcon);


        // create new leaderboard menu


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
}
