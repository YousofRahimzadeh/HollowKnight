package Yousof.HollowKnight.Enum;

public enum ZoteVoices {
    MUMBLE_1("audio/Zote_01.wav"),
    MUMBLE_2("audio/Zote_02.wav"),
    MUMBLE_3("audio/Zote_03.wav"),
    MUMBLE_4("audio/Zote_04.wav"),
    MUMBLE_5("audio/Zote_05.wav");

    private final String filePath;

    ZoteVoices(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}