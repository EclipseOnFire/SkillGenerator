package com.github.eclipseonfire.skillgenerator.controllers;

import com.github.eclipseonfire.skillgenerator.models.Exporter;
import com.github.eclipseonfire.skillgenerator.models.Skill;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * The controller of the app's main window.
 *
 * @author Arthur MILLE
 */
public class MainWindow implements Initializable {

    @FXML
    private TableColumn<Skill, String> codeColumn;

    @FXML
    private TableColumn<Skill, String> labelColumn;

    @FXML
    private TableColumn<Skill, Boolean> displayColumn;

    @FXML
    private TableView<Skill> table;

    private Path lastExportDirectory = Paths.get(System.getProperty("user.home"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        this.displayColumn.setCellValueFactory(new PropertyValueFactory<>("displayed"));
        this.displayColumn.setCellFactory(skill -> new CheckBoxTableCell<>(skill::getCellObservableValue));

        this.table.getItems().addAll(Skill.getAll());
    }

    @FXML
    private void exportButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sélection de l'export");
        alert.setHeaderText("En quel format souhaitez-vous exporter les compétences ?");
        alert.setContentText("Veuillez sélectionner un des format ou bien annuler.");

        ButtonType htmlButton   = new ButtonType("HTML");
        ButtonType csvButton    = new ButtonType("CSV (Excel)");
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(htmlButton, csvButton, cancelButton);

        ButtonType clickedButton = alert.showAndWait().orElse(cancelButton);

        if (clickedButton != cancelButton) {
            boolean html = clickedButton == htmlButton;

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionnez la destination de l'export");
            chooser.setInitialDirectory(this.lastExportDirectory.toFile());
            chooser.setInitialFileName(String.format("Export compétences.%s", html ? "html" : "csv"));
            chooser.getExtensionFilters().clear();

            if (html) {
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier HTML", "*.html"));
            }
            else {
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier CSV", "*.csv"));
            }


            File d = chooser.showSaveDialog(this.table.getScene().getWindow());

            if (d != null) {
                Path              dest           = d.toPath();
                Collection<Skill> selectedSkills = this.table.getItems().filtered(Skill::getDisplayed);

                this.lastExportDirectory = dest.getParent();

                try {
                    Exporter.export(html ? Exporter.TYPE_HTML : Exporter.TYPE_CSV, selectedSkills, dest);
                }
                catch (IOException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setHeaderText("Impossible d'enregistrer votre fichier!");
                    error.setContentText(e.getLocalizedMessage());
                    error.show();
                }
            }
        }
    }

    @FXML
    private void unCheckAllButtonClicked(ActionEvent actionEvent) {
        for (Skill skill : this.table.getItems()) {
            skill.setDisplayed(false);
        }
    }

    @FXML
    private void checkAllButtonClicked(ActionEvent actionEvent) {
        for (Skill skill : this.table.getItems()) {
            skill.setDisplayed(true);
        }
    }

    @FXML
    private void tableKeyTyped(KeyEvent keyEvent) {
        if (KeyCode.ENTER.equals(keyEvent.getCode())) {
            Skill selectedItem = this.table.getSelectionModel().getSelectedItem();

            selectedItem.setDisplayed(!selectedItem.getDisplayed());
        }
    }
}
