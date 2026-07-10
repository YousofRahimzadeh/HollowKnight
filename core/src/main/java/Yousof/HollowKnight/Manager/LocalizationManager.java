package Yousof.HollowKnight.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Enum.SupportedLanguages;

/**
 * Central localization manager for the game.
 *
 * <p>Usage:
 * <pre>
 *   String label = LocalizationManager.get(GameText.START_GAME);
 *   LocalizationManager.setLanguage(SupportedLanguages.FRENCH);
 * </pre>
 *
 * <p>The selected language is automatically persisted across game launches
 * in the LibGDX Preferences file {@value #PREFS_FILE}.
 */
public final class LocalizationManager {

    private static final String PREFS_FILE = "hollowknight_settings";
    private static final String LANG_KEY   = "language";

    /** Lazily-initialised current language (null until first access). */
    private static SupportedLanguages currentLanguage = null;

    // Static utility class — no instances.
    private LocalizationManager() {}

    // ── Initialisation ────────────────────────────────────────────────────────

    /**
     * Loads the persisted language on first access.
     * Safe to call from any thread after the LibGDX application is created.
     */
    private static void ensureInitialised() {
        if (currentLanguage != null) return;
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        String savedCode = prefs.getString(LANG_KEY, SupportedLanguages.ENGLISH.code);
        currentLanguage = SupportedLanguages.fromCode(savedCode);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Returns the localised string for {@code key} in the active language.
     * Falls back to English if the active language has no translation.
     *
     * @param key a {@link GameText} constant
     * @return the translated string, never {@code null}
     */
    public static String get(GameText key) {
        ensureInitialised();
        return key.get(currentLanguage);
    }

    /**
     * Switches the active language and immediately persists the change.
     *
     * @param language the new {@link SupportedLanguages} to activate
     */
    public static void setLanguage(SupportedLanguages language) {
        currentLanguage = language;
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        prefs.putString(LANG_KEY, language.code);
        prefs.flush();
    }

    /**
     * @return the currently active language (loads from prefs on first call)
     */
    public static SupportedLanguages getCurrentLanguage() {
        ensureInitialised();
        return currentLanguage;
    }

    /**
     * Returns the next language in the {@link SupportedLanguages} ordinal cycle.
     * Useful for a single "toggle" button that cycles through all available languages.
     *
     * @return the next {@link SupportedLanguages} after the current one
     */
    public static SupportedLanguages getNextLanguage() {
        ensureInitialised();
        SupportedLanguages[] langs = SupportedLanguages.values();
        int nextIdx = (currentLanguage.ordinal() + 1) % langs.length;
        return langs[nextIdx];
    }
}
