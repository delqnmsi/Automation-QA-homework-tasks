package web.enums;

public enum ResultsLanguage {
    ENGLISH("English"),
    ITALIAN("Italian"),
    GERMAN("German");

    private final String language;

    ResultsLanguage(String displayName) {
        this.language = displayName;
    }

    public String getLanguage() {
        return language;
    }
}
