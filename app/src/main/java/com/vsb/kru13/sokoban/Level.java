package com.vsb.kru13.sokoban;

import java.util.Arrays;

public class Level {
    private final int[] level;

    public Level() {
        this.level = new int[100];
        Arrays.fill(this.level, Sprite.FLOOR.getNumber());
    }

    public void setLevel(String level) {
        String[] rows = level.split("\n");

        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < rows[y].length(); x++) {
                this.level[y * 10 + x] = getNumber(rows[y].charAt(x));
            }
        }
    }

    private int getNumber(char object) {
        switch (object) {
            case '#':
                return Sprite.WALL.getNumber();
            case '$':
                return Sprite.BOX.getNumber();
            case '@':
                return Sprite.PLAYER.getNumber();
            case '.':
                return Sprite.FLOOR_X.getNumber();
            default:
                return Sprite.FLOOR.getNumber();
        }
    }

    public int[] getRaw() {
        return this.level;
    }
}
