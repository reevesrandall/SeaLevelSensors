package com.google.ar.sceneform.samples.hellosceneform.Model;

import java.util.ArrayList;
import java.util.List;

public class StormList {
    private static List<Storm> list;

    static {
        list = new ArrayList<>();
        list.add(new Storm("Custom"));
        list.add(new Storm("Irma"));
        list.add(new Storm("Matthew"));
        list.add(new Storm("Michael"));
    }

    public static Storm getStorm(int index) {
        return list.get(index);
    }

    public static void addStorm(Storm storm) {
        list.add(storm);
    }
}
