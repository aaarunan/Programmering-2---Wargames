package edu.ntnu.arunang.wargames.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main GUI class of the JavaFX application. Contains helper methods and boilder plate code for use in the controllers,
 * and verification of FXML loaders.
 * <p>
 * code was initially taken from the quizmaker application of k2g2 found here:
 * https://gitlab.stud.idi.ntnu.no/idatt1002-2022-k2-02/idatt1002_2022_k2_02/-/blob/dev/quizmaker/src/main/java/org/ntnu/k2/g2/quizmaker/gui/GUI.java
 */

public class GUI extends Application {

    private static final int STAGE_MIN_HEIGHT = 475;
    private static final int STAGE_MIN_WIDTH = 625;

    /**
     * Sets a scene by getting the current stage from a given node and replacing the scene. The paths default is
     * /resources.
     *
     * @param node that can extract the current stage
     * @param page fxml page from /resources/gui
     */

    public static void setSceneFromNode(Node node, String page) {
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = initLoader(getPath(page));

        Scene prev = node.getScene();
        loader.setLocation(getPath(page));
        Parent root = loader.getRoot();
        Scene scene = new Scene(root, prev.getWidth(), prev.getHeight());

        stage.setScene(scene);
    }

    /**
     * Change the scene of a given stage. The path default is /resources.
     *
     * @param stage     stage that is switching scene
     * @param page      fxml page from /recources/gui
     * @param maximized wether the code should be
     */

    public static void setSceneFromStage(Stage stage, String page, boolean maximized) {
        FXMLLoader loader = initLoader(getPath(page));
        stage.setScene(new Scene(loader.getRoot()));
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.setMinWidth(STAGE_MIN_WIDTH);
        stage.setMaximized(maximized);

        stage.show();
    }

    /**
     * Set the scene of the stage by extracting the stage from an event. This might be a better solution if there is no
     * node to extract the stage from.
     *
     * @param actionEvent a javafx event
     * @param page        the page from /src/resources/gui directory
     */

    public static void setSceneFromActionEvent(ActionEvent actionEvent, String page) {
        FXMLLoader loader = initLoader(getPath(page));
        Parent root = loader.getRoot();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene prev = ((Node) actionEvent.getSource()).getScene();
        stage.setScene(new Scene(root, prev.getWidth(), prev.getHeight()));
    }

    /**
     * This is a helper method that checks the loader for exceptions and returns the Parent if successful. This is for
     * easy troubleshooting.
     *
     * @param url the url that is added to the loader
     * @return the loaded Parent
     */

    public static FXMLLoader initLoader(URL url) {
        FXMLLoader loader = null;

        try {
            loader = new FXMLLoader(url);
            loader.load();
        } catch (java.io.IOException e) {
            System.out.println("Could not load XML file... Check the controller class for " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println("Most likely, you mistyped the fxml resource path that you tried to load.");
            System.out.println(
                    "Remember to add / in the beginning of the path and give the path relative to the resources folder.");
            System.out.println("\n" + e.getClass() + ": " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("A different, unexpected exception was thrown while loading the FXML file...\n\n"
                    + e.getClass() + ": " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        }

        // throw new exceptions to not get NullPointerException down the line
        if (loader == null) {
            throw new NullPointerException("Root is null");
        }
        return loader;
    }

    /**
     * Get the path for the fxml pages
     *
     * @param page name of the page
     * @return Full URL
     */

    public static URL getPath(String page) {
        return GUI.class.getResource("/gui/view/" + page + ".fxml");
    }

    /**
     * Main method of the GUI, and starts the application by applying the mainpage to the primaryStage.
     *
     * @param primaryStage primary window of the application
     */

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("/gui/media/logo.png"))));
        primaryStage.setTitle("Wargames");

        setSceneFromStage(primaryStage, "main", true);
    }
}
