package model;

import java.util.Collection;

public class AuxMethods {
    public static boolean isContainedInMatrix(String element, Collection<Collection<String>> matrix) {
        for (Collection<String> collection : matrix) {
            if (collection.contains(element)) {
                return true;
            }
        }
        return false;
    }
}
