package com.wms.base.api;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;

@SuppressLint("CommitPrefEdits")
final public class SpAPI {

    private static SharedPreferences prefs;

    private SpAPI() {

    }

    private static class VH {
        static SpAPI spApi = new SpAPI();
    }

    public static SpAPI Instance() {
        return VH.spApi;
    }

    public static void init(Context context, String prefsname) {
        prefs = context.getSharedPreferences(prefsname, Context.MODE_MULTI_PROCESS);
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return prefs.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return prefs.getString(key, defaultVal);
    }

    public String getString(String key) {
        return prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return prefs.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return prefs.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return prefs.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return prefs.getStringSet(key, defaultVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key) {
        return prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return prefs.getAll();
    }

    public boolean exists(String key) {
        return prefs.contains(key);
    }


    public SpAPI putString(String key, String value) {
        prefs.edit().putString(key, value);
        prefs.edit().apply();
        return this;
    }

    public SpAPI putInt(String key, int value) {
        prefs.edit().putInt(key, value);
        prefs.edit().apply();
        return this;
    }

    public SpAPI putFloat(String key, float value) {
        prefs.edit().putFloat(key, value);
        prefs.edit().apply();
        return this;
    }

    public SpAPI putLong(String key, long value) {
        prefs.edit().putLong(key, value);
        prefs.edit().apply();
        return this;
    }

    public SpAPI putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value);
        prefs.edit().apply();
        return this;
    }

    public void commit() {
        prefs.edit().apply();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SpAPI putStringSet(String key, Set<String> value) {
        prefs.edit().putStringSet(key, value);
        prefs.edit().apply();
        return this;
    }

    public void putObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            prefs.edit().putString(key, objectVal);
            prefs.edit().apply();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (prefs.contains(key)) {
            String objectVal = prefs.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bais.close();
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public SpAPI remove(String key) {
        prefs.edit().remove(key);
        prefs.edit().apply();
        return this;
    }

    public SpAPI removeAll() {
        prefs.edit().clear();
        prefs.edit().apply();
        return this;
    }

}
