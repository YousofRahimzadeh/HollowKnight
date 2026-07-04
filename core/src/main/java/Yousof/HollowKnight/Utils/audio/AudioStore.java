package Yousof.HollowKnight.Utils.audio;

public enum AudioStore {
    CityOfTears("audio/CityOfTears.mp3"),
    HollowKnight("audio/HollowKnight.mp3"),
    HollowKnightFly("audio/hollow_shade_fly.wav"),
    HollowKnightFireBall("audio/hollow_knight_firewalls_produce.wav"),
    HollowKnightFocus("audio/focus_health_charging.wav"),
    HollowKnightHealthHeal("audio/focus_health_heal.wav"),
    HollowKnightSoulGain("audio/soul_pickup_5.wav"),
    HollowKnightScream("audio/hollow_knight_bulging_start_scream.wav"),
    HollowKnightSword("audio/mage_knight_sword.wav"),
    EnemyDamage("audio/enemy_damage.wav")
    ;


    public String path;
    private AudioStore(String path){
        this.path = path;
    }
}
