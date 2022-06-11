package com.example.medea;

import javafx.scene.media.MediaPlayer;

public class Player {
    public MediaPlayer mediaPlayer;
    public void playMedia() {
        mediaPlayer.play();
    }
    public void stopMedia() {
        mediaPlayer.stop();
    }
    public void pauseMedia() {
        mediaPlayer.pause();
    }
}
