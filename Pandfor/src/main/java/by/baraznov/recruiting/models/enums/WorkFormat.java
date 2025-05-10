package by.baraznov.recruiting.models.enums;

public enum WorkFormat {
    REMOTELY("Удаленно"),
    HYBRID("Гибридный формат"),
    EMPLOYER_PLACE("В офисе");

    private final String label;

    WorkFormat(String label) {
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
