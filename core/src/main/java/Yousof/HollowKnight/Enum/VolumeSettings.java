package Yousof.HollowKnight.Enum;

public enum VolumeSettings {
    MUSIC(1f , true),
    SFX(1f , true);

    private float volume;
    private boolean on;

    private VolumeSettings(float volume , boolean on){
        this.volume = volume;
        this.on = on;
    }


    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isOn() {
        return on;
    }


    public void setOn(boolean on) {
        this.on = on;
    }

}
