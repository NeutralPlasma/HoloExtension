package eu.virtusdevelops.holoextension.guis;

import eu.virtusdevelops.holoextension.HoloExtension;
import eu.virtusdevelops.holoextension.modules.Module;
import eu.virtusdevelops.holoextension.modules.ModuleManager;
import eu.virtusdevelops.holoextension.modules.ModuleDataType;
import eu.virtusdevelops.holoextension.modules.PapiModule;
import eu.virtusdevelops.holoextension.utils.GuiUtils;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.utils.AbstractChatUtil;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Bukkit;
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

        // Pagination
        paginatorItems();


        // TODO: add item for creating new papi module.
        ItemStack papi_create_item = GuiUtils.plainItem(Material.COMMAND_BLOCK, "&8[&6Create leaderboard&8]");
        Icon papi_create_icon = new Icon(papi_create_item);
        papi_create_icon.addClickAction((player1 -> {
            //load();
//            new AnvilGUI.Builder()
//                    .plugin(plugin)
//                    .text("<PLACEHOLDER>")
//                    .onClose(HumanEntity::closeInventory)
//                    .onComplete((player3, s) -> {
//                        if (s.contains(" ") || s.isBlank()){
//                            return AnvilGUI.Response.text("Incorrect...");
//                        }else{
//                            finalConstructionMenu(s);
//                        }
//                        return AnvilGUI.Response.close();
//                    }).open(player);
            createNewPapiModule();
        }));
        gui.setIcon(44, papi_create_icon);



        // TODO: add item for creating new papi module.
        ItemStack external_modules_item = GuiUtils.plainItem(Material.COMMAND_BLOCK, "&8[&6Premade leaderboards&8]");
        Icon external_modules_icon = new Icon(external_modules_item);
        external_modules_icon.addClickAction((player1 -> {
            //load();
            new ExternalModulesMenu(player, moduleManager);
        }));
        gui.setIcon(42, external_modules_icon);



        player.openInventory(gui.getInventory());
        bigger = false;
    }

    // GUIS

    private void finalConstructionMenu(String name){
//        new AnvilGUI.Builder()
//                .plugin(plugin)
//                .text("<TIME:NUMBER>")
//                .onClose(player -> {
//                    player.closeInventory();
//                    load();
//                })
//                .onComplete((player, s) -> {
//                    try{
//                        ModuleDataType type = ModuleDataType.valueOf(s);
//                        moduleManager.createNewPapiModule(name, type);
//                        return AnvilGUI.Response.close();
//                    }catch (Exception error){
//                        return AnvilGUI.Response.text("Incorrect...");
//                    }
//                }).open(player);


        player.closeInventory();
        player.sendMessage(TextUtils.colorFormat("&d-------------------"));
        player.sendMessage(TextUtils.colorFormat("&7Please specify the if placeholder is time (TIME) or number (NUMBER) based: (example for %statistic_seconds_played% you would type TIME)"));

        new AbstractChatUtil(player, (event) -> {
            if(event.getMessage().equalsIgnoreCase("cancel")){
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::load);
                return;
            }
            if(event.getMessage().isBlank() || event.getMessage().contains(" ") && !(event.getMessage().equalsIgnoreCase("TIME") || event.getMessage().equalsIgnoreCase("NUMBER"))){
                player.sendMessage(TextUtils.colorFormat("&cInvalid type please try again or type CANCEL to cancel"));
                finalConstructionMenu(name);
            }else{
                ModuleDataType type = ModuleDataType.valueOf(event.getMessage());
                moduleManager.createNewPapiModule(name, type);
            }
        }, plugin);
    }


    // Other


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

    private void createNewPapiModule(){
        Bukkit.getScheduler().runTask(plugin, runnable -> {
            player.closeInventory();
            player.sendMessage(TextUtils.colorFormat("&d-------------------"));
            player.sendMessage(TextUtils.colorFormat("&7Please specify the placeholder: (example: %statistic_seconds_played%)"));

            new AbstractChatUtil(player, (event) -> {
                if(event.getMessage().equalsIgnoreCase("cancel")){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::load);
                    return;
                }
                if(event.getMessage().isBlank() || event.getMessage().contains(" ")){
                    player.sendMessage(TextUtils.colorFormat("&cInvalid placeholder please try again or type CANCEL to cancel"));
                    createNewPapiModule();
                }else{
                    finalConstructionMenu(event.getMessage());
                }
            }, plugin);
        });

    }

}
