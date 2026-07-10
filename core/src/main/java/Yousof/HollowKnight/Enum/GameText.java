package Yousof.HollowKnight.Enum;

/**
 * Central repository of every translatable string in the game.
 * Each constant binds an English value and a French value.
 * Retrieve strings via {@link Yousof.HollowKnight.Manager.LocalizationManager#get(GameText)}.
 */
public enum GameText {

    // ── Navigation / Common ───────────────────────────────────────────────────
    BACK("Back", "Retour"),
    CONTINUE("Continue", "Continuer"),
    SAVE_AND_EXIT("Save and Exit", "Sauvegarder et Quitter"),
    MAIN_MENU("Main Menu", "Menu Principal"),
    RESTART("Restart", "Recommencer"),

    // ── Main Menu ─────────────────────────────────────────────────────────────
    START_GAME("Start Game", "Commencer"),
    SETTINGS("Settings", "Parametres"),
    GUIDE("Guide", "Guide"),
    ACHIEVEMENTS("Achievements", "Succes"),
    QUIT_GAME("Quit Game", "Quitter le Jeu"),

    // ── Save Slots ────────────────────────────────────────────────────────────
    SELECT_JOURNEY("--- SELECT YOUR JOURNEY ---", "--- SELECTIONNEZ VOTRE AVENTURE ---"),
    NEW_GAME("New Game", "Nouvelle Partie"),
    DELETE_GAME("Delete Game", "Supprimer"),

    // ── Pause Modal ───────────────────────────────────────────────────────────
    PAUSE_HEADER("— GAME PAUSED —", "— JEU EN PAUSE —"),

    // ── Victory Modal ─────────────────────────────────────────────────────────
    VICTORY_TITLE("You Conquered the Knight!", "Vous Avez Vaincu le Chevalier!"),
    VICTORY_DEATHS("Deaths", "Deces"),
    VICTORY_ENEMIES_DEFEATED("Enemies Defeated", "Ennemis Vaincus"),
    VICTORY_TIME_ELAPSED("Time Elapsed", "Temps Ecoule"),

    // ── Settings — Section Headers ────────────────────────────────────────────
    SETTINGS_AUDIO_DISPLAY_HEADER("--- AUDIO & DISPLAY ---", "--- AUDIO & AFFICHAGE ---"),
    SETTINGS_CONTROLS_HEADER("--- CONTROLS ---", "--- CONTROLES ---"),

    // ── Settings — Audio & Display ────────────────────────────────────────────
    SETTINGS_MUSIC_VOL("Music Vol", "Volume Musique"),
    SETTINGS_SFX_VOL("SFX Vol", "Volume Effets"),
    SETTINGS_MUSIC_ON("Music: [ ON ]", "Musique: [ ON ]"),
    SETTINGS_MUSIC_OFF("Music: [ OFF ]", "Musique: [ OFF ]"),
    SETTINGS_SFX_ON("SFX: [ ON ]", "Effets: [ ON ]"),
    SETTINGS_SFX_OFF("SFX: [ OFF ]", "Effets: [ OFF ]"),
    SETTINGS_BRIGHTNESS("Brightness", "Luminosite"),
    SETTINGS_LANGUAGE("Language", "Langue"),
    SETTINGS_RESET_AUDIO("Reset Audio", "Reinitialiser Audio"),

    // ── Settings — Controls ───────────────────────────────────────────────────
    SETTINGS_RESET_CONTROLS("Reset Controls", "Reinitialiser Controles"),
    SETTINGS_KEY_PRESS_HINT("[ Press Any Key... ]", "[ Appuyez sur une touche... ]"),

    // ── Key Binding Labels (Controls Grid) ────────────────────────────────────
    KEY_RIGHT("Move Right", "Droite"),
    KEY_LEFT("Move Left", "Gauche"),
    KEY_LOOK_UP("Look Up", "Regarder en Haut"),
    KEY_LOOK_DOWN("Look Down", "Regarder en Bas"),
    KEY_JUMP("Jump", "Sauter"),
    KEY_ATTACK("Attack", "Attaquer"),
    KEY_FOCUS("Focus / Heal", "Concentration"),
    KEY_VENGEFUL("Vengeful Spirit", "Esprit Vengeur"),
    KEY_SCREAM("Howling Wraiths", "Cris Hurlants"),
    KEY_DASH("Dash", "Esquive"),

    // ── Guide — Interactive Section ───────────────────────────────────────────
    GUIDE_INTERACTIVE_HEADER("--- INTERACTIVE CHARACTER TEST ---", "--- TEST DE PERSONNAGE INTERACTIF ---"),
    GUIDE_INTERACTIVE_HINT(
        "[ Press & Hold your movement/action keys to animate the Knight ]",
        "[ Maintenez vos touches de deplacement/action pour animer le Chevalier ]"),

