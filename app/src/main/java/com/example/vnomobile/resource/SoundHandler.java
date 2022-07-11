package com.example.vnomobile.resource;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SoundHandler {

    private static SoundHandler instance;

    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private DataDirectory dataDirectory;

    //private Integer bleepStreamId = null;
    private Integer musicStreamId = null;

    private boolean loopMusic = false;

    private Integer currentMusicId = null;


    Map<String, Integer> bleepMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    Map<String, Integer> sfxMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private SoundHandler() {

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        this.soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(5)
                .build();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        this.dataDirectory = ResourceHandler.getInstance().getDirectory();

        for (File file : dataDirectory.getBleeps()) {
            int bleepId = soundPool.load(file.getPath(), 1);
            bleepMap.put(file.getName().split("\\.")[0], bleepId);
        }

        for (File file : dataDirectory.getSfxFiles()) {
            int sfxId = soundPool.load(file.getPath(), 1);
            sfxMap.put(file.getName().split("\\.")[0], sfxId);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (currentMusicId != null && currentMusicId.equals(sampleId) && status == 0) {
                    if (musicStreamId != null) {
                        soundPool.stop(musicStreamId);
                    }
                    soundPool.play(sampleId, 1, 1, 1, loopMusic ? 1 : 0, 1);
                }
            }
        });
    }

    public static SoundHandler getInstance() {
        if (instance == null) {
            instance = new SoundHandler();
        }
        return instance;
    }

    public void playBleep(String bleepName) {
        if (bleepMap.containsKey(bleepName)) {
            int bleepId = bleepMap.get(bleepName);
            soundPool.play(bleepId, 1, 1, 1, 0, 1);
            return;
        }
        log.warn("Can't find bleep with name {}", bleepName);
    }

    public void playSfx(String sfxName) {
        if (sfxMap.containsKey(sfxName)) {
            int sfxId = sfxMap.get(sfxName);
            soundPool.play(sfxId, 1, 1, 1, 0, 1);
            return;
        }
        log.warn("Cant' find sfx with name {}", sfxName);
    }

    public void playMusicTrack(String trackName, boolean isLooping) {
        File musicFile = dataDirectory.getMusicTrack(trackName);
        if (musicFile == null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            log.warn("Track {} not found", trackName);
            return;
        }
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicFile.getPath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            log.error("While trying to play {}: ", trackName, e);
            return;
        }
        mediaPlayer.start();
        currentMusicId = soundPool.load(musicFile.getPath(), 1);
    }
}