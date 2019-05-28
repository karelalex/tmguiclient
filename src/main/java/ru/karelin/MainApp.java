package ru.karelin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.karelin.gui.StageLoader;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void init(){
        log.debug("loading context");
        context = new ClassPathXmlApplicationContext("application-context.xml");
    }
    public void start(Stage stage) throws Exception {

        log.info("Starting Hello JavaFX and Maven demonstration application");
        StageLoader.loadMain().show();
    }
}
