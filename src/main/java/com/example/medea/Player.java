package com.example.medea;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
    public MediaPlayer mediaPlayer;
    public Media media;
    public void playMedia() {
        mediaPlayer.play();
    }
    public void stopMedia() {
        mediaPlayer.stop();
    }
    public void pauseMedia() {mediaPlayer.pause();}
}
