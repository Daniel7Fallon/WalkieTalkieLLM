package org.example;

import java.util.List;

public class NumberedList {
    private final List<String> list;

    public NumberedList(List<String> list) {
        this.list = list;
    }

    //1. corresponds to index 0 etc.
    public String getByPosition(int position) {
        return list.get(position-1);
    }

    public List<String> getList() {
        return list;
    }
}
