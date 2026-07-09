package Yousof.HollowKnight.Utils.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

import Yousof.HollowKnight.Enum.Settings;

public class AudioManager {
    private static AudioManager instance;

    private ObjectMap<String, Sound> sounds;
    private Music currentMusic;
    private Music nextMusic;

    private enum FadeState { NONE, FADING_OUT, FADING_IN }
    private FadeState fadeState = FadeState.NONE;
    
    private float fadeSpeed = 1f;
    private float maxTargetVolume = Settings.musicVolume;
    private float maxSfxVolume = Settings.sfxVolume; 
    private boolean nextMusicLoop = true;

    private AudioManager() {
        sounds = new ObjectMap<>();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void transitionToMusic(String nextFilePath, boolean loop) {
        if (!Settings.musicOn) return;

        if (fadeState != FadeState.NONE) {
            return; 
        }
        
        if (currentMusic == null || !currentMusic.isPlaying()) {
            playMusicImmediate(nextFilePath, loop);
            return;
        }

        this.nextMusic = Gdx.audio.newMusic(Gdx.files.internal(nextFilePath));
        this.nextMusicLoop = loop;
        
        this.fadeState = FadeState.FADING_OUT;
    }

    public void stopMusic(){
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
    }

    private void playMusicImmediate(String filePath, boolean loop) {
        stopMusic();
        if (!Settings.musicOn) return;
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        currentMusic.setLooping(loop);
        currentMusic.setVolume(this.maxTargetVolume);
        currentMusic.play();
        fadeState = FadeState.NONE;
    }

    public void update(float delta) {
        updateVolumeSettings();
        updateOnOffSettings();
        if (fadeState == FadeState.NONE || currentMusic == null) return;

        if (fadeState == FadeState.FADING_OUT) {
            float newVolume = currentMusic.getVolume() - (fadeSpeed * delta);
            
            if (newVolume <= 0f) {
                currentMusic.stop();
                currentMusic.dispose();
                
                currentMusic = nextMusic;
                currentMusic.setLooping(nextMusicLoop);
                currentMusic.setVolume(0f);
                currentMusic.play();
                
                nextMusic = null;
                fadeState = FadeState.FADING_IN;
            } else {
                currentMusic.setVolume(newVolume);
            }
        } 
        else if (fadeState == FadeState.FADING_IN) {
            float newVolume = currentMusic.getVolume() + (fadeSpeed * delta);
            
            if (newVolume >= maxTargetVolume) {
                currentMusic.setVolume(maxTargetVolume);
                fadeState = FadeState.NONE;
            } else {
                currentMusic.setVolume(newVolume);
            }
        }
    }

    public void playSound(String filePath) {
        if (!Settings.sfxOn) return;
        Sound sound;
        if (!sounds.containsKey(filePath)) {
            sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            sounds.put(filePath, sound);
        } else {
            sound = sounds.get(filePath);
        }

        sound.play(maxSfxVolume);
    }

    public void stopSound(String filePath) {
        Sound sound;
        if (!sounds.containsKey(filePath)) {
            sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            sounds.put(filePath, sound);
        } else {
            sound = sounds.get(filePath);
        }
        sound.stop();
    }

    public void updateVolumeSettings() {
        this.maxTargetVolume = Settings.musicVolume;
        this.maxSfxVolume = Settings.sfxVolume; 
        
        if (currentMusic != null && currentMusic.isPlaying() && fadeState == FadeState.NONE) {
            currentMusic.setVolume(this.maxTargetVolume);
        }
    }

    public void updateOnOffSettings(){
        if(!Settings.musicOn){
            if(currentMusic != null){
                currentMusic.pause();
            }
        }
        if(!Settings.sfxOn){
            sounds.forEach(s -> s.value.stop());
        }
        if(Settings.musicOn){
            if(!currentMusic.isPlaying()){
                currentMusic.play();
            }
        }
    }

    public void dispose() {
        if (currentMusic != null) currentMusic.dispose();
        if (nextMusic != null) nextMusic.dispose();
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }

}