    // ── Guide — Abilities & Mechanics ─────────────────────────────────────────
    GUIDE_ABILITIES_HEADER("--- KNIGHT ABILITIES & MECHANICS ---", "--- CAPACITES DU CHEVALIER ---"),
    GUIDE_HEALTH_TITLE("[ Health System - Masks ]", "[ Systeme de Sante - Masques ]"),
    GUIDE_HEALTH_DESC(
        "Your life is measured in Masks. Holding the FOCUS key consumes collected SOUL to focus and repair broken masks safely.",
        "Votre vie est mesuree en Masques. Maintenir la touche FOCUS consomme l'AME collectee pour reparer les masques brises en toute securite."),
    GUIDE_SOUL_TITLE("[ SOUL Orb Gathering ]", "[ Collecte de l'Orbe d'Ame ]"),
    GUIDE_SOUL_DESC(
        "Strike enemies with your Nail weapon (SLASH) to harvest and fill the SOUL Orb. SOUL is required for spells and healing.",
        "Frappez les ennemis avec votre Clou (SLASH) pour collecter et remplir l'Orbe d'Ame. L'AME est necessaire pour les sorts et les soins."),
    GUIDE_COMBAT_TITLE("[ Core Abilities ]", "[ Capacites Essentielles ]"),
    GUIDE_COMBAT_DESC(
        "Dash: Tap the dash key for a quick horizontal dodge.\nNail Slash: Strike enemies to deal damage or down-slash to bounce (Pogo).",
        "Esquive: Appuyez sur la touche d'esquive pour une derobade rapide.\nFrappe du Clou: Frappez les ennemis ou frappez vers le bas pour rebondir (Pogo)."),

    // ── Guide — Cheat Codes ───────────────────────────────────────────────────
    GUIDE_CHEATS_HEADER("--- AVAILABLE CHEAT CODES ---", "--- CODES DE TRICHE DISPONIBLES ---"),
    CHEAT_DESC_BOSS_TELEPORT(
        "Teleport instantly to the False Knight boss arena.",
        "Teleportation instantanee vers l'arene du Faux Chevalier."),
    CHEAT_DESC_NOCLIP(
        "Fly through map constraints without animations or gravity.",
        "Voler a travers la carte sans animations ni gravite."),
    CHEAT_DESC_EMERGENCY_HEAL(
        "Instantly restore all Masks to full health.",
        "Restaurer instantanement tous les Masques en pleine sante."),
    CHEAT_DESC_REFILL_SOUL(
        "Immediately refill the SOUL Orb to maximum.",
        "Remplir immediatement l'Orbe d'Ame au maximum."),
    CHEAT_DESC_GOD_MODE(
        "Toggle invincibility — take no damage from any source.",
        "Activer l'invincibilite - ne subir aucun degat."),
    CHEAT_DESC_INSTA_KILL(
        "One-shot every enemy on contact.",
        "Eliminer instantanement chaque ennemi au contact."),

    // ── Achievements ─────────────────────────────────────────────────────────
    ACHIEVEMENTS_HALL_HEADER("--- HALL OF ACHIEVEMENTS ---", "--- SALLE DES SUCCES ---"),
    ACHIEVEMENTS_HEADER("--- YOUR ACHIEVEMENTS ---", "--- VOS SUCCES ---"),
    ACHIEVEMENTS_LOCKED("Locked", "Verrouille"),
    ACHIEVEMENTS_UNLOCKED("Unlocked", "Deverrouille"),

    // ── Inventory / Charms ────────────────────────────────────────────────────
    INVENTORY_EQUIPPED("Equipped", "Equipes"),
    INVENTORY_CHARMS("Charms", "Charmes"),
    INVENTORY_SELECT_CHARM("Select a Charm", "Selectionner un Charme"),
    INVENTORY_CLICK_HINT("Click to equip or unequip.\nHover to see details.",
        "Cliquer pour equiper ou desequiper.\nSurvoler pour voir les details."),
    INVENTORY_NOTCHES_FULL("Notches Full!", "Encoches Pleines!"),
    INVENTORY_UNEQUIP_FIRST("You must unequip a charm first.",
        "Vous devez d'abord desequiper un charme."),

    // ── Save Slots ────────────────────────────────────────────────────────────
    SAVE_SLOT_GAME("Game", "Partie"),
    SAVE_SLOT_MASKS("Masks", "Masques"),
    SAVE_SLOT_SOUL("Soul", "Ame");

    // ─────────────────────────────────────────────────────────────────────────

    private final String english;
    private final String french;

    GameText(String english, String french) {
        this.english = english;
        this.french = french;
    }

    /**
     * Returns the translation for the given language.
     * Falls back to English for any unrecognised locale.
     */
    public String get(SupportedLanguages lang) {
        if (lang == SupportedLanguages.FRENCH) return french;
        return english;
    }
}
