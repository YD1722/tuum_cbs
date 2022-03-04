package com.tuum.cbs.helpers;


import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
    // TODO: Generics
    public static List<String> getListsDiff(List<String> list1, List<String> list2) {
        List<String> clonedList1 = new ArrayList<>(list1);
        clonedList1.removeAll(list2);
        return clonedList1;
    }
}
