/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ulitiJav;

/**
 *
 * @author Administrator
 */
import java.util.HashMap;
import java.util.Map;

public class DataHolder {

    private static DataHolder instance;
    private Map<String, Object> dataMap;

    private DataHolder() {
        dataMap = new HashMap<>();
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public <T> void setObject(String key, T value) {
        dataMap.put(key, value);
    }

    public <T> T getObject(String key, Class<T> type) {
        Object value = dataMap.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public void setString(String key, String value) {
        dataMap.put(key, value);
    }

    public String getString(String key) {
        Object value = dataMap.get(key);
        if (value != null && value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public void clearData(String key) {
        dataMap.remove(key);
    }

    public void clearAllData() {
        dataMap.clear();
    }
}

