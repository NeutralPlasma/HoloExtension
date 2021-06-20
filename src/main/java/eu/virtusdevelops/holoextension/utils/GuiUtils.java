package eu.virtusdevelops.holoextension.utils;

import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiUtils {



    public static ItemStack getSwitchItem(boolean enabled, Material first, Material second, String name){
        ItemStack item = new ItemStack(enabled ? first : second);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(enabled ? TextUtils.colorFormat("&6" + name + " &8[&aEnabled&8]") : TextUtils.colorFormat("&6" + name + " &8[&cDisabled&8]"));
        List<String> lore = getEmptyLore();
        lore.add(TextUtils.colorFormat("&7Click to " + (enabled ? "&cdisable" : "&aenable")));
        meta.setLore(TextUtils.colorFormatList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack editItem(Material mat, String name, String current){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.colorFormat("&6" + name));
        List<String> lore = getEmptyLore();
        lore.add(TextUtils.colorFormat("&7Current value: &6" + current));
        lore.add(TextUtils.colorFormat("&7Click to &6edit."));
        meta.setLore(TextUtils.colorFormatList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static List<String> getEmptyLore(){
        return new ArrayList<>();
    }
}
