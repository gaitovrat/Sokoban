package com.vsb.kru13.sokoban;

public enum Sprite {
    FLOOR(0),
    WALL(1),
    BOX(2),
    FLOOR_X(3),
    PLAYER(4),
    GREEN_BOX(5);

    private final int number;

    Sprite(int number) {
        this.number = number;
    }

    int getNumber() {
        return this.number;
    }
}
