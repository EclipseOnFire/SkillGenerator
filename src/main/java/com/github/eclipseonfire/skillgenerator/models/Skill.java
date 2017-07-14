package com.github.eclipseonfire.skillgenerator.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A Skill.
 *
 * @author Arthur MILLE
 */
public class Skill {

    private final StringProperty  code      = new SimpleStringProperty();
    private final StringProperty  label     = new SimpleStringProperty();
    private final BooleanProperty displayed = new SimpleBooleanProperty();

    public Skill(String code, String label, boolean displayed) {
        Objects.requireNonNull(code, "The parameter code cannot be null!");
        Objects.requireNonNull(label, "The parameter label cannot be null!");

        this.code.set(code);
        this.label.set(label);
        this.displayed.set(displayed);
    }

    public boolean getDisplayed() {
        return displayed.get();
    }

    public void setDisplayed(boolean displayed) {
        this.displayed.set(displayed);
    }

    public BooleanProperty displayedProperty() {
        return displayed;
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        Objects.requireNonNull(code, "The parameter code cannot be null!");

        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String label) {
        Objects.requireNonNull(label, "The parameter label cannot be null!");

        this.label.set(label);
    }

    public StringProperty labelProperty() {
        return label;
    }

    public static Collection<Skill> getAll() {
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(Skill.class.getResourceAsStream(CSV_FILE), StandardCharsets.UTF_8)
                     )
        ) {

            List<Skill> skills = new ArrayList<>();
            String      line;

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");

                skills.add(new Skill(split[0], split[1], false));
            }

            return skills;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String CSV_FILE = "/skills.csv";
}