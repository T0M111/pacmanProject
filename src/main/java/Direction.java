public enum Direction {
    LEFT(180), RIGHT(0), UP(90), DOWN(270);

    private final int angle;

    Direction(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }
}