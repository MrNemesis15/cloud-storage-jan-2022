package com.geekbrains.cloud.stream;

import lombok.Data;

@Data
public class ComparableRect {

    private int width, length;

    public ComparableRect(int width, int length) {
        this.width = width;
        this.length = length;
    }

    public int compareTo(ComparableRect o) {
        if (width == o.width) {
            return length - o.length;
        }
        return width - o.width;
    }
}
