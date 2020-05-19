package icu.skitdev.melen.utils.config;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Configuration {
    Logger logger = LoggerFactory.getLogger(Configuration.class);

    private final JSONObject object;
    private final File file;

    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if (file.exists())
                this.object = new JSONReader(file).toJSONObject();
        else
            object = new JSONObject();
    }

    public void save(){
        try (JSONWriter writer = new JSONWriter(file)){
            writer.write(object);
            writer.flush();
        }catch (IOException e){
            logger.error("Impossible de sauvegarder le fichier !");
            e.printStackTrace();
        }
    }

    public String getString(String key, String defaultValue){
        if (!object.has(key)){
            object.put(key, defaultValue);
        }
        return object.getString(key);
    }
    public int getInt(String key, int defaultValue){
        if (!object.has(key)){
            object.put(key, defaultValue);
        }
        return object.getInt(key);
    }
    public long getLong(String key, long defaultValue){
        if (!object.has(key)){
            object.put(key, defaultValue);
        }
        return object.getLong(key);
    }

    public JSONArray getArray(String key, JSONArray defaultValue){
        if (!object.has(key)){
            object.put(key, defaultValue);
        }
        return object.getJSONArray(key);
    }

    public void setString(String key, String value){
        if (object.has(key))
            object.remove(key);
        object.put(key, value);
    }
}
