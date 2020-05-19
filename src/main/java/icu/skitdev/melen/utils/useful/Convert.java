package icu.skitdev.melen.utils.useful;

public class Convert {
    public static int getInt(String value){
        try {
            return Integer.parseInt(value);
        }catch(Exception ignored){ }
        return 0;
    }
    public static long getLong(String value){
        try {
            return Long.parseLong(value);
        }catch(Exception ignored){ }
        return 0;
    }
    public static float getFloat(String value){
        try {
            return Float.parseFloat(value);
        }catch(Exception ignored){ }
        return 0;
    }
}
