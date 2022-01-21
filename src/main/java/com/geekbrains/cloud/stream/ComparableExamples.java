package com.geekbrains.cloud.stream;

import java.util.Comparator;
import java.util.TreeSet;

public class ComparableExamples {

    public static void main(String[] args) {

        Comparator<ComparableRect> cmp = (r1, r2) -> {
            if (r1.getWidth() == r2.getWidth()) {
                return r1.getLength() - r2.getLength();
            }
            return r1.getWidth() - r2.getWidth();
        };

        TreeSet<ComparableRect> rects = new TreeSet<>(
                Comparator.comparingInt(ComparableRect::getWidth)
                        .thenComparingInt(ComparableRect::getLength)
                        .reversed()
        );

        rects.add(new ComparableRect(1, 2));
        rects.add(new ComparableRect(3, 2));
        rects.add(new ComparableRect(4, 10));
        rects.add(new ComparableRect(4, 1));
        rects.add(new ComparableRect(4, 2));

        System.out.println(rects);

    }
}
