package net.rhian.agathe.spawn.items;

import org.bukkit.inventory.ItemStack;

import net.rhian.agathe.spawn.item.SpawnItem;
import net.rhian.agathe.spawn.item.SpawnItemAction;
import net.rhian.agathe.spawn.item.SpawnItemType;
import net.rhian.agathe.util.ItemBuilder;

public class SimpleSpawnItem extends SpawnItem {

    private final int slot;
    private final ItemStack item;
    private final SpawnItemType spawnItemType;
    private final SpawnItemAction action;

    public SimpleSpawnItem(int slot, ItemStack item, SpawnItemAction action) {
        this.slot = slot;
        this.item = item;
        this.spawnItemType = SpawnItemType.NORMAL;
        this.action = action;
    }

    public SimpleSpawnItem(int slot, ItemStack item, SpawnItemType type, SpawnItemAction action) {
        this.slot = slot;
        this.item = item;
        this.spawnItemType = type;
        this.action = action;
    }

    public SimpleSpawnItem(int slot, ItemBuilder item, SpawnItemAction action) {
        this.slot = slot;
        this.item = item.build();
        this.spawnItemType = SpawnItemType.NORMAL;
        this.action = action;
    }

    public SimpleSpawnItem(int slot, ItemBuilder item, SpawnItemType type, SpawnItemAction action) {
        this.slot = slot;
        this.item = item.build();
        this.spawnItemType = type;
        this.action = action;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public SpawnItemType getType() {
        return spawnItemType;
    }

    @Override
    public SpawnItemAction getAction() {
        return action;
    }
}
