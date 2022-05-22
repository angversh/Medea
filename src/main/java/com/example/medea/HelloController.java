package com.example.medea;

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

    @FXML
    private HBox cardLayout;

    @FXML
    private Button addFileBtn;

    @FXML
    private void addFile(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addFileDialog.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add new track");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Cant load addFileDialog.fxml");
        }
    }
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
    private Label finalLabel;


    private Timer timer;
    private TimerTask task;
    private boolean running;
    private Media media;
    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;



    private MediaPlayer mediaPlayer;

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
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            beginTimer();
            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                }
            });
        }
    }

    @FXML
    private void playMedia(ActionEvent event) {
        mediaPlayer.play();
        beginTimer();
    }

    @FXML
    private void stopMedia(ActionEvent event) {
        mediaPlayer.stop();
        songProgressBar.setProgress(0);
    }

    @FXML
    private void pauseMedia(ActionEvent event) {
        mediaPlayer.pause();
        cancelTimer();
    }

    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
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
