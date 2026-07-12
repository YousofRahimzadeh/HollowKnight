package Yousof.HollowKnight.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Enum.SupportedLanguages;

public final class LocalizationManager {

    private static final String PREFS_FILE = "hollowknight_settings";
    private static final String LANG_KEY   = "language";

    private static SupportedLanguages currentLanguage = null;

    private LocalizationManager() {}

    // ── Initialisation ────────────────────────────────────────────────────────
    private static void ensureInitialised() {
        if (currentLanguage != null) return;
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        String savedCode = prefs.getString(LANG_KEY, SupportedLanguages.ENGLISH.code);
        currentLanguage = SupportedLanguages.fromCode(savedCode);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public static String get(GameText key) {
        ensureInitialised();
        return key.get(currentLanguage);
    }

    public static void setLanguage(SupportedLanguages language) {
        currentLanguage = language;
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        prefs.putString(LANG_KEY, language.code);
        prefs.flush();
    }


    public static SupportedLanguages getCurrentLanguage() {
        ensureInitialised();
        return currentLanguage;
    }

    public static SupportedLanguages getNextLanguage() {
        ensureInitialised();
        SupportedLanguages[] langs = SupportedLanguages.values();
        int nextIdx = (currentLanguage.ordinal() + 1) % langs.length;
        return langs[nextIdx];
    }
}
