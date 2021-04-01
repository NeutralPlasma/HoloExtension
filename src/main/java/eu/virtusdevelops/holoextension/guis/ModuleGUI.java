package eu.virtusdevelops.holoextension.guis;

import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.PapiModule;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ModuleGUI {
    private InventoryCreator gui = new InventoryCreator(45, TextUtils.colorFormat(""));
    private Module module;
    private Player player;
    //private List<Integer> positions = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34);
    //int currentPage = 0;

    public ModuleGUI(Player player, Module module){
        this.module = module;
        this.player = player;
        gui.addCloseActions((a,b) -> {

        });
    }

    private void load(){
        player.closeInventory();
        gui.clean();



        // Enable item
        ItemStack enable = new ItemStack(module.isEnabled() ? Material.GREEN_STAINED_GLASS : Material.RED_STAINED_GLASS);
        ItemMeta meta = enable.getItemMeta();
        meta.setDisplayName(module.isEnabled() ? TextUtils.colorFormat("&8[&aEnabled&8]") : TextUtils.colorFormat("&cDisabled&8]"));
        List<String> lore = new ArrayList<>();
        lore.add(TextUtils.colorFormat("&7Click to " + (module.isEnabled() ? "disable" : "enable")));
        meta.setLore(lore);
        enable.setItemMeta(meta);
        Icon icon = new Icon(enable);
        icon.addClickAction((player) -> {
            if(module.isEnabled()){
                module.onDisable();
                module.setEnabled(false);
            }else{
                module.onEnable();
                module.setEnabled(true);
            }
            load();
        });
        // TODO: setup the positions for items.

        if(module instanceof PapiModule){

            // Name Item





            // Type item

            // Clear Cache item
        }
    }
}
