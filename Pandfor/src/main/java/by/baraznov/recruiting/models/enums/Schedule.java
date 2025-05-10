package by.baraznov.recruiting.models.enums;

public enum Schedule {
    FIVE_TWO("5/2"),
    TWO_TWO("2/2"),
    THREE_THREE("3/3"),
    ON_WEEKENDS("По выходным");

    private final String label;

    Schedule(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}