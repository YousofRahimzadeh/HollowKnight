package Yousof.HollowKnight.Enum;

public enum SupportedLanguages {

    ENGLISH("en", "English"),
    FRENCH("fr", "Francais");

    public final String code;
    public final String displayName;

    SupportedLanguages(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static SupportedLanguages fromCode(String code) {
        for (SupportedLanguages lang : values()) {
            if (lang.code.equals(code)) return lang;
        }
        return ENGLISH;
    }
}
