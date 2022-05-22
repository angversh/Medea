package com.example.medea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
}
