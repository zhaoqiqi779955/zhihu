package utils;

import java.util.UUID;

public class GUID {
    public static String genId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
