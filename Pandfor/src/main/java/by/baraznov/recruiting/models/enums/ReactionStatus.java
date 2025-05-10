package by.baraznov.recruiting.models.enums;

public enum ReactionStatus {
    REJECTION("Отказ"),
    INVITATION("Приглашение"),
    NOT_VIEWED("Не просмотрено");

    private final String label;

    ReactionStatus(String label) {
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
