package net.rhian.agathe.party;

import org.bukkit.Material;

import lombok.Getter;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

@Getter
public enum PartyEvent {

    FFA(Material.GOLDEN_APPLE),
    TWO_TEAMS(Material.DIAMOND_CHESTPLATE);

    private final Material icon;
    private final String name;

    PartyEvent(Material icon) {
        this.icon = icon;
        this.name = WordUtils.capitalizeFully(toString().replaceAll("_"," "));
    }

    public static PartyEvent fromString(String s){
        for(PartyEvent event : values()){
            if(event.toString().equalsIgnoreCase(s)){
                return event;
            }
        }
        return null;
    }

}
