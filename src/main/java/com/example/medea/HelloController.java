package com.example.medea;
//angversh
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.util.*;
import java.io.File;
//angversh
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {

    Player myPlayer = new Player();

    @FXML
    public TextField newPlaylistName;
    @FXML
    private Button playButton;
    @FXML
    private Button chooseFile;
    @FXML
    public Button createPlaylist;
    @FXML
    public Button openPlaylist;
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
    @FXML
    private ProgressBar songProgressBar;

    private Timer timer;
    private TimerTask task;
    private boolean running;
    private boolean repeated;
    private File directory;
    //angversh
    private boolean isShuffled;
    private boolean isCycled;
    private MediaPlayer player;
    //angversh
    private File[] files;
    private boolean isPlaying;
    private int count = 0;
    private String currentPlaylist;
    String urlPause = "file:src/main/resources/images/icons/pause.png";
    Image imagePause = new Image(urlPause);
    String urlPlay = "file:src/main/resources/images/icons/play.png";
    Image imagePlay = new Image(urlPlay);

    boolean backgroundLoading = true;

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
        if (isPlaying) {
            myPlayer.playMedia();
            beginTimer();
            isPlaying = false;
            playButt.setImage(imagePause);

        } else {
            myPlayer.pauseMedia();
            cancelTimer();
            isPlaying = true;
            playButt.setImage(imagePlay);
            //angversh
            if (isCycled) {
                isCycled = false;
                cycleTrack();
            }
            //angversh
        }
    }

    @FXML
    private void repeat(ActionEvent event){

    }
    @FXML
    private void plusFiveSec(ActionEvent event){

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
    //angversh
    public void cycleTrack() {
        if (isCycled) {
            isCycled = false;
            player.cycleCountProperty().set(0);
        } else {
            player.cycleCountProperty().set(Integer.MAX_VALUE);
            isCycled = true;
        }
    }
    public void shuffleTrack() {
        isShuffled = !isShuffled;
    }
    public boolean isCycled() {
        return isCycled;
    }
    //angversh
    @FXML
    private void openPlaylistCreation(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createPlaylistDialog.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create new Playlist");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Can't load 'create playlist' window");
        }
    }

    @FXML
    private void createPlaylist(ActionEvent event) {
        Pattern newPlaylistNamePattern = Pattern.compile("\\w+");
        Matcher newPlaylistNameMatcher = newPlaylistNamePattern.matcher(newPlaylistName.getText());
        if (newPlaylistNameMatcher.matches()){
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select a directory for your new playlist");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File dir = directoryChooser.showDialog(null);
            if (dir != null) {
                String newPlaylistPath = dir.getAbsolutePath() + "'" + newPlaylistName.getText()  + ".txt";
                newPlaylistPath = newPlaylistPath.replaceAll("'", "\\\\");
                FileChooser TracksFileChooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select mp3 or mp4 file", "*.mp3", "*.mp4");
                TracksFileChooser.getExtensionFilters().add(filter);
                List<File> TracksPaths = TracksFileChooser.showOpenMultipleDialog(null);
                try {
                    File newPlaylist = new File(newPlaylistPath);
                    if (newPlaylist.createNewFile())
                        System.out.println("New playlist created");
                    else
                        System.out.println("This playlist already exists");
                }
                catch (Exception e) {
                    System.err.println(e);
                }
                try {
                    FileWriter writer = new FileWriter(newPlaylistPath);
                    for (File track : TracksPaths) {
                        String trackPath = track.toURI().toString();
                        writer.write(trackPath + System.getProperty("line.separator"));
                    }
                    writer.close();
                }
                catch (Exception e) {
                    System.err.println(e);
                }
                currentPlaylist = newPlaylistPath;
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        }
    }
}
