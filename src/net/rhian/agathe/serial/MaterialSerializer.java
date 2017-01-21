package net.rhian.agathe.serial;

import org.bukkit.Material;

import net.rhian.agathe.configuration.AbstractSerializer;

public class MaterialSerializer extends AbstractSerializer<Material> {
    @Override
    public String toString(Material data) {
        return data.toString();
    }
    
    @Override
    public Material fromString(Object data) {
        return Material.valueOf(((String)data).toUpperCase());
    }
}
