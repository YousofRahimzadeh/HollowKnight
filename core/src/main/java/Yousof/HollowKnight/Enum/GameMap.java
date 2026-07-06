package Yousof.HollowKnight.Enum;

public enum GameMap {
    TEST("untitled.tmx", "audio/CityOfTears.mp3"),
    FORGOTTEN_CROSSROADS("maps/crossroads.tmx", "audio/crossroads.mp3"),
    GREENPATH("maps/greenpath.tmx", "audio/greenpath.mp3");

    private final String filePath;
    private final String musicPath;

    GameMap(String filePath, String musicPath) {
        this.filePath = filePath;
        this.musicPath = musicPath;
    }

    public String getFilePath() { return filePath; }
    public String getMusicPath() { return musicPath; }
}