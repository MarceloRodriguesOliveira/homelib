package com.homelib.entities;

import java.util.ArrayList;
import java.util.List;

public class GlobalStore {
    private static GlobalStore instance;
    private final List<Book> data;

    public GlobalStore() {
        data = new ArrayList<>();
    }

    public static GlobalStore getInstance() {
        if(instance == null){
            instance = new GlobalStore();
        }
        return instance;
    }

    public List<Book> getData() {
        return data;
    }

}
