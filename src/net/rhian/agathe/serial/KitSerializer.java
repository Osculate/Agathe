package net.rhian.agathe.serial;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.rhian.agathe.configuration.AbstractSerializer;
import net.rhian.agathe.exception.PracticeException;
import net.rhian.agathe.kit.Kit;

public class KitSerializer extends AbstractSerializer<Kit> {
    private final String blockSplit = "&&";
    private final ItemStackSerializer is = new ItemStackSerializer();

    @Override
    public String toString(Kit data) {
        StringBuilder sb = new StringBuilder("");
        sb.append(data.getName() + blockSplit);
        for(int i = 0; i < data.getInventory().length; i++){
            ItemStack item = data.getInventory()[i];
            if(item == null) continue;
            if(item.getType() == Material.AIR) continue;
            sb.append(is.toString(item) + "@=" + i + "!!");
        }
        sb.append(blockSplit);
        for(int i = 0; i < data.getArmor().length; i++){
            ItemStack item = data.getArmor()[i];
            if(item == null) continue;
            if(item.getType() == Material.AIR) continue;
            sb.append(is.toString(item) + "@="+i+"!!");
        }
        return sb.toString();
    }

    @Override
    public Kit fromString(Object data) {
        if(data instanceof String){
            ItemStack[] iArmor = new ItemStack[4];
            ItemStack[] iInv = new ItemStack[36];
            String s = (String) data;
            String[] split = s.split(blockSplit);
            String name = split[0];
            if(split.length >= 2){
                String inv = split[1];
                String[] d = inv.split("!!");
                for(String str : d){
                    //str = (itemdata)@=i
                    String itemData = str.split("@=")[0];
                    int id = Integer.parseInt(str.split("@=")[1]);
                    iInv[id] = is.fromString(itemData);
                }
            }

            if(split.length >= 3){
                String armor = split[2];
                String[] d = armor.split("!!");
                for(String str : d){
                    //str = (itemdata)@=i
                    String itemData = str.split("@=")[0];
                    int id = Integer.parseInt(str.split("@=")[1]);
                    iArmor[id] = is.fromString(itemData);
                }
            }
            return new Kit(name, iArmor, iInv);
        } else {
            throw new PracticeException("KitSerializer object data is not a string?");
        }
    }
}
