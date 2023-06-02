package fr.amu.iut.cc3;

import javafx.scene.shape.Circle;

import java.util.Comparator;

public class CircleCompartor implements Comparator<Circle> {

    // override the compare() method
    public int compare(Circle c1, Circle c2) {
        int id1 = Integer.parseInt(c1.getId().charAt(c1.getId().length()-1) + "");
        int id2 = Integer.parseInt(c2.getId().charAt(c2.getId().length()-1) + "");
        if (id1 == id2)
            return 0;
        else if (id1 < id2)
            return -1;
        else
            return 1;
    }
}