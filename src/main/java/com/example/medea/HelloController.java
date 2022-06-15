package com.example.medea;
//angversh
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    //angversh
    @FXML
    private Button cycleButton;
    @FXML
    private ImageView cycleButt;
    //angversh
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ListView<String> playList;
    @FXML
    private Button nextButton;
    @FXML
    private Button prevButton;

    private ArrayList<String> playlistPaths = new ArrayList<String>();


    private boolean trackIsOn;
    double current;
    double end;

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

    private String currentPlaylist;
    private ArrayList<String> playListsNames = new ArrayList<String>();
    String urlPause = "file:src/main/resources/images/icons/pause.png";
    Image imagePause = new Image(urlPause);
    String urlPlay = "file:src/main/resources/images/icons/play.png";
    Image imagePlay = new Image(urlPlay);

    String urlCycle = "file:src/main/resources/images/icons/repeat.png";
    Image imageCycle = new Image(urlCycle);
    String urlCycleOn = "file:src/main/resources/images/icons/repeat_on.png";
    Image imageCycleOn = new Image(urlCycleOn);

    boolean backgroundLoading = true;
    private int songNumber;


    public HelloController() {
    }
    @FXML
    private void listenPlaylist(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select txt file with dir of tracks","*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();

        while (line != null) {
            File currFile = new File(line);
            String fileName = currFile.getName().replace("%20", " ").replace(".mp3", "");
            playlistPaths.add(line);
            playListsNames.add(fileName);
            line = reader.readLine();
            playList.getItems().addAll(fileName);
        }
        playTrack(playlistPaths.get(songNumber), new File(playlistPaths.get(songNumber)));

        playList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                myPlayer.stopMedia();
                String currentTrack = playList.getSelectionModel().getSelectedItem();
                System.out.println(currentTrack);
                int index = playListsNames.indexOf(currentTrack);
                System.out.println(index);
                playTrack(playlistPaths.get(index), new File(playListsNames.get(index)));
            }
        });
    }

    @FXML
    public void nextMedia(ActionEvent actionEvent) {
        if (songNumber < playlistPaths.size() - 1) {

            songNumber = songNumber + 1;

            myPlayer.stopMedia();

            if (running) {
                cancelTimer();
            }
            playTrack(playlistPaths.get(songNumber), new File(playlistPaths.get(songNumber)));
        } else {
            songNumber = 0;
            myPlayer.stopMedia();
            playTrack(playlistPaths.get(songNumber), new File(playlistPaths.get(songNumber)));
        }
    }
    @FXML
    public void prevMedia(ActionEvent actionEvent) {
        if (songNumber > 0) {

            songNumber = songNumber - 1;

            myPlayer.stopMedia();

            if (running) {
                cancelTimer();
            }
            playTrack(playlistPaths.get(songNumber), new File(playlistPaths.get(songNumber)));
        } else {
            songNumber = playlistPaths.size() - 1;
            myPlayer.stopMedia();
            playTrack(playlistPaths.get(songNumber), new File(playlistPaths.get(songNumber)));
        }
    }


    private void playTrack(String fileDir, File file){
            Media media = new Media(fileDir);
            String fileName = file.getName();
            myPlayer.mediaPlayer = new MediaPlayer(media);
            myPlayer.playMedia();
            trackIsOn = true;
            mediaView.setMediaPlayer(myPlayer.mediaPlayer);
            beginTimer();
            songNameLabel.setText(fileName.replace("%20", " ").replace(".mp3", ""));
            volumeSlider.setValue(myPlayer.mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(observable -> myPlayer.mediaPlayer.setVolume(volumeSlider.getValue() / 350));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select mp3 or mp4 file", "*.mp3", "*.mp4", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);


        String filePath = file.toURI().toString();
        System.out.println(filePath);

        if (filePath != null){
            playTrack(filePath, file);
        }
    }

    @FXML
    private void playMedia(ActionEvent event) {
        if (isPlaying) {
            myPlayer.playMedia();
            beginTimer();
            isPlaying = false;
            playButt.setImage(imagePause);

            if (isCycled) {
                isCycled = false;
                cycleTrack();
                cycleButt.setImage(imageCycle);
            }

        } else {
            myPlayer.pauseMedia();
            cancelTimer();
            isPlaying = true;
            playButt.setImage(imagePlay);
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
                current = myPlayer.mediaPlayer.getCurrentTime().toSeconds();
                int start = (int) current;
                end = myPlayer.mediaPlayer.getTotalDuration().toSeconds();
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
                    trackIsOn = false;
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
            cycleButt.setImage(imageCycle);
        } else {
            player.cycleCountProperty().set(Integer.MAX_VALUE);
            isCycled = true;
            cycleTrack();
            cycleButt.setImage(imageCycleOn);
            }
        }

    public void shuffleTrack() {
        isShuffled = !isShuffled;
    }
    //angversh

    @FXML
    private void openPlaylistCreation(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createPlaylistDialog.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create new Playlist");
            stage.getIcons().add(new Image(HelloController.class.getResourceAsStream("/images/icons/icon-medea.png")));
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

    public boolean isCycled() {
        return isCycled;
    }
}
