package eu.virtusdevelops.holoextension.guis;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.modules.PapiModule;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenu {
    private InventoryCreator gui = new InventoryCreator(45, TextUtils.colorFormat("&8[&bMain menu&8]"));
    private Player player;
    private ModuleManager moduleManager;
    private HoloExtension plugin;
    private List<Integer> positions = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34);
//    private List<Integer> positions = Arrays.asList(10,11); // TEST
    int currentPage = 0;
    boolean bigger = false;

    public MainMenu(Player player, ModuleManager moduleManager, HoloExtension plugin){
        this.player = player;
        this.moduleManager = moduleManager;
        this.plugin = plugin;
        load();
    }


    public void load(){
        gui.clean();


        int counter = 0;
        for (int index = currentPage*positions.size(); index < moduleManager.getModuleList().size(); index++){
            Module module = moduleManager.getModuleList().get(index);

            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(TextUtils.colorFormat("&8[&b" + module.getName() + "&8]"));
            List<String> lore = new ArrayList<>();
            lore.add("&7Click to open menu.");
            meta.setLore(TextUtils.colorFormatList(lore));
            item.setItemMeta(meta);
            Icon icon = new Icon(item);


            icon.addClickAction((player) -> {
                if(module instanceof PapiModule) {
                    player.sendMessage(module.getName());
                    // TODO: Custom menu for papi modules...
                    new ModuleGUI(player, module, plugin, moduleManager);
                }else{
                    player.sendMessage(module.getName());
                    new ModuleGUI(player, module, plugin, moduleManager);
                }
            });


            gui.setIcon(positions.get(counter), icon);
            player.sendMessage("Current: " + counter);
            if(counter+1 >= positions.size()){
                player.sendMessage("Bigger...");
                bigger = true;
                break;
            }else{
                bigger = false; // Find a better way for this.
            }
            counter++;
        }

        // TODO: add pagination stuff
        paginatorItems();


        player.openInventory(gui.getInventory());
        bigger = false;
    }

    public void paginatorItems(){
        if(currentPage > 0){
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(TextUtils.colorFormat("&8[&bPrevious page&8]"));
            List<String> lore = new ArrayList<>();
            lore.add("&7Click");
            meta.setLore(TextUtils.colorFormatList(lore));
            item.setItemMeta(meta);
            Icon icon = new Icon(item);
            icon.addClickAction((player1 -> {
                currentPage--;
                load();
            }));
            gui.setIcon(37, icon);
        }

        if(bigger){
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(TextUtils.colorFormat("&8[&bNext page&8]"));
            List<String> lore = new ArrayList<>();
            lore.add("&7Click");
            meta.setLore(TextUtils.colorFormatList(lore));
            item.setItemMeta(meta);
            Icon icon = new Icon(item);
            icon.addClickAction((player1 -> {
                currentPage++;
                load();
            }));
            gui.setIcon(43, icon);
        }
    }

}
