package web.enums;

public enum BookType {
    PAPERBACK("Paperback"),
    KINDLE_EDITION_WITH_AUDIO_VIDEO("Kindle Edition with Audio/Video"),
    HARDCOVER("Hardcover"),
    AUDIOBOOK("Audiobook"),
    KINDLE_EDITION("Kindle Edition"),
    MASS_MARKET_PAPERBACK("Mass Market Paperback");

    private final String bookEdition;

    BookType(String displayName) {
        this.bookEdition = displayName;
    }

    public String getBookType() {
        return bookEdition;
    }
}
