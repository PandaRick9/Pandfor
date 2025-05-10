package by.baraznov.recruiting.models.enums;

public enum EmploymentType {
    FULL_TIME("Полная занятость"),
    PART_TIME("Частичная занятость"),
    INTERNSHIP("Стажировка");

    private final String label;

    EmploymentType(String label) {
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
