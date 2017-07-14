package com.github.eclipseonfire.skillgenerator.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

/**
 * A utility class for JavaFX.
 *
 * @author Arthur MILLE
 */
public final class FXUtils {

    private static final String VIEWS_URL = "/com/github/eclipseonfire/skillgenerator/views/";

    private FXUtils() {}

    public static Parent attachedViewParent(Class<?> controller) {
        String url = VIEWS_URL + controller.getSimpleName() + ".fxml";

        try {
            return FXMLLoader.load(FXUtils.class.getResource(url));
        }
        catch (NullPointerException e) {
            throw new RuntimeException(String.format("The resource '%s' was not found!", url));
        }
        catch (IOException e) {
            throw new RuntimeException(String.format("Unable to load '%s' resource!", url), e);
        }
    }
}