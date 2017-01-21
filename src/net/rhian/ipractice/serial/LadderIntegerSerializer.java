package net.rhian.ipractice.serial;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.util.JSON;

import net.rhian.ipractice.configuration.AbstractSerializer;
import net.rhian.ipractice.exception.PracticeException;
import net.rhian.ipractice.ladder.Ladder;

public class LadderIntegerSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        Map<Ladder, Integer> map = (HashMap<Ladder,Integer>) data;

        Map<String, Integer> smap = new HashMap<>();

        for(Ladder l : map.keySet()){
            if(l != null && l.getName() != null){
                smap.put(l.getName(), map.get(l));
            }
        }

        return JSON.serialize(smap);
    }

    @Override
    public HashMap fromString(Object data) {
        if(data instanceof String){
            String s = (String) data;
            Map<String,Integer> smap = (HashMap<String,Integer>) JSON.parse(s);
            HashMap<Ladder,Integer> map = new HashMap<>();
            for(String k : smap.keySet()){
                map.put(Ladder.getLadder(k), smap.get(k));
            }
            return map;
        }
        else{
            throw new PracticeException("object data is not string?");
        }
    }
}
