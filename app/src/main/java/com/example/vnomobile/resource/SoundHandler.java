package com.example.vnomobile.resource;

import android.media.AudioAttributes;
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

    private Integer bleepStreamId = null;
    private Integer sfxStreamId = null;


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
            bleepStreamId = soundPool.play(bleepId, 1, 1, 1, 0, 1);
            return;
        }
        log.warn("Can't find bleep with name {}", bleepName);
    }

    public void playSfx(String sfxName) {
        if (sfxMap.containsKey(sfxName)) {
            int sfxId = sfxMap.get(sfxName);
            sfxStreamId = soundPool.play(sfxId, 1, 1, 1, 0, 1);
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
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicFile.getPath());
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            log.error("While trying to play {}: ", trackName, e);
            return;
        }
    }

    public void shutUp() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if(bleepStreamId != null) {
            soundPool.stop(bleepStreamId);
        }
        if(sfxStreamId != null) {
            soundPool.stop(sfxStreamId);
        }
    }
}
