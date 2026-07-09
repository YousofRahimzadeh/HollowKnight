package Yousof.HollowKnight.Enum;

public enum GameMap {
    CRYSTALPEAKS("maps/CrystalPeaks/CrystalPeaks.tmx", "audio/CrystalPeaks.mp3"),
    CITYOFTEARS("maps/CityOfTears/CityOfTears.tmx", "audio/CityOfTears.mp3");

    private final String filePath;
    private final String musicPath;

    GameMap(String filePath, String musicPath) {
        this.filePath = filePath;
        this.musicPath = musicPath;
    }

    public String getFilePath() { return filePath; }
    public String getMusicPath() { return musicPath; }
}