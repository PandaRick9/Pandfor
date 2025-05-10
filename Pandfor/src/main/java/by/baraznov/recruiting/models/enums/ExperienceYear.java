package by.baraznov.recruiting.models.enums;

public enum ExperienceYear {
    NO_EXPERIENCE       ("Нет опыта"),
    ONE_THEE_YEAR       ("От 1 до 3 лет"),
    THREE_SIX_YEAR      ("От 3 до 6 лет"),
    MORE_THE_SIX_YEAR   ("Более 6 лет"),
    IT_DOESNT_METTER    ("Не имеет значения");

    private final String label;

    ExperienceYear(String label) {
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
