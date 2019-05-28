package ru.karelin.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageLoader implements ApplicationContextAware {

    private static ApplicationContext staticContext;


    private static final String FXML_DIR = "/fxml/";
    private static final String MAIN_STAGE = "hello";


    public static Parent load(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StageLoader.class.getResource(FXML_DIR + fxmlName + ".fxml"));
        loader.setClassLoader(StageLoader.class.getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load(StageLoader.class.getResourceAsStream(FXML_DIR + fxmlName + ".fxml"));
    }


    public static Stage loadMain() throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(load(MAIN_STAGE), 300, 180);
        stage.setScene(scene);
        stage.setOnHidden(event -> Platform.exit());
        stage.setTitle("Hello JavaFX and Maven");
        scene.getStylesheets().add("/styles/styles.css");
        return stage;
    }

    public static Scene loadProjects() throws IOException {
        Scene scene = new Scene(load("projects"), 1300, 700);
        scene.getStylesheets().add("/styles/styles.css");
        return scene;
    }

    public static Scene loadTasks() throws IOException {
        Scene scene = new Scene(load("tasks"), 1300, 700);
        scene.getStylesheets().add("/styles/styles.css");
        return scene;
    }



    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        StageLoader.staticContext = context;

    }
}

