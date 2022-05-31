package com.example.medea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaylistArray {

    @FXML
    private Label playlistArtist;

    @FXML
    private ImageView playlistCover;

    @FXML
    private Label playlistName;

    @FXML
    private HBox playlistlayout;
    private List<PlaylistLayout> recentlyAdded;

    public PlaylistArray(Label playlistArtist) {
    }

    public void initialize(URL location, ResourceBundle resources) throws IOException {
        recentlyAdded = new ArrayList<>(recentlyAdded());
        try {
            for(int i = 0; i < recentlyAdded.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("view.fxml"));
                HBox playlistlayout = fxmlLoader.load();
                PlaylistLayout playlistLayout = fxmlLoader.getController();
                PlaylistLayout.setData(recentlyAdded.get(i));
                playlistlayout.getChildren().add(playlistlayout);
            }
        }
        catch (IOException e) {
                e.printStackTrace();
            }
        }

    private List<PlaylistLayout> recentlyAdded() {
        List<PlaylistLayout> ls = new ArrayList<>();
        PlaylistLayout playlistLayout = new PlaylistLayout();
        playlistLayout.setName("Over It");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/1.png");
        playlistLayout.setArtist("Summer Walker");
        ls.add(playlistLayout);

        playlistLayout.setName("Leo Season");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/2.png");
        playlistLayout.setArtist("Medea Music");
        ls.add(playlistLayout);

        playlistLayout.setName("Amalia(Deluxe Version)");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/3.png");
        playlistLayout.setArtist("Doja Cat");
        ls.add(playlistLayout);

        playlistLayout.setName("Fever");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/4.png");
        playlistLayout.setArtist("Megan Thee Stallion");
        ls.add(playlistLayout);

        playlistLayout.setName("Certified Lover Boy");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/5.png");
        playlistLayout.setArtist("Drake");
        ls.add(playlistLayout);

        playlistLayout.setName("Dawn FM");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/6.png");
        playlistLayout.setArtist("The Weeknd");
        ls.add(playlistLayout);

        playlistLayout.setName("Meet The Woo");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/7.png");
        playlistLayout.setArtist("Pop Smoke");
        ls.add(playlistLayout);

        playlistLayout.setName("Queen");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/8.png");
        playlistLayout.setArtist("Nicki Minaj");
        ls.add(playlistLayout);

        playlistLayout.setName("Issa");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/9.png");
        playlistLayout.setArtist("Nicki Minaj");
        ls.add(playlistLayout);

        playlistLayout.setName("Plain Jane - Single");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/10.png");
        playlistLayout.setArtist("A$AP Ferg, Nicki Minaj");
        ls.add(playlistLayout);

        playlistLayout.setName("Ctrl");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/11.png");
        playlistLayout.setArtist("SZA");
        ls.add(playlistLayout);

        playlistLayout.setName("Hot Pink");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/12.png");
        playlistLayout.setArtist("Doja Cat");
        ls.add(playlistLayout);

        playlistLayout.setName("JACKBOYS");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/13.png");
        playlistLayout.setArtist("Jackboys, Travis Scott");
        ls.add(playlistLayout);

        playlistLayout.setName("Pups - Single");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/14.png");
        playlistLayout.setArtist("A$AP Ferg, A$AP Rocky");
        ls.add(playlistLayout);

        playlistLayout.setName("Snow Cougar");
        playlistLayout.setImageSrc("target/classes/com/example/medea/images/15.png");
        playlistLayout.setArtist("Yung Gravy");
        ls.add(playlistLayout);

        //add more

        return ls;
    }
}


