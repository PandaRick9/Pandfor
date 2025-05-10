package by.baraznov.recruiting.models.enums;


public enum SkillLevel {
    BASIC("Базовый"),
    INTERMEDIATE("Средний"),
    ADVANCED("Продвинутый");

    private final String label;

    SkillLevel(String label) {
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

