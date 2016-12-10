package com.cheddy.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author : Cheddy
 */
public final class Data {

    private String lastUsedFile = null;

    private static final Data INSTANCE;

    static {
        byte[] bytes = null;
        try {
            if(Constants.DATA_FILE.exists()) {
                bytes = Files.readAllBytes(Constants.DATA_FILE.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bytes != null) {
            Gson gson = new GsonBuilder().setLenient().create();
            INSTANCE = gson.fromJson(new String(bytes, Constants.DEFAULT_CHARSET), Data.class);
        }else{
            INSTANCE = new Data();
        }
    }

    private Data() {

    }

    public static Data instance() {
        return INSTANCE;
    }

    public static void save(){
        Gson gson = new Gson();
        try {
            Files.write(Constants.DATA_FILE.toPath(), gson.toJson(instance()).getBytes(Constants.DEFAULT_CHARSET));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public String lastUsedFile() {
        return lastUsedFile;
    }

    public void setLastUsedFile(String lastUsedFile) {
        this.lastUsedFile = lastUsedFile;
    }
}
