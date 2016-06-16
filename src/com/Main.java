package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author 404NotFound
 */
public class Main extends Application 
{
    private Stage primaryStage;
    private AnchorPane rootLayout;

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        this.primaryStage = primaryStage;

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/view/Login.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
