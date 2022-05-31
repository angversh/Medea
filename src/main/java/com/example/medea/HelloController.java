package com.example.medea;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    Player myPlayer = new Player();
    @FXML
    private Button playButton;
    @FXML
    private Button chooseFile;
    @FXML
    private Button pauseButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label startLabel;
    @FXML
    private Label finalLabel;


    private Timer timer;
    private TimerTask task;
    private boolean running;
    private Media media;
    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;




    @FXML
    private ProgressBar songProgressBar;

    public HelloController() {
    }



    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select mp3 file", "*.mp3");
        fileChooser.getExtensionFilters().add(filter);

        String filePath = fileChooser.showOpenDialog(null).toURI().toString();

        if (filePath != null){
            Media media = new Media(filePath);
            myPlayer.mediaPlayer = new MediaPlayer(media);
            myPlayer.mediaPlayer.play();
            beginTimer();
            volumeSlider.setValue(myPlayer.mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    myPlayer.mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                }
            });
        }
    }

    @FXML
    private void playMedia(ActionEvent event) {
        myPlayer.playMedia();
        beginTimer();
    }

    @FXML
    private void stopMedia(ActionEvent event) {
        myPlayer.stopMedia();
        songProgressBar.setProgress(0);
    }

    @FXML
    private void pauseMedia(ActionEvent event) {
        myPlayer.pauseMedia();
        cancelTimer();
    }

    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = myPlayer.mediaPlayer.getCurrentTime().toSeconds();
                int start = (int) current;
                double end = myPlayer.mediaPlayer.getTotalDuration().toSeconds();
                int finish = (int) end;
                double a = myPlayer.mediaPlayer.getCurrentTime().toSeconds();
                double b = myPlayer.mediaPlayer.getTotalDuration().toSeconds();

                int minutesFinish = finish / 60;
                int secondsFinish = finish - 60 * minutesFinish;
                Platform.runLater(() -> {
                    startLabel.setText("0:0" + start);
                    if (start >= 10) {
                        startLabel.setText("0:" + start);
                    }
                    if (start >= 60) {
                        int minutesStart = start / 60;
                        int currentSeconds = (start - 60 * minutesStart);
                        startLabel.setText(minutesStart + ":" + currentSeconds);
                        if (currentSeconds < 10) {
                            startLabel.setText(minutesStart + ":0" + currentSeconds);
                        }
                    }
                    finalLabel.setText(minutesFinish + ":" + secondsFinish);
                });
                songProgressBar.setProgress(current/end);

                if (current/end == 1) {
                    cancelTimer();
                }
            }

        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }
}
