package org.example;

import java.util.ArrayList;
import java.util.List;

public class NumberedList {
    private final List<String> list = new ArrayList<>();

    //1. corresponds to index 0 etc.
    public String getByPosition(int position) {
        return list.get(position-1);
    }
    public List<String> getList() {
        return list;
    }
    public void add(String item) {
        list.add(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : list) {
            sb.append(++i).append(". ").append(s).append("\n");
        }
        return sb.toString();
    }
}
