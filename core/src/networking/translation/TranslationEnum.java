package networking.translation;

public enum TranslationEnum {
    UUID_LIST("UUID_LIST"),
    UUID("UUID");

    private final String text;

    TranslationEnum(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
}
