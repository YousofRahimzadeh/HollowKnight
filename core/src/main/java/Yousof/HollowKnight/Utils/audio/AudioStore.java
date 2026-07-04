package Yousof.HollowKnight.Utils.audio;

public enum AudioStore {
    CityOfTears("audio/CityOfTears.mp3"),
    HollowKnight("audio/HollowKnight.mp3"),
    HollowKnightFly("audio/hollow_shade_fly.wav"),
    HollowKnightFireBall("audio/hollow_shade_fireball.wav"),
    HollowKnightSword("audio/hollow_shade_sword.wav")
    ;


    public String path;
    private AudioStore(String path){
        this.path = path;
    }
}
