package eu.virtusdevelops.holoextension.guis;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.modules.ModuleType;
import eu.virtusdevelops.holoextension.utils.GuiUtils;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ModuleGUI {
    private InventoryCreator gui = new InventoryCreator(45, TextUtils.colorFormat(""));
    private Module module;
    private ModuleManager moduleManager;
    private HoloExtension plugin;
    private Player player;

    public ModuleGUI(Player player, Module module, HoloExtension holoExtension, ModuleManager moduleManager){
        this.module = module;
        this.player = player;
        this.plugin = holoExtension;
        this.moduleManager = moduleManager;
        gui.addCloseActions((a,b) -> {

        });
        player.closeInventory();
        load();
    }

    private void load(){
        //player.closeInventory();
        gui.clean();


        // Enable item
        ItemStack enable = GuiUtils.getSwitchItem(module.isEnabled(),
                Material.GREEN_STAINED_GLASS,
                Material.RED_STAINED_GLASS,
                "Module status");

        Icon enable_icon = new Icon(enable);
        enable_icon.addClickAction((player) -> {
            if(module.isEnabled()){
                module.setEnabled(false);
                module.onDisable();
            }else{
                module.setEnabled(true);
                module.onEnable();
            }
            moduleManager.updateModule(module);

            load();
        });
        gui.setIcon(10, enable_icon);


        // refresh item
        ItemStack refresh = GuiUtils.editItem(Material.BOOK, "Refresh interval", String.valueOf(module.getRepeat()));
        Icon refresh_icon = new Icon(refresh);
        refresh_icon.addClickAction((player) -> {
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .text("<INTERVAL>")
                    .onClose(player1 -> {
                        player1.closeInventory();
                        load();
                    })
                    .onComplete((player1, s) -> {
                        long number = Long.parseLong(s);
                        if (number > 0){
                            module.setRepeat(number);
                            module.reload();
                            moduleManager.updateModule(module);
                            player.performCommand("hd reload");
                        }
                        return AnvilGUI.Response.close();
                    }).open(player);
        });
        gui.setIcon(13, refresh_icon);
        // size item
        ItemStack size = GuiUtils.editItem(Material.BOOK, "Leaderboard size", String.valueOf(module.getSize()));
        Icon size_icon = new Icon(size);
        size_icon.addClickAction((player) -> {
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .text("<SIZE>")
                    .onClose(player1 -> {
                        player1.closeInventory();
                        load();
                    })
                    .onComplete((player1, s) -> {
                        int number = Integer.parseInt(s);
                        if (number > 0){
                            module.setSize(number);
                            module.reload();
                            moduleManager.updateModule(module);
                            player.performCommand("hd reload");
                        }
                        return AnvilGUI.Response.close();
                    }).open(player);
        });
        gui.setIcon(29, size_icon);


        // update offline item

        ItemStack offline = GuiUtils.getSwitchItem(module.isUpdateOffline(),
                Material.GREEN_STAINED_GLASS,
                Material.RED_STAINED_GLASS,
                "Offline updating");

        Icon offline_icon = new Icon(offline);
        offline_icon.addClickAction((player) -> {
            if(module.isUpdateOffline()){
                module.setUpdateOffline(false);
            }else{
                module.setUpdateOffline(true);
            }
            moduleManager.updateModule(module);
            module.reload();
            load();
        });
        gui.setIcon(31, offline_icon);



        // noplayer item
        ItemStack noplayer = GuiUtils.editItem(Material.BOOK, "No player replacer", String.valueOf(module.getSize()));
        Icon noplayer_icon = new Icon(noplayer);
        noplayer_icon.addClickAction((player) -> {
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .text("<TEXT>")
                    .onClose(player1 -> {
                        player1.closeInventory();
                        load();
                    })
                    .onComplete((player1, s) -> {
                        module.setNoplayer(s);
                        module.reload();
                        moduleManager.updateModule(module);
                        player.performCommand("hd reload");
                        return AnvilGUI.Response.close();
                    }).open(player);
        });
        gui.setIcon(16, noplayer_icon);


        // Create hologram
        ItemStack hologram = GuiUtils.plainItem(Material.COMMAND_BLOCK, "&8[&6Create hologram&8]");
        Icon hologram_icon = new Icon(hologram);
        hologram_icon.addClickAction((player) -> {


            // Change this anyway its ugly but works.
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .text("<NAME>")
                    .onClose(HumanEntity::closeInventory)
                    .onComplete((player1, s) -> {
                        // DISPATCH THE CREATION COMMANDS
                        if(s.contains(" ")){
                            player.sendMessage(TextUtils.colorFormat("&cYou can't use spaces in hologram name."));
                            return AnvilGUI.Response.close();
                        }

                        player.performCommand("hd create " + s);
                        for(int i = 1; i <= module.getSize(); i++){
                            player.performCommand("hd addline " +
                                    s +
                                    " {he-" + module.getName() + "-" + i + "-user} - " +
                                    "{he-" + module.getName() + "-" + i + "-value}");
                        }
                        player.performCommand("hd reload");

                        return AnvilGUI.Response.close();
                    }).open(player);


        });
        gui.setIcon(33, hologram_icon);


        player.openInventory(gui.getInventory());
    }

}
