package me.collinzhang.support;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction leftOf(Direction direction) {
        Direction ret = null;
        switch (direction) {
        case UP:
            ret = Direction.LEFT;
            break;
        case DOWN:
            ret = Direction.RIGHT;
            break;
        case LEFT:
            ret = Direction.DOWN;
            break;
        case RIGHT:
            ret = Direction.UP;
            break;
        }
        return ret;
    }

    public static Direction rightOf(Direction direction) {
        Direction ret = null;
        switch (direction) {
        case UP:
            ret = Direction.RIGHT;
            break;
        case DOWN:
            ret = Direction.LEFT;
            break;
        case LEFT:
            ret = Direction.UP;
            break;
        case RIGHT:
            ret = Direction.DOWN;
            break;
        }
        return ret;
    }

    public static Direction reverseOf(Direction direction) {
        Direction ret = null;
        switch (direction) {
        case UP:
            ret = Direction.DOWN;
            break;
        case DOWN:
            ret = Direction.UP;
            break;
        case LEFT:
            ret = Direction.RIGHT;
            break;
        case RIGHT:
            ret = Direction.LEFT;
            break;
        }
        return ret;
    }
}
