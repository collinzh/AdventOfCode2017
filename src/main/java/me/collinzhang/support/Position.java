package me.collinzhang.support;

public class Position {

    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position move(Direction direction) {
        int nX = x;
        int nY = y;
        switch (direction) {

        case UP:
            nY--;
            break;
        case DOWN:
            nY++;
            break;
        case LEFT:
            nX--;
            break;
        case RIGHT:
            nX++;
            break;
        }

        return new Position(nX, nY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Position))
            return false;

        Position position = (Position) o;

        if (x != position.x)
            return false;
        return y == position.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
