package net.rhian.ipractice.database.mongo.serial;

import java.util.HashMap;

import com.mongodb.util.JSON;

import net.rhian.ipractice.configuration.AbstractSerializer;

public class MapSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        return JSON.serialize(data);
    }

    @Override
    public HashMap fromString(Object data) {
        HashMap map = (HashMap) JSON.parse(((String) data));
        return map;
    }
}
