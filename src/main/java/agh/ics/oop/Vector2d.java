package agh.ics.oop;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public record Vector2d(int x, int y) {

    @Override
    public String toString() {
        return ("(" + this.x + "," + this.y + ")");
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(max(this.x, other.x), max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(min(this.x, other.x), min(this.y, other.y));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Vector2d vector2d)) return false;
        return (this.x == vector2d.x && this.y == vector2d.y);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.x, this.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }
}