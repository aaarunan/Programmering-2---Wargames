package edu.ntnu.arunang.wargames.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;

/**
 * Main GUI class of the JavaFX application. Contains helper methods and boilder plate code
 * for use in the controllers, and verification of FXML loaders.
 */

public class GUI extends Application {

    private static final int STAGE_MIN_HEIGHT = 400;
    private static final int STAGE_MIN_WIDTH = 600;

    /**
     * Sets a scene by getting the current stage from a given node
     * and replacing the scene. The paths default is /resources.
     *
     * @param node that can extract the current stage
     * @param page fxml page from /resources/gui
     */

    public static void setSceneFromNode(Node node, String page) {
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();

        Scene prev = node.getScene();
        loader.setLocation(getPath(page));
        Parent root = GUI.checkFXMLLoader(loader);
        Scene scene = new Scene(root, prev.getWidth(), prev.getHeight());

        stage.setScene(scene);
    }

    /**
     * Create a new stage (window). The path default is /resources.
     *
     * @param page fxml page from /resources/gui
     */

    public static void createNewStage(String page) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getPath(page));
        stage.setScene(new Scene(checkFXMLLoader(loader)));
        stage.show();
    }

    /**
     * Change the scene of a given stage. The path default is /resources.
     *
     * @param stage stage that is switching scene
     * @param page  fxml page from /recources/gui
     */

    public static void setSceneFromStage(Stage stage, String page) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getPath(page));
        stage.setScene(new Scene(checkFXMLLoader(loader)));
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.setMinWidth(STAGE_MIN_WIDTH);
        stage.setMaximized(true);

        stage.show();
    }

    /**
     * Set the scene of the stage by extracting the stage from an event.
     * This might be a better solution if there is no node to extract the stage from.
     *
     * @param actionEvent a javafx event
     * @param page        the page from /src/resources/gui directory
     */

    public static void setSceneFromActionEvent(ActionEvent actionEvent, String page) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getPath(page));
        Parent root = checkFXMLLoader(loader);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene prev = ((Node) actionEvent.getSource()).getScene();
        stage.setScene(new Scene(root, prev.getWidth(), prev.getHeight()));
        stage.show();
    }

    /**
     * This is a helper method that checks the loader for exceptions and returns
     * the Parent if successful. This is for easy troubleshooting.
     *
     * @param loader the FXMLloader that is being checked
     * @return the loaded Parent
     */

    protected static Parent checkFXMLLoader(FXMLLoader loader) {
        Parent root = null;

        try {
            root = loader.load();
        } catch (java.io.IOException e) {
            System.out.println("Could not load XML file... Check the controller class for " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println("Most likely, you mistyped the fxml resource path that you tried to load.");
            System.out.println("Remember to add / in the beginning of the path and give the path relative to the resources folder.");
            System.out.println("\n" + e.getClass() + ": " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("A different, unexpected exception was thrown while loading the FXML file...\n\n" + e.getClass() + ": " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        }
        return root;
    }

    public static URL getPath(String page) {
        try {
            return FileSystems.getDefault().getPath("src", "main", "resources", "gui", page + ".fxml").toUri().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("Could not make URL from '%s'", page));
        }
    }

    /**
     * Main method of the GUI, and starts the application by applying the mainpage
     * to the primaryStage.
     *
     * @param primaryStage primary window of the application
     */

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("/gui/media/logo.png"))));
        primaryStage.setTitle("Wargames");

        setSceneFromStage(primaryStage, "main");
    }
}
