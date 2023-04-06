package model;

import java.util.List;

public class AuxMethods {
    public static boolean isContainedInMatrix(String element, List<List<String>> matrix) {
        for (List<String> collection : matrix) {
            if (collection.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static String getGroupInMatrix(String element, List<List<String>> matrix) {
        for (List<String> collection : matrix) {
            if (collection.contains(element)) {
                return String.valueOf(matrix.indexOf(collection));
            }
        }
        return "-1";
    }
}
