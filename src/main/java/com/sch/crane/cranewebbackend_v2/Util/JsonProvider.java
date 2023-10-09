package com.sch.crane.cranewebbackend_v2.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProvider {

    ObjectMapper mapper = new ObjectMapper();


    public String dtoToJson(Object obj){
        if(obj == null) return null;

        String res = null;

        try{
            res = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return res;
    }


}
