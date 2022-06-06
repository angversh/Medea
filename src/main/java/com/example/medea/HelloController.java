package com.example.medea;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.DoublePredicate;

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
    @FXML
    private Label songNameLabel;
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider sceneSlider;
    @FXML
    private ImageView playButt;



    private Timer timer;
    private TimerTask task;
    private boolean running;
    private File directory;
    private File[] files;
    private int count = 0;

    @FXML
    private ProgressBar songProgressBar;

    public HelloController() {
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select mp3 or mp4 file", "*.mp3", "*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);

        String filePath = file.toURI().toString();
        String fileName = file.getName();

        if (filePath != null){
            Media media = new Media(filePath);
            myPlayer.mediaPlayer = new MediaPlayer(media);
            myPlayer.playMedia();
            mediaView.setMediaPlayer(myPlayer.mediaPlayer);
            //DoubleProperty width = mediaView.fitWidthProperty();
            //DoubleProperty height = mediaView.fitHeightProperty();
            //width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            //height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            beginTimer();
            songNameLabel.setText(fileName);
            volumeSlider.setValue(myPlayer.mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(observable -> myPlayer.mediaPlayer.setVolume(volumeSlider.getValue() / 350));
        }
    }

    @FXML
    private void playMedia(ActionEvent event) {
        if (count % 2 == 0){
            myPlayer.playMedia();
            beginTimer();
            count += 1;
        }
        else {
            myPlayer.pauseMedia();
            cancelTimer();
            count += 1;
        }
    }

    @FXML
    private void pauseMedia(ActionEvent event) {
        myPlayer.pauseMedia();
        cancelTimer();
    }

    private void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = myPlayer.mediaPlayer.getCurrentTime().toSeconds();
                int start = (int) current;
                double end = myPlayer.mediaPlayer.getTotalDuration().toSeconds();
                sceneSlider.setMax(end);
                int finish = (int) end;
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
                sceneSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        myPlayer.mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    private void cancelTimer(){
        running = false;
        timer.cancel();
    }
}
