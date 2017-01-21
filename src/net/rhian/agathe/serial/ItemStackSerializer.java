package net.rhian.agathe.serial;

import org.bukkit.inventory.ItemStack;

import net.rhian.agathe.configuration.AbstractSerializer;
import net.rhian.agathe.util.ItemUtil;

public class ItemStackSerializer extends AbstractSerializer<ItemStack> {
    @Override
    public String toString(ItemStack data) {
        return data == null ? null : ItemUtil.itemToString(data);
    }
    @Override
    public ItemStack fromString(Object data) {
        return data == null ? null : ItemUtil.stringToItem(data.toString());
    }
}
