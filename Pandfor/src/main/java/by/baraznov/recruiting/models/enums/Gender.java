package by.baraznov.recruiting.models.enums;

public enum Gender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String label;

    Gender(String label) {
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
