package eu.virtusdevelops.holoextension.guis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.virtusdevelops.holoextension.modules.*;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExternalModulesMenu {
    private InventoryCreator gui = new InventoryCreator(45, TextUtils.colorFormat("&8[&bPremade leaderboards&8]"));
    private Player player;
    private ModuleManager moduleManager;
    private List<Integer> positions = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34);

    int currentPage = 0;

    public ExternalModulesMenu(Player player, ModuleManager moduleManager){
        this.player = player;
        this.moduleManager = moduleManager;
        load();
    }


    public void load(){
        gui.clean();
        // modules list
        List<ModuleData> modules = new ArrayList<>();
        boolean hasNextPage = false;

        // parse modules from json webpage
        try {
            URL url = new URL("https://database.virtusdevelops.eu?page=" + currentPage + "&amount=" + positions.size());
            URLConnection request = url.openConnection();
            request.connect();



            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            if(rootobj.get("error") != null){
                // Print connection error thing.
                player.sendMessage(TextUtils.colorFormat("&cDatabase error... Please contact developer."));
                return;
            }

            hasNextPage = rootobj.get("has_next_page").getAsBoolean();

            rootobj.getAsJsonArray("modules").forEach(data -> {
                JsonObject object = data.getAsJsonObject();
                modules.add(new ModuleData(object.get("name").getAsString(),
                        object.get("placeholder").getAsString(),
                        object.get("type").getAsString(),
                        object.get("refresh").getAsLong(),
                        object.get("size").getAsInt(),
                        object.get("updateoffline").getAsInt() == 1,
                        object.get("extension").getAsString(),
                        object.has("format") ? object.get("format").getAsInt() : 1));
            });




        }catch (ConnectException connection){
            player.sendMessage(TextUtils.colorFormat("&cCould not connect to servers.... Please contact developer."));
            player.closeInventory();
            connection.printStackTrace();
            return;
        }catch (Exception error){
            player.sendMessage(TextUtils.colorFormat("&cCheck console for errors"));
            player.closeInventory();
            error.printStackTrace();
            return;
        }


        int counter = 0;

        for(ModuleData module : modules){
            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(TextUtils.colorFormat("&8[&b" + module.getName() + "&8]"));
            List<String> lore = new ArrayList<>();
            lore.add("&7Click to add this module.");
            lore.add("&7" + module.getPlaceholder());
            lore.add("&7Papi extension: " + module.getExtensionName());
            meta.setLore(TextUtils.colorFormatList(lore));
            item.setItemMeta(meta);
            Icon icon = new Icon(item);


            icon.addClickAction((player) -> {
                player.closeInventory();
                player.performCommand("papi ecloud download " + module.getExtensionName());
                player.performCommand("papi reload");
                moduleManager.createNewPapiModule(module);
                player.sendMessage(TextUtils.colorFormat("&7Successfully added: &b" + module.getName() + " &7(" + module.getPlaceholder() + "&7)"));

            });
            gui.setIcon(positions.get(counter), icon);
            counter++;
        }


        // Pagination
        paginatorItems(hasNextPage);



        player.openInventory(gui.getInventory());
    }



    // Other


    public void paginatorItems(Boolean nextpage){
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

        if(nextpage){
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